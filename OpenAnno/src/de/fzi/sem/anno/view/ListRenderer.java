package de.fzi.sem.anno.view;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ToolTipManager;
import javax.swing.table.TableCellRenderer;

import com.hp.hpl.jena.rdf.model.Literal;

import de.fzi.sem.anno.model.Iri;

public class ListRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -155483848258682809L;

	public ListRenderer() {
		// TODO Auto-generated constructor stub
	}

	public ListRenderer(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ListRenderer(Icon arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ListRenderer(String arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ListRenderer(Icon arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ListRenderer(String arg0, Icon arg1, int arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus,  
            int row, int column) {
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().registerComponent(this);
		if(value.getClass() == String.class) {
			setText(value.toString());			
		} else if(value.getClass() == ArrayList.class) {
			@SuppressWarnings("unchecked")
			List<Object> values = (List<Object>) value;
			int id = 0;
			StringBuilder sb = new StringBuilder();
			sb.append("<html>");

			for(Object v: values) {
				int limit = 15;
				if(v instanceof Literal) {
					id = values.indexOf(v);
				}
				sb.append(v.toString());
				sb.append("<br>");
				if(values.indexOf(v) > limit) {
					sb.append("...");
					sb.append(values.size() - limit);
					sb.append(" more");
					break;
				}
			}
			sb.append("</html>");
			if(values.size() > 0) {
				Object v = values.get(id);
				if(v.getClass().equals(Iri.class)) {
					Iri iri = (Iri) v;
					setText(iri.getCurie());
				} else if (v instanceof Literal){
					Literal literal = (Literal) v;					
					setText(literal.getLexicalForm());
				} else {
					setText(v.toString());
				}
			}
			setToolTipText(sb.toString());

		} else {
			setText(value.getClass().toString() );
			setToolTipText(value.getClass().toString() );
		}
		
		return this;
	}

}
