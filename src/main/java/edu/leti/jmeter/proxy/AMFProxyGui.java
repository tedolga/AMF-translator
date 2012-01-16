package edu.leti.jmeter.proxy;

import org.apache.jmeter.control.gui.LogicControllerGui;
import org.apache.jmeter.gui.UnsharedComponent;
import org.apache.jmeter.gui.util.MenuFactory;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class AMFProxyGui extends LogicControllerGui implements UnsharedComponent {
    private JLabeledTextField proxyPort;
    private AMFProxyControl amfProxyControl;
    JButton startButton;
    JButton stopButton;

    public AMFProxyGui() {
        super();
        init();
    }

    public void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH);
        JLabeledTextField proxyHost = new JLabeledTextField("AmfProxy Host:  ");
        proxyHost.setText("localhost");
        proxyHost.setEnabled(false);
        proxyPort = new JLabeledTextField("AmfProxy Port:  ");

        startButton = new JButton("Start");
        startButton.addActionListener(new StartListener());
        stopButton = new JButton("Stop");
        stopButton.addActionListener(new StopListener());

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel proxyPanel = new JPanel();
        JPanel buttonPanel = new JPanel();

        proxyPanel.setLayout(new GridBagLayout());

        mainPanel.add(proxyPanel, BorderLayout.NORTH);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        proxyPanel.add(proxyHost, c);

        c.gridy = 2;
        proxyPanel.add(proxyPort, c);

        proxyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Settings"));

        buttonPanel.add(startButton, BorderLayout.CENTER);
        buttonPanel.add(stopButton, BorderLayout.CENTER);

        mainPanel.add(proxyPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public String getStaticLabel() {
        return "AMF Proxy Server";
    }

    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    /**
     * Метод, вызываемый при перерисовке GUI тестового элемнта. Отображает в GUI параметры, содержащиеся в сэмплере
     *
     * @param element тестовый элемент (экземпляр сэмплера)
     */
    @Override
    public void configure(TestElement element) {
        super.configure(element);
        amfProxyControl = (AMFProxyControl) element;
        proxyPort.setText(amfProxyControl.getProxyPort());
    }

    public TestElement createTestElement() {
        amfProxyControl = new AMFProxyControl();
        modifyTestElement(amfProxyControl);
        return amfProxyControl;
    }

    /**
     * Метод передаёт в тестовый элемент из GUI необходимые параметры.
     *
     * @param testElement тестовый элемент
     */
    public void modifyTestElement(TestElement testElement) {
        amfProxyControl = (AMFProxyControl) testElement;
        super.configureTestElement(amfProxyControl);
        amfProxyControl.setProxyPort(proxyPort.getText());
    }

    @Override
    public void clearGui() {
        super.clearGui();
        proxyPort.setText("");
    }

    @Override
    public Collection<String> getMenuCategories() {
        return Arrays.asList(MenuFactory.NON_TEST_ELEMENTS);
    }

    private class StartListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            startProxy();
        }
    }

    private class StopListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            stopProxy();
        }
    }

    private void startProxy() {
        startButton.setEnabled(false);
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                modifyTestElement(amfProxyControl);
                amfProxyControl.startProxy();
                return null;
            }
        };
        worker.execute();
    }

    private void stopProxy() {
        amfProxyControl.stopProxy();
        startButton.setEnabled(true);
    }
}
