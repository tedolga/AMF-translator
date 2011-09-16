package edu.leti.jmeter.sampler.gui;

import edu.leti.jmeter.sampler.AMFSender;
import org.apache.jmeter.protocol.http.config.gui.MultipartUrlConfigGui;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

import javax.swing.*;
import java.awt.*;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class AMFSenderGUI extends AbstractSamplerGui {
    private MultipartUrlConfigGui urlConfigGui;
    private AMFRequestPanel amfPanel;

    public AMFSenderGUI() {
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

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        urlConfigGui.configure(element);
    }

    public TestElement createTestElement() {
        AMFSender sender = new AMFSender();
        modifyTestElement(sender);
        return sender;
    }

    public void modifyTestElement(TestElement testElement) {
        testElement.clear();
        super.configureTestElement(testElement);
        urlConfigGui.modifyTestElement(testElement);
        this.configure(testElement);
    }

    @Override
    public void clearGui() {
        super.clearGui();
        urlConfigGui.clear();
    }
}
