package de.fzi.sem.anno.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {
	private static final long serialVersionUID = 2369817266254686886L;
	private String[] columnNames = {};
	private List<Object[]> data;
	private List<Integer> editCol;
	
	public TableModel() {
		data = new ArrayList<Object[]>();
		editCol = new ArrayList<Integer>();		
	}

	public void addEditCol(int i) {
		editCol.add(i);
	}
	
	public void addAllEditCol(List<Integer> list) {
		editCol.addAll(list);
	}
	
	public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data.get(row)[col];
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int c) {
    	try {
    		return getValueAt(0, c).getClass();
    	} catch (Exception e) {
    		System.out.println("Error at " + c + ": " + e.getMessage() + " Value: " + getValueAt(0, c) );
    		return null;
    	}        
    }
    
    public boolean isCellEditable(int row, int col) {
    	for(Integer i: editCol) {
    		if(i.equals(col)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public void setValueAt(Object value, int row, int col) {
    	if(row < data.size()) {
    		data.get(row)[col] = value;
    		fireTableCellUpdated(row, col);
    	} else {
    		System.out.println("ERROR: row " + row + " size " + data.size());
    		data.get(row)[col] = value;
    		fireTableCellUpdated(row, col);
    	}          
    }
    
    public void addRow(Object[] row) {
    	data.add(row);
    	fireTableDataChanged();
    }
    
    public void setData (Object[][] data) {
    	setData(new ArrayList<Object[]>(Arrays.asList(data)));
    }
    
    public void setData (List<Object[]> data) {
    	this.data = data;
    	fireTableDataChanged();
    }

	public void setColNames(String[] columnNames) {
		this.columnNames = columnNames;	
		fireTableStructureChanged();
	}

	public void addEditColumn(int i) {
		this.editCol.add(i);		
	}

	public String[] getColumnNames() {
		return this.columnNames;
	}

	public List<Object[]> getData() {
		return data;
	}
}
