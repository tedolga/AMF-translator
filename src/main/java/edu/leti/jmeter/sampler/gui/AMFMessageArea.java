package edu.leti.jmeter.sampler.gui;

import org.apache.jorphan.gui.JLabeledTextArea;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;


/**
 * @author Tedikova O.
 * @version 1.0
 */

public class AMFMessageArea extends JLabeledTextArea {
    private JLabel mLabel;

    private JTextArea mTextArea;
    // Maybe move to vector if MT problems occur
    private final ArrayList<ChangeListener> mChangeListeners = new ArrayList<ChangeListener>(3);
    // A temporary cache for the focus listener


    public AMFMessageArea(String pLabel) {
        super();
        mLabel = new JLabel(pLabel);
        init();
        mTextArea.setText("Your Message");
    }

    private void init() {
        setLayout(new BorderLayout());

        mTextArea = new JTextArea();
        mTextArea.setRows(4);
        mTextArea.setLineWrap(true);
        mTextArea.setWrapStyleWord(true);
        // Register the handler for focus listening. This handler will
        // only notify the registered when the text changes from when
        // the focus is gained to when it is lost.
        mTextArea.addFocusListener(this);

        // Add the sub components
        this.add(mLabel, BorderLayout.NORTH);
        this.add(new JScrollPane(mTextArea), BorderLayout.CENTER);
    }

}
