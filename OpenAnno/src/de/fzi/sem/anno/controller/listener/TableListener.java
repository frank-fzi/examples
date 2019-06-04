package de.fzi.sem.anno.controller.listener;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import de.fzi.sem.anno.controller.OpenAnnoController;
import de.fzi.sem.anno.model.PotentialMatch;

public class TableListener implements TableModelListener {
	
	private OpenAnnoController controller;

	public TableListener(OpenAnnoController controller) {
		this.controller = controller;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int column = e.getColumn();
		TableModel model = (TableModel) e.getSource();
		if( model.getRowCount() > 0 && column > -1  && model.getValueAt(row, column).getClass().equals(Boolean.class)) {
			System.out.println("Row: " + row + " Column: " + column);
			Boolean selected = (Boolean) model.getValueAt(row, column);
			PotentialMatch dataset = controller.getModel().getResults().get(row);
			dataset.setSelected(selected);	
			System.out.println("New value of row " + row + ": " + dataset.isSelected());
		}
	}
}
