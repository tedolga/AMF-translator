package edu.leti.jmeter.proxy;

/**
 * Абстрактный класс для proxy
 *
 * @author Tedikova O.
 * @version 1.0
 */
public abstract class AbstractProxy implements ProxyInterface {

    protected final int localPort;

    public AbstractProxy(int proxyPort) {
        localPort = proxyPort;
    }

    @Override
    public int getLocalPort() {
        return localPort;
    }
}
