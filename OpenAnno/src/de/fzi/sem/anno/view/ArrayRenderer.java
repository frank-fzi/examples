package de.fzi.sem.anno.view;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellRenderer;

//public class ArrayRenderer extends DefaultTableCellRenderer {
public class ArrayRenderer extends JLabel implements TableCellRenderer {
	private static final long serialVersionUID = 2692773106628050000L;
	
	private Integer matches;
	private Integer total;
	private boolean info = false;
//	Integer size;

	public ArrayRenderer(Integer matches, Integer total ) {
		super();			
		this.matches = matches;
		this.total = total;
		this.info = true;
	 }
	
	public ArrayRenderer() {
		super();
	}
	
	public Component getTableCellRendererComponent(  
			JTable table, Object value,  
            boolean isSelected, boolean hasFocus,  
            int row, int column) {  
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().registerComponent(this);
		if(value == null) {
			System.out.println("column value is null");
		} else if(value.getClass() == String[].class) {
			String[] array = (String[]) value;
			StringBuilder toolTipText = new StringBuilder();
			toolTipText.append("<html>");
			for(int i = 0; i < array.length; i++) {
				toolTipText.append(array[i]);
				toolTipText.append("<br>");
			}		
			toolTipText.append("</html>");
			StringBuilder sb = new StringBuilder();
//			Integer count = new Integer(array.length);
			if (info) {
				sb.append(matches);
				sb.append(" of ");
				sb.append(total);	
				sb.append(" (");
				sb.append(matches*100/total);
				sb.append("%, see tooltip)");
			} else {
				sb.append(" (see tooltip)");
			}			
			setText(sb.toString());
			setToolTipText(toolTipText.toString());
		} else if(value.getClass() == String.class) {
			setText(value.toString());
			setToolTipText(value.toString());
		} else {
			setText("Value is not a string nor an array of strings!");
			setToolTipText("Fix that!");
		}
		return this;  
	}  
}
