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
        Box box = Box.createVerticalBox();
        box.add(makeTitlePanel());
        add(box, BorderLayout.NORTH);
    }


    public String getStaticLabel() {
        return "AMF message sender";  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getLabelResource() {
        return getClass().getCanonicalName();  //To change body of implemented methods use File | Settings | File Templates.
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
