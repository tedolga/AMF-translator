package edu.leti.jmeter.gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Tedikova O.
 * @version 1.0
 */

public class AMFRequestPanel extends JPanel {

    private TextField messageField;
    private JLabel titleLabel;

    public AMFRequestPanel() {
        init();
    }

    private void init() {
        setBorder(BorderFactory.createEtchedBorder());

        titleLabel = new JLabel("AMF Request");

        messageField = new TextField();
        messageField.setText("Here will be your message!");
        messageField.setEditable(true);

        add(titleLabel, BorderLayout.WEST);
        add(messageField, BorderLayout.CENTER);
    }

    public void clearGui() {
    }
}
