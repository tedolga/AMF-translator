package edu.leti.jmeter.sampler.binary;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Tedikova O.
 * @version 1.0
 */

public class ParametersPanel extends JPanel {
    private String[] columnNames;
    private Object[][] rowData;
    private ParamsTableModel tableModel;
    private JTable paramsTable;
    private JButton addButton;
    private JButton deleteButton;

    public ParametersPanel() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout(0, 4));
        columnNames = new String[]{"name", "value"};
        rowData = new Object[][]{};
        paramsTable = new JTable(rowData, columnNames);
        tableModel = new ParamsTableModel();
        paramsTable.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(paramsTable);

        addButton = new JButton("Add");
        addButton.addActionListener(new AddRowListener());
        deleteButton = new JButton("Delete");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton, BorderLayout.CENTER);
        buttonPanel.add(deleteButton, BorderLayout.CENTER);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Request Parameters"));
    }

    private class AddRowListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            tableModel.addRow();

        }
    }

    private class ParamsTableModel extends AbstractTableModel {

        public void addRow() {

        }

        public int getRowCount() {
            return rowData.length;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        public Class<?> getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }


        public Object getValueAt(int rowIndex, int columnIndex) {
            return rowData[rowIndex][columnIndex];
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            rowData[rowIndex][columnIndex] = aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }


        public void addTableModelListener(TableModelListener l) {

        }

        public void removeTableModelListener(TableModelListener l) {

        }
    }
}
