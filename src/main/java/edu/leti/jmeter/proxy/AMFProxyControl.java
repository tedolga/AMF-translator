package edu.leti.jmeter.proxy;

import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class AMFProxyControl extends GenericController implements SamplerDeliverer {
    private static final Logger logger = LoggerFactory.getLogger(AMFProxyControl.class);

    private static final String PROXY_PORT = "amf_proxy_port";
    private static final String SERVER_HOST = "amf_server_host";
    private static final String SERVER_PORT = "amf_server_port";
    private static final String PROXY_TYPE = "amf_proxy_type";


    public void deliverSampler(HTTPSamplerBase sampler, TestElement[] subConfigs, SampleResult result) {

    }

    public enum ProxyTypes {
        BINARY_PROXY_TYPE("Binary Proxy"),
        HTTP_PROXY_TYPE("HTTP Proxy");

        private final String type;

        ProxyTypes(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }

    }

    private AmfHttpProxy amfProxy;

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

    public String getProxyType() {
        return getPropertyAsString(PROXY_TYPE);
    }

    public void setProxyType(String proxyType) {
        setProperty(PROXY_TYPE, proxyType);
    }

    public synchronized void startProxy() throws IOException {
        if (amfProxy == null) {
            amfProxy = new AmfHttpProxy(Integer.valueOf(getProxyPort()), getServerHost(),
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

