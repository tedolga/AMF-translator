package edu.leti.jmeter.sampler;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.gui.JLabeledTextField;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Графический интерфейс AMF sampler
 *
 * @author Tedikova O.
 * @version 1.0
 */
public class AmfRPCSamplerGui extends AbstractSamplerGui {
    private JLabeledTextField endpointUrlField;
    private JLabeledTextField amfCallField;
    private ParametersPanel parametersPanel;

    public AmfRPCSamplerGui() {
        super();
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);

        endpointUrlField = new JLabeledTextField("Endpoint URL");
        amfCallField = new JLabeledTextField("AMF Call");
        parametersPanel = new ParametersPanel();

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel amfRequestPanel = new JPanel();
        amfRequestPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        amfRequestPanel.add(endpointUrlField, c);

        c.gridy = 2;
        amfRequestPanel.add(amfCallField, c);

        c.gridy = 4;
        c.weightx = 6;
        c.gridheight = 4;
        amfRequestPanel.add(parametersPanel, c);

        amfRequestPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "AMF Request"));

        mainPanel.add(amfRequestPanel, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public String getStaticLabel() {
        return "AMF RPC Sampler";
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
        AmfRPCSampler amfSender = (AmfRPCSampler) element;
        endpointUrlField.setText(amfSender.getEndpointUrl());
        amfCallField.setText(amfSender.getAmfCall());
        parametersPanel.setTableData(amfSender.getParamsTable());
    }

    public TestElement createTestElement() {
        AmfRPCSampler amfSender = new AmfRPCSampler();
        modifyTestElement(amfSender);
        return amfSender;
    }

    /**
     * Метод передаёт в тестовый элемент из GUI необходимые параметры.
     *
     * @param testElement тестовый элемент
     */
    public void modifyTestElement(TestElement testElement) {
        AmfRPCSampler amfSender = (AmfRPCSampler) testElement;
        super.configureTestElement(amfSender);
        amfSender.setEndpointUrl(endpointUrlField.getText());
        amfSender.setAmfCall(amfCallField.getText());
        amfSender.setParameters(getParametersList(parametersPanel.getTableData()));
        amfSender.setParamsTable(parametersPanel.getTableData());
    }

    @Override
    public void clearGui() {
        super.clearGui();
        endpointUrlField.setText("");
        amfCallField.setText("");
        parametersPanel.setTableData(new ArrayList<String[]>());
    }

    private java.util.List<String> getParametersList(List<String[]> params) {
        List<String> result = new ArrayList<String>();
        for (String[] row : params) {
            result.add(row[1]);
        }
        return result;
    }
}

