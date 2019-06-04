package de.fzi.sem.anno.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

import de.fzi.sem.anno.controller.listener.InfoListener;
import de.fzi.sem.anno.controller.listener.OpenListener;
import de.fzi.sem.anno.controller.listener.RunListener;
import de.fzi.sem.anno.controller.listener.TableListener;
import de.fzi.sem.anno.model.Button;
import de.fzi.sem.anno.model.Select;
import de.fzi.sem.anno.model.singleton.FilePath;
import de.fzi.sem.anno.model.singleton.NameSpaces;
import de.fzi.sem.anno.model.singleton.Services;
import de.fzi.sem.interfaces.IView;

public class OpenAnnoView implements IView {
	
	// properties
	private List<Tab> tabs;
	private InputPanel iPanel;
	private TablePanel sPanel;
	private TablePanel rPanel;
	
	// swing elements
	private JFrame frame;	
	private JTabbedPane tabbedPane;
	
	// listener
	private ChangeListener tabListener;	

	public OpenAnnoView() {
		this("OpenAnno");
	}

	public OpenAnnoView(String title) {
		frame = new JFrame (title);
		tabbedPane = new JTabbedPane();
		tabs = new ArrayList<Tab>();
		iPanel = new InputPanel();
		sPanel = new TablePanel();
		rPanel = new TablePanel();		
	}
	
	public void build() throws Exception {
		// build panels	
		iPanel.build();
		
		sPanel.build();
		sPanel.setColumnWidth(0, 50);
		sPanel.setColumnWidth(3, 150);
		rPanel.addEditColumn(4);
		rPanel.build();
		rPanel.setColumnWidth(3, 100);
		rPanel.setColumnWidth(4, 55);
		
		// add tabs to tabbedPane
		addTab("Input", iPanel);		
		addTab("Search", sPanel);		
		addTab("Results", rPanel);
		tabbedPane.addChangeListener(tabListener);
		for(Tab tab: tabs) {
			tabbedPane.addTab( tab.getTitle(), tab.getComponent());
		}
		
		// create menu bar
		JMenuBar menuBar = new JMenuBar();				
		
		// file menu
		JMenu file = new JMenu("File");
		JMenuItem newTarget = new JMenuItem("New Target");
		newTarget.addActionListener(new RunListener("target", frame.getTitle()));
		JMenuItem newAgent = new JMenuItem("Edit Agents");
		FilePath path = FilePath.getInstance();
		newAgent.addActionListener(new OpenListener(path.get("agent")));
		JMenuItem editTarget = new JMenuItem("Edit Targets");
		editTarget.addActionListener(new OpenListener(path.get("in")));
        menuBar.add(file);
//        file.add(newTarget);
        file.add(newAgent);
        file.add(editTarget);
        
        // info menu
		JMenu info = new JMenu("Info");
		JMenuItem showNS = new JMenuItem("Show namespaces");
		showNS.addActionListener(new InfoListener(frame, NameSpaces.getInstance()));
		JMenuItem showServices = new JMenuItem("Show services");
		showServices.addActionListener(new InfoListener(frame, Services.getInstance()));
		JMenuItem showFP = new JMenuItem("Show file paths");
		showFP.addActionListener(new InfoListener(frame, FilePath.getInstance()));
        menuBar.add(info);
        info.add(showNS);
        info.add(showServices);
        info.add(showFP);
		
		// set up panel
//		JComboBox<?> comboBox = new JComboBox<Object>(services.getComboBoxData());
//		comboBox.setMaximumSize(comboBox.getPreferredSize());
//		comboBox.setSelectedItem(services.getComboBoxDefault());
//		comboBox.addActionListener(services.getComboListener());		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//		panel.add(new JLabel(services.getComboBoxLabel()));
//		panel.add(comboBox);
		panel.add(tabbedPane);
		
		// set up frame
		frame.add(menuBar, BorderLayout.NORTH);
		frame.add(panel, BorderLayout.CENTER);		
		frame.setLocation(300,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();		
		frame.setVisible(true);	
	}	
	
	public void setSearchTableData(List<Object[]> tableData) {
		Object[][] tableDataArray = new Object[tableData.size()][tableData.get(0).length];
		int i = 0;
		for(Object[] row: tableData) {
			tableDataArray[i] = row;
			i++;
		}		
		this.setSearchTableData(tableDataArray);		
	}
	
	public void setResultsTableData(List<Object[]> tableData) {
		Object[][] tableDataArray = new Object[tableData.size()][tableData.get(0).length];
		int i = 0;
		for(Object[] row: tableData) {
			tableDataArray[i] = row;
			i++;
		}		
		this.setResultsTableData(tableDataArray);		
	}
	
	public void setSearchTableData(Object[][] tableData) {
		sPanel.setTableData(tableData);
	}
	
	public void addSearchRow(Object[] row) {
		sPanel.addTableRow(row);
	}
	
	public void addResultRow(Object[] row) {
		rPanel.addTableRow(row);
	}
	
	public void setResultsTableData(Object[][] tableData) {
		rPanel.setTableData(tableData);
	}
	
	private void updateTableData(String tabTitle, int row, int col, Object value) {
		int tabIndex = tabbedPane.indexOfTab(tabTitle);
		TablePanel table = (TablePanel) tabbedPane.getComponentAt(tabIndex);
		table.updateTable(row, col, value);
	}
	
	public void updateSearchTableData(int row, int col, Object value) {
		this.updateTableData("Search", row, col, value);
	}
	
	public void updateResultsTableData(int row, int col, Object value) {
		this.updateTableData("Results", row, col, value);	
	}

	public List<Tab> getTabs() {
		return tabs;
	}
	
	public Tab getTab(String name) {
		int index = tabbedPane.indexOfTab(name);
		return tabs.get(index);
	}
	
	public Tab getTabAt(int index) {		
		return tabs.get(index);
	}
	
	public void setTabAt(int index, Tab tab) {
		this.tabs.set(index, tab);
	}
	
	public void addTab (Tab tab) {
		this.tabs.add(tab);
	}
	
	public int indexOfTab(String title) {
		for(Tab tab: tabs) {
			if(tab.getTitle().equals(title)) {
				return tabs.indexOf(tab);
			}
		}
		return -1;
	}

	private void addTab(String string, Component component) {
		this.tabs.add(new Tab(string, component));		
	}

	public ChangeListener getTabListener() {
		return tabListener;
	}

	public void setTabListener(ChangeListener tabListener) {
		this.tabListener = tabListener;
	}

	public void showResults() {
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Results"));
	}

	public void showSearch() {
		tabbedPane.setSelectedIndex(tabbedPane.indexOfTab("Search"));
	}

	public void addInputSelect(Select select) {
		iPanel.addSelect(select);		
	}

	public void setInputButton(Button button) {
		iPanel.setButton(button);	
	}

	public void setSearchLabel(String searchLabel) {
		sPanel.setTableLabel(searchLabel);		
	}

	public void setSearchColumnNames(String[] columnNames) {
		sPanel.setColumnNames(columnNames);		
	}

	public void setSearchButton(Button button) {
		sPanel.setButton(button);		
	}

	public void setResultsLabel(String resultsLabel) {
		rPanel.setTableLabel(resultsLabel);		
	}

	public void setResultsColumnNames(String[] columnNames) {
		rPanel.setColumnNames(columnNames);		
	}

	public void setResultsButton(Button button) {
		rPanel.setButton(button);		
	}

	public void setResultTableListener(TableListener tableListener) {
		rPanel.setTableListener(tableListener);		
	}
}
