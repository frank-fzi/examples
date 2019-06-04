package de.fzi.sem.target.view;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.fzi.sem.interfaces.IView;

public class TargetView extends JFrame implements IView {
	private static final long serialVersionUID = 5549185088485829631L;

	public TargetView(String title) {
		super(title);
	}

	public void build() {
		
		// set up panel
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(new JLabel("Create new resource"));
		
		/*
		JLabel label = new JLabel(select.getComboBoxLabel());
		JComboBox<?> comboBox = new JComboBox<Object>(select.getComboBoxData());
		comboBox.setMaximumSize(maxSize);
		comboBox.setSelectedItem(select.getComboBoxDefault());
		comboBox.addActionListener(select.getComboListener());
		panel.add(label);
		panel.add(comboBox);
		*/
		
		// set up frame
		this.add(panel, BorderLayout.CENTER);		
		this.setLocation(300,100);
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();		
		this.setVisible(true);
	}
}
