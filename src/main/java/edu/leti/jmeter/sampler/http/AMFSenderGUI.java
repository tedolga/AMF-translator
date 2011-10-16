package edu.leti.jmeter.sampler.http;

import org.apache.jmeter.protocol.http.config.gui.MultipartUrlConfigGui;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextArea;

import javax.swing.*;
import java.awt.*;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class AMFSenderGUI extends AbstractSamplerGui {
    private MultipartUrlConfigGui urlConfigGui;
    private JLabeledTextArea amfMessageArea;

    public AMFSenderGUI() {
        super();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());

        urlConfigGui = new MultipartUrlConfigGui();
        amfMessageArea = new JLabeledTextArea();

        JPanel amfPanel = new JPanel(new BorderLayout());
        amfPanel.add(amfMessageArea, BorderLayout.CENTER);
        amfPanel.setBorder(BorderFactory.createTitledBorder("AMF Request"));

        Box box = Box.createVerticalBox();
        box.add(makeTitlePanel());
        box.add(urlConfigGui, BorderLayout.CENTER);
        box.add(amfPanel, BorderLayout.CENTER);
        add(box, BorderLayout.CENTER);
    }

    @Override
    public String getStaticLabel() {
        return "AMF message sender";
    }

    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        AMFHTTPSampler AMFHTTPSampler = (AMFHTTPSampler) element;
        amfMessageArea.setText(AMFHTTPSampler.getAmfMessage());
        urlConfigGui.configure(AMFHTTPSampler);
    }

    public TestElement createTestElement() {
        AMFHTTPSampler sender = new AMFHTTPSampler();
        modifyTestElement(sender);
        return sender;
    }

    public void modifyTestElement(TestElement testElement) {
        AMFHTTPSampler AMFHTTPSampler = (AMFHTTPSampler) testElement;
        super.configureTestElement(AMFHTTPSampler);
        urlConfigGui.modifyTestElement(AMFHTTPSampler);
        AMFHTTPSampler.setAmfMessage(amfMessageArea.getText());
    }

    @Override
    public void clearGui() {
        super.clearGui();
        urlConfigGui.clear();
        amfMessageArea.setText("");
    }
}
