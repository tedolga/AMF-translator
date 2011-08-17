package edu.leti.jmeter.gui;

import org.apache.jorphan.gui.JLabeledTextArea;

import javax.swing.*;
import java.awt.*;

/**
 * @author Tedikova O.
 * @version 1.0
 */

public class AMFRequestPanel extends JPanel {

    private JLabeledTextArea messageArea;

    public AMFRequestPanel() {
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

        messageArea = new JLabeledTextArea("AMF Message");
        messageArea.setPreferredSize(new Dimension(0, 1000));

        add(messageArea, c);
    }

    public void clearGui() {
    }
}
