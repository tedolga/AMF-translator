package edu.leti.jmeter.sampler.gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Tedikova O.
 * @version 1.0
 */

public class AMFRequestGUI extends JPanel {

    private AMFMessageArea messageArea;

    public AMFRequestGUI() {
        init();
    }

    private void init() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("AMF Request"));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 1;
        messageArea = new AMFMessageArea("AMF Message");
        //messageArea.setPreferredSize(new Dimension(0, 500));
        add(messageArea, c);
    }

    public String getMessage() {
        return messageArea.getText();
    }

    public void clearGui() {
    }
}
