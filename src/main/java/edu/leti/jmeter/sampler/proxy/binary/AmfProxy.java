package edu.leti.jmeter.sampler.proxy.binary;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.ClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class AmfProxy {

    private static final Logger logger = LoggerFactory.getLogger(AmfProxy.class);

    private int localPort;
    private String remoteHost;
    private int remotePort;
    private ExecutorService executor;
    private ServerBootstrap sb;

    public AmfProxy(int proxyPort, String serverHost, int serverPort) {
        localPort = proxyPort;
        remoteHost = serverHost;
        remotePort = serverPort;
    }

    public void start() {
        logger.debug("Proxying *:" + localPort + " to " + remoteHost + ':' + remotePort + " ...");

        // Configure the bootstrap.
        executor = Executors.newSingleThreadExecutor();
        sb = new ServerBootstrap(
                new NioServerSocketChannelFactory(executor, executor));

        // Set up the event pipeline factory.
        ClientSocketChannelFactory cf =
                new NioClientSocketChannelFactory(executor, executor);

        sb.setPipelineFactory(
                new HexDumpProxyPipelineFactory(cf, remoteHost, remotePort));

        // Start up the server.
        sb.bind(new InetSocketAddress(localPort));
    }

    public void stop() {
        if (executor != null) {
            executor.shutdown();
        }
    }
}
