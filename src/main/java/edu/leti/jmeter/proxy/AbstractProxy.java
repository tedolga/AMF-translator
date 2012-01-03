package edu.leti.jmeter.proxy;

/**
 * Абстрактный класс для proxy
 *
 * @author Tedikova O.
 * @version 1.0
 */
public abstract class AbstractProxy implements ProxyInterface {

    protected final int localPort;
    protected final String remoteHost;
    protected final int remotePort;

    public AbstractProxy(int proxyPort, int serverPort, String serverHost) {
        localPort = proxyPort;
        remotePort = serverPort;
        remoteHost = serverHost;
    }

    public int getLocalPort() {
        return localPort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }
}
