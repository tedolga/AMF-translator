package edu.leti.jmeter.proxy;

import org.apache.jmeter.protocol.http.proxy.Proxy;
import org.apache.jmeter.protocol.http.proxy.ProxyControl;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.util.JOrphanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс, реализующий http proxy сервер.
 *
 * @author Tedikova O.
 * @version 1.0
 */
public class AmfHttpProxy extends AbstractProxy implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AmfHttpProxy.class);

    private final SamplerDeliverer deliverer;
    private boolean isRunning;
    private Thread workerThread;
    private ServerSocket mainSocket;

    /**
     * Создаёт экземпляр {@link AmfHttpProxy} на localhost c указанными параметрами.
     *
     * @param deliverer используется для передачи параметров перехваченного запроса в {@link AMFProxyControl}
     */
    public AmfHttpProxy(int proxyPort, String serverHost, int serverPort, SamplerDeliverer deliverer) {
        super(proxyPort, serverPort, serverHost);
        this.deliverer = deliverer;
    }

    public void start() {
        workerThread = new Thread(this, "AmfHttpProxy worker thread");
        workerThread.start();
    }

    public void stop() {
        isRunning = false;
        JOrphanUtils.closeQuietly(mainSocket);
        if (workerThread != null && workerThread.isAlive() && !workerThread.isInterrupted()) {
            workerThread.interrupt();
        }
    }

    @Override
    public void run() {
        isRunning = true;
        try {
            mainSocket = new ServerSocket(getLocalPort());

            while (isRunning) {
                Socket clientSocket = null;
                try {
                    clientSocket = mainSocket.accept();
                    doLegacyRead(clientSocket);
                } catch (InterruptedException e) {
                    // Timeout occurred. Ignore, and keep looping until we're
                    logger.debug(String.format("Proxy client on port %d was interrupted", getLocalPort()), e);
                } catch (Exception e) {
                    logger.error("Exception occurred", e);
                } finally {
                    JOrphanUtils.closeQuietly(clientSocket);
                }
            }
        } catch (IOException e) {
            logger.error(String.format("ServerSocket accepting on port %d failed", getLocalPort()), e);
        } finally {
            JOrphanUtils.closeQuietly(mainSocket);
        }
    }

    /**
     * Метод обрабатывает один http запрос. В методе используется
     * код {@link org.apache.jmeter.protocol.http.proxy.Proxy}.
     *
     * @param socket сокет
     * @throws NoSuchMethodException     в случае, если класс не содержит указанного метода.
     * @throws IllegalAccessException    в случае, если доступ к методу не разрешён.
     * @throws InvocationTargetException в случае, если вызов метода не разрешён.
     * @throws InterruptedException      р
     */
    protected void doLegacyRead(Socket socket) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InterruptedException {
        Map<String, String> pageEncodings = Collections.synchronizedMap(new HashMap<String, String>());
        Map<String, String> formEncodings = Collections.synchronizedMap(new HashMap<String, String>());
        Proxy proxy = new Proxy();
        Method method = proxy.getClass().getDeclaredMethod("configure", Socket.class, ProxyControl.class, Map.class, Map.class);
        method.setAccessible(true);

        method.invoke(proxy, socket, new ProxyControl() {
            /**
             * Receives the recorded sampler from the proxy server for placing in the
             * test tree. param serverResponse to be added to allow saving of the
             * server's response while recording. A future consideration.
             */
            @Override
            public void deliverSampler(HTTPSamplerBase sampler, TestElement[] subConfigs, SampleResult result) {
                deliverer.deliverSampler(sampler, subConfigs, result);
            }
        }, pageEncodings, formEncodings);
        proxy.start();
        proxy.join();
    }
}
