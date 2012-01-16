package edu.leti.jmeter.proxy;

import edu.leti.amf.MessageDecoder;
import edu.leti.jmeter.sampler.AmfRPCSampler;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.io.amf.MessageBody;
import flex.messaging.messages.RemotingMessage;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.exceptions.IllegalUserActionException;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.gui.tree.JMeterTreeModel;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.util.HTTPFileArg;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.util.JOrphanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, отвечающий за управление прокси-сервером
 *
 * @author Tedikova O.
 * @version 1.0
 */
public class AMFProxyControl extends GenericController implements SamplerDeliverer {

    private static final Logger logger = LoggerFactory.getLogger(AMFProxyControl.class);

    private static final String PROXY_PORT = "amf_proxy_port";
    private static final String SERVER_HOST = "amf_server_host";
    private static final String SERVER_PORT = "amf_server_port";

    /**
     * Метод создаёт новый {@link AmfRPCSampler} на каждое полученное сообщение типа RemotingMessage и отображает
     * его в GUI
     *
     * @param sampler    {@inheritDoc}
     * @param subConfigs {@inheritDoc}
     * @param result     {@inheritDoc}
     */
    public void deliverSampler(HTTPSamplerBase sampler, TestElement[] subConfigs, SampleResult result) {
        MessageDecoder decoder = new MessageDecoder();
        HTTPFileArg[] httpFiles = sampler.getHTTPFiles();
        for (HTTPFileArg httpFile : httpFiles) {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(new File(httpFile.getPath()));
                ActionMessage actionMessage = decoder.getActionMessage(inputStream);
                MessageBody messageBody = actionMessage.getBody(0);
                Object[] data = (Object[]) messageBody.getData();
                if (data.length > 0 && data[0] instanceof RemotingMessage) {
                    RemotingMessage remotingMessage = (RemotingMessage) data[0];
                    String operation = remotingMessage.getOperation();
                    String destination = remotingMessage.getDestination();
                    List parameters = remotingMessage.getParameters();
                    List<String[]> stringParameters = new ArrayList<String[]>();
                    for (Object parameter : parameters) {
                        stringParameters.add(new String[]{"", parameter.toString()});
                    }
                    AmfRPCSampler amfRPCSampler = new AmfRPCSampler();
                    amfRPCSampler.setAmfCall(String.format("%s.%s", destination, operation));
                    amfRPCSampler.setEndpointUrl(sampler.getUrl().toString());
                    amfRPCSampler.setParamsTable(stringParameters);
                    amfRPCSampler.setName(sampler.getUrl().toString());
                    JMeterTreeNode myTarget = findFirstNodeOfType(AMFProxyControl.class);
                    try {
                        JMeterTreeModel treeModel = GuiPackage.getInstance().getTreeModel();
                        treeModel.addComponent(amfRPCSampler, myTarget);
                    } catch (IllegalUserActionException e) {
                        JMeterUtils.reportErrorToUser(e.getMessage());
                    }
                }
            } catch (Exception e) {
                logger.error("Can't open file " + httpFile.getPath(), e);
            } finally {
                JOrphanUtils.closeQuietly(inputStream);
            }
        }
    }

    /**
     * Находит первый активный узел указанного типа в дереве элементов GUI
     *
     * @param type тип элемента
     * @return первый активный узел указанного типа
     */
    private JMeterTreeNode findFirstNodeOfType(Class<?> type) {
        JMeterTreeModel treeModel = GuiPackage.getInstance().getTreeModel();
        List<JMeterTreeNode> nodes = treeModel.getNodesOfType(type);
        for (JMeterTreeNode node : nodes) {
            if (node.isEnabled()) {
                return node;
            }
        }
        return null;
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

    public synchronized void startProxy() {
        if (amfProxy == null) {
            try {
                amfProxy = new AmfHttpProxy(Integer.parseInt(getProxyPort()), "", 0, this);
            } catch (NumberFormatException ex) {
                JMeterUtils.reportErrorToUser("Couldn't create proxy server with proxy port= " + getProxyPort() +
                        "; server host=" + getServerHost() + " and server port=" + getServerPort());
            }
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

