package edu.leti.jmeter.proxy;

import edu.leti.amf.MessageDecoder;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.io.amf.MessageBody;
import flex.messaging.messages.RemotingMessage;
import org.apache.jmeter.protocol.http.proxy.Proxy;
import org.apache.jmeter.protocol.http.proxy.ProxyControl;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.util.HTTPFileArg;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.util.JOrphanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class AmfHttpProxy extends AbstractProxy {

    private static final Logger logger = LoggerFactory.getLogger(AmfHttpProxy.class);

    private boolean running = true;
    private ServerSocket mainSocket;

    public AmfHttpProxy(int proxyPort, String serverHost, int serverPort) {
        super(proxyPort, serverPort, serverHost);

    }

    @Override
    public void start() throws IOException {
        running = true;
        Thread daemon = new Thread() {
            /**
             * If this thread was constructed using a separate
             * <code>Runnable</code> run object, then that
             * <code>Runnable</code> object's <code>run</code> method is called;
             * otherwise, this method does nothing and returns.
             * <p/>
             * Subclasses of <code>Thread</code> should override this method.
             *
             * @see #start()
             * @see #stop()
             * @see #Thread(ThreadGroup, Runnable, String)
             */
            @Override
            public void run() {
                try {
                    mainSocket = new ServerSocket(getLocalPort());
                } catch (IOException e) {
                    logger.error("Cannot create socket on port " + getLocalPort(), e);
                }
                Map<String, String> pageEncodings = Collections.synchronizedMap(new HashMap<String, String>());
                Map<String, String> formEncodings = Collections.synchronizedMap(new HashMap<String, String>());

                try {
                    while (running) {
                        try {
                            // Listen on main socket
                            Socket clientSocket = mainSocket.accept();
                            if (running) {
                                // Pass request to new proxy thread
                                Proxy thd = new Proxy();
                                Method method = thd.getClass().getDeclaredMethod("configure", Socket.class, ProxyControl.class, Map.class, Map.class);
                                method.setAccessible(true);
                                method.invoke(thd, clientSocket, new ProxyControl() {
                                    /**
                                     * Receives the recorded sampler from the proxy server for placing in the
                                     * test tree. param serverResponse to be added to allow saving of the
                                     * server's response while recording. A future consideration.
                                     */
                                    @Override
                                    public void deliverSampler(HTTPSamplerBase sampler, TestElement[] subConfigs, SampleResult result) {
                                        System.out.println(sampler.getPath());
                                        MessageDecoder decoder = new MessageDecoder();
                                        HTTPFileArg[] httpFiles = sampler.getHTTPFiles();
                                        for (HTTPFileArg httpFile : httpFiles) {
                                            System.out.println(httpFile.getPath());
                                            InputStream inputStream = null;
                                            try {
                                                try {
                                                    inputStream = new FileInputStream(new File(httpFile.getPath()));
                                                    //System.out.println(decoder.getTrace(inputStream));
                                                    ActionMessage actionMessage = decoder.getActionMessage(inputStream);
                                                    MessageBody messageBody = actionMessage.getBody(0);
                                                    Object[] data = (Object[]) messageBody.getData();
                                                    if (data[0] instanceof RemotingMessage) {
                                                        RemotingMessage remotingMessage = (RemotingMessage) data[0];
                                                        String operation = remotingMessage.getOperation();
                                                        String destination = remotingMessage.getDestination();
                                                        List parameters = remotingMessage.getParameters();
                                                    }
                                                } catch (Exception e) {
                                                    logger.error("Can't open file " + httpFile.getPath(), e);
                                                }
                                            } finally {
                                                if (inputStream != null) {
                                                    try {
                                                        inputStream.close();
                                                    } catch (IOException ignored) {
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }, pageEncodings, formEncodings);
                                thd.start();
                            }
                        } catch (InterruptedIOException e) {
                            continue;
                            // Timeout occurred. Ignore, and keep looping until we're
                            // told to stop running.
                        }
                    }
                    logger.info("Proxy Server stopped");
                } catch (Exception e) {
                    logger.warn("Proxy Server stopped", e);
                } finally {
                    JOrphanUtils.closeQuietly(mainSocket);
                }

                // Clear maps
                pageEncodings = null;
                formEncodings = null;
            }
        };
        daemon.setDaemon(true);
        daemon.start();

    }

    @Override
    public void stop() {
        running = false;
    }
}
