package edu.leti.jmeter.gui;

import edu.leti.jmeter.elements.AMFSender;
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

    public SenderGUI() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        TextField messageField = new TextField();
        messageField.setText("Here will be your message!");
        messageField.setEditable(false);
        Box box = Box.createVerticalBox();
        box.add(makeTitlePanel());
        Box messageBox = Box.createVerticalBox();
        messageBox.add(makeScrollPane(messageField));
        add(box, BorderLayout.NORTH);
        add(messageBox, BorderLayout.CENTER);
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
