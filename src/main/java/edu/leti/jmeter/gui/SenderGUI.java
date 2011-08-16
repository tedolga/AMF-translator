package edu.leti.jmeter.gui;

import edu.leti.jmeter.elements.AMFSender;
import org.apache.jmeter.protocol.http.config.gui.MultipartUrlConfigGui;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class SenderGUI extends AbstractSamplerGui {
    private MultipartUrlConfigGui urlConfigGui;
    private AMFRequestPanel amfPanel;

    public SenderGUI() {
        super();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());

        urlConfigGui = new MultipartUrlConfigGui();
        amfPanel = new AMFRequestPanel();

        Box box = Box.createVerticalBox();
        box.add(makeTitlePanel());
        box.add(urlConfigGui, BorderLayout.CENTER);
        box.add(amfPanel, BorderLayout.CENTER);
        add(box, BorderLayout.CENTER);
    }

    public String getStaticLabel() {
        return "AMF message sender";
    }

    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    public void configureTestElement(TestElement element) {

    }

    public TestElement createTestElement() {
        AMFSender sender = new AMFSender();
        modifyTestElement(sender);
        return sender;
    }

    public void modifyTestElement(TestElement testElement) {
        super.configureTestElement(testElement);
        if (testElement instanceof HTTPSampler) {
            HTTPSampler sampler = (HTTPSampler) testElement;
        }
    }
}
