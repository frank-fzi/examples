package de.fzi.sem.anno.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import de.fzi.sem.anno.model.Button;

public class TablePanel extends JPanel {	
	private static final long serialVersionUID = 1653296325017593583L;
	
	// Data
	private String tableLabel = "";
	private TableModel tableModel;	
	private String buttonValue = "";
	
	// Elements
	private JLabel label = null;
	private JTable table = null;
	
	// Listener
	private ActionListener comboListener = null;
	private TableModelListener tableListener = null;
	private ActionListener buttonListener = null;	
	
	public TablePanel() {	
		super();
		label = new JLabel(tableLabel);
		tableModel = new TableModel();
	}	
	
	public void build() {	
		// set up components		
		table = new JTable(tableModel);
	    table.setPreferredScrollableViewportSize(new Dimension((int)table.getPreferredScrollableViewportSize().getWidth(), 300));
		JScrollPane scrollPane = new JScrollPane(table);	
		try {
			table.getColumnModel().getColumn(1).setCellRenderer(new ListRenderer());
			table.getColumnModel().getColumn(2).setCellRenderer(new ListRenderer());
			table.getColumnModel().getColumn(3).setCellRenderer(new ListRenderer());
//			if(info) {				
//				setUpAccordanceColumn(table, table.getColumnModel().getColumn(3), matches, total);
//			} else {
//				setUpAccordanceColumn(table, table.getColumnModel().getColumn(3));
//			}			
		} catch (Exception e) {
			System.out.println("Couldn't set up accordance column: " + e.getMessage());
		}
		
		table.getModel().addTableModelListener(tableListener);
		JButton button = new JButton(buttonValue);
		button.addActionListener(buttonListener);	
		
		// set up panel
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(label, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(button, BorderLayout.SOUTH);
	}
	
//	private void resetAccordanceColumn() {
//		try {
//			table.getColumnModel().getColumn(1).setCellRenderer(new ListRenderer());
//			table.getColumnModel().getColumn(2).setCellRenderer(new ListRenderer());
//			table.getColumnModel().getColumn(3).setCellRenderer(new ListRenderer());
////			if(info) {				
////				setUpAccordanceColumn(table, table.getColumnModel().getColumn(3), matches, total);
////			} else {
////				setUpAccordanceColumn(table, table.getColumnModel().getColumn(3));
////			}			
//		} catch (Exception e) {
//			System.out.println("Couldn't set up accordance column: " + e.getMessage());
//		}
//	}
	
//	private void setUpAccordanceColumn(JTable table, TableColumn accordanceColumn) {
//    	accordanceColumn.setCellRenderer(new ArrayRenderer());
//    	accordanceColumn.setCellEditor(new ArrayEditor());			
//	}
//	
//	private void setUpAccordanceColumn(JTable table, TableColumn accordanceColumn, Integer matches, Integer total) {
//    	accordanceColumn.setCellRenderer(new ArrayRenderer(matches, total));
//    	accordanceColumn.setCellEditor(new ArrayEditor());			
//	}
	
	public void setColumnWidth(int col, int width) {
		if(col > table.getColumnCount()) throw new IllegalArgumentException();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		TableColumn tableColumn = table.getColumnModel().getColumn(col);
		tableColumn.setPreferredWidth(width);
		tableColumn.setResizable(false);
		tableColumn.setMaxWidth(width);
	}

	// Getter / Setter
	
	public JPanel getComponent() {
		return this;
	}

	public String getTableLabel() {
		return tableLabel;
	}

	public void setTableLabel(String tableLabel) {
		this.label.setText(tableLabel);
	}

	public String[] getColumnNames() {
		return tableModel.getColumnNames();
	}

	public void setColumnNames(String[] columnNames) {
		this.tableModel.setColNames(columnNames);
	}

	public List<Object[]> getTableData() {
		return tableModel.getData();
	}

	public void setTableData(Object[][] tableData) {
		tableModel.setData(tableData);
	}
	
	public void addTableRow(Object[] row) {
		tableModel.addRow(row);
	}

	public String getButtonValue() {
		return buttonValue;
	}

	public ActionListener getComboListener() {
		return comboListener;
	}

	public void setComboListener(ActionListener comboListener) {
		this.comboListener = comboListener;
	}

	public TableModelListener getTableListener() {
		return tableListener;
	}

	public void setTableListener(TableModelListener tableListener) {
		this.tableListener = tableListener;
	}

	public ActionListener getButtonListener() {
		return buttonListener;
	}

	public void updateTable(int row, int col, Object value) {
		table.setValueAt(value, row, col);		
	}

	public void setButton(Button button) {
		this.buttonValue = button.getValue();
		this.buttonListener = button.getListener();
	}

	public void addEditColumn(int i) {
		tableModel.addEditColumn(i);		
	}
}
