package edu.leti.jmeter.sampler;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tedikova O.
 * @version 1.0
 */

public class ParametersPanel extends JPanel {
    private ParamsTableModel tableModel;
    private JTable paramsTable;
    private JButton addButton;
    private JButton deleteButton;

    public ParametersPanel() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout(0, 4));
        tableModel = new ParamsTableModel();
        paramsTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(paramsTable);

        addButton = new JButton("Add");
        addButton.addActionListener(new AddRowListener());
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteRowListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton, BorderLayout.CENTER);
        buttonPanel.add(deleteButton, BorderLayout.CENTER);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Request Parameters"));
    }

    public List<String[]> getTableData() {
        return tableModel.getRows();
    }

    public void setTableData(List<String[]> rows) {
        tableModel.setRows(rows);
    }

    private class AddRowListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            tableModel.addRow();

        }
    }

    private class DeleteRowListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            tableModel.deleteRow();
        }
    }

    private class ParamsTableModel extends AbstractTableModel {

        private String[] columnNames = new String[]{"name", "value"};
        private java.util.List<String[]> rows = new ArrayList<String[]>();

        public void addRow() {
            rows.add(new String[]{"", ""});
            int firstRow = rows.size() - 1;
            fireTableRowsInserted(0, firstRow);
        }

        public void deleteRow() {
            int selectedRow = ParametersPanel.this.paramsTable.getSelectedRow();
            rows.remove(selectedRow);
            fireTableRowsDeleted(selectedRow, selectedRow);
        }

        public int getRowCount() {
            return rows.size();
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
            return rows.get(rowIndex)[columnIndex];
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            rows.get(rowIndex)[columnIndex] = (String) aValue;
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        public List<String[]> getRows() {
            return rows;
        }

        public void setRows(List<String[]> rows) {
            this.rows = rows;
        }
    }
}
