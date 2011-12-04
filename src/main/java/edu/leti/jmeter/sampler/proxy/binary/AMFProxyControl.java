package edu.leti.jmeter.sampler.proxy.binary;

import org.apache.jmeter.control.GenericController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class AMFProxyControl extends GenericController {
    private static final Logger logger = LoggerFactory.getLogger(AMFProxyControl.class);

    private static final String PROXY_PORT = "amf_proxy_port";
    private static final String SERVER_HOST = "amf_server_host";
    private static final String SERVER_PORT = "amf_server_port";
    private AmfProxy amfProxy;

    public String getProxyPort() {
        return getPropertyAsString(PROXY_PORT);
    }

    public void setProxyPort(String proxyPort) {
        setProperty(PROXY_PORT, proxyPort);
    }

    public String getServerHost() {
        return getPropertyAsString(SERVER_HOST);
    }

    public void setServerHost(String serverHost) {
        setProperty(SERVER_HOST, serverHost);
    }

    public String getServerPort() {
        return getPropertyAsString(SERVER_PORT);
    }

    public void setServerPort(String serverPort) {
        setProperty(SERVER_PORT, serverPort);
    }

    public synchronized void startProxy() {
        if (amfProxy == null) {
            amfProxy = new AmfProxy(Integer.valueOf(getProxyPort()), getServerHost(),
                    Integer.valueOf(getServerPort()));
        } else {
            amfProxy.stop();
        }
        amfProxy.start();
    }

    public void stopProxy() {
        if (amfProxy != null) {
            amfProxy.stop();
        }
    }
}

