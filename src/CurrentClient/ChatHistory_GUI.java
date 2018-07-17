package CurrentClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.JTextComponent;

/**
 * 
 * @author rxm448
 *
 */

public class ChatHistory_GUI{

	JFrame gui;				// frame to act as the gui
	JTable table;			// Table to display the chat history 

	private String[] columnNames
	= {"Country", "Capital", "Population in Millions", "Democracy"};

	private Object[][] data = {
			{"USA", "Washington DC", 280, true},
			{"Canada", "Ottawa", 32, true},
			{"United Kingdom", "London", 60, true},
			{"Germany", "Berlin", 83, true},
			{"France", "Paris", 60, true},
			{"Norway", "Oslo", 4.5, true},
			{"India", "New Delhi", 1046, true}
	};


	private String[] columns = new String[]{"sender", "time-stamp", "content"}; // string array of column names 



	/**
	 * constructor for a ChatHistory_GUI, creates the table using the given string array and the preset column headings, then creates the view  
	 * @param client, sets the client 
	 * @param messages, the array of messages to display as the public chat history 
	 */
	public ChatHistory_GUI(Client client, String[] messages) {

		String[][] data = buildData(messages);
		DefaultTableModel model = new DefaultTableModel(data, columns);
		table = new JTable(model);		

		buildview();
		
		gui.addWindowListener(new java.awt.event.WindowAdapter(){	// Listener that disposes the gui if the user simply closes the window 
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				gui.dispose();
			}
			
		});
	}


	/**
	 * this method converts the 1 d array of messages into a 2d one for the table, each message has three parts, sender time-stamp and content, 
	 * so every three elements of 'messages' is formed into an array in a new 2d array 
	 * @param messages, the array of messages 
	 * @return the two dimentional array needed for the table 
	 */
	private String[][] buildData(String[] messages) {
		String[][] data = new String[messages.length/3][3];

		for (int i = 0; i < data.length; i++) {
			int position = i * 3;
			data[i][0] = messages[position];
			data[i][1] = messages[position + 1];
			data[i][2] = messages[position + 2];
		}

		return data;
	}


	public ChatHistory_GUI() {
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		table = new JTable(model);		
		buildview();
	}


	/**
	 * method for adding all the components to the frame 'gui'
	 */
	private void buildview() {

		gui = new JFrame("Chat Histories"); // creation of main frame and panel 
		JPanel panel = new JPanel();
		gui.setSize(700, 450);

		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel()); // creation of row sorter for filtering table
		table.setRowSorter(rowSorter);

		JScrollPane scrollPane = new JScrollPane(table); // creation of scroll pane to hold table
		table.setFillsViewportHeight(true);
		scrollPane.setBounds(30, 50, 600, 250);

		JLabel senderLabel = new JLabel("Filter Sender:");	// creation of filter label for sender 
		senderLabel.setBounds(100, 350, 100, 30);
		
		JLabel contentLabel = new JLabel("Filter content:"); 	// creation of filter label for content
		contentLabel.setBounds(295, 350, 100, 30);

		JTextField filterSender = new JTextField(); 	// creation of filter text box  for sender
		filterSender.setBounds(170, 350, 100, 30);
		
		JTextField filterContent = new JTextField();	// creation of filter text box  for content
		filterContent.setBounds(370, 350, 100, 30);

		
				
		DocumentListener filterListener = new DocumentListener() {	// creation of document listener for filter text boxes 

			@Override
			public void insertUpdate(DocumentEvent e) {
				String SenderText = filterSender.getText();
				String contentText = filterContent.getText();

				if (SenderText.trim().length() == 0 && contentText.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					List<RowFilter<Object,Object>> filters =  new ArrayList<RowFilter<Object,Object>>(2);
					filters.add(RowFilter.regexFilter("(?i)" + SenderText, 0));
					filters.add(RowFilter.regexFilter("(?i)" + contentText, 2));
					rowSorter.setRowFilter(RowFilter.andFilter(filters));
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String SenderText = filterSender.getText();
				String contentText = filterContent.getText();

				if (SenderText.trim().length() == 0 && contentText.trim().length() == 0) { 
					rowSorter.setRowFilter(null);
				} else {
					List<RowFilter<Object,Object>> filters =  new ArrayList<RowFilter<Object,Object>>(2);
					filters.add(RowFilter.regexFilter("(?i)" + SenderText, 0));
					filters.add(RowFilter.regexFilter("(?i)" + contentText, 2));
					rowSorter.setRowFilter(RowFilter.andFilter(filters));
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				throw new UnsupportedOperationException("not implemented"); 
			}

		};
		
		filterSender.getDocument().addDocumentListener(filterListener);	// adding filterListener as a listener for the filter text boxes 
		filterContent.getDocument().addDocumentListener(filterListener);

		JLabel history = new JLabel("Public Chat History:"); // creation of label for tabel title 
		history.setBounds(280, -20, 200, 100);

		panel.add(history); 	// Addition of all components to the panel 
		panel.add(scrollPane); 
		panel.add(filterSender);
		panel.add(filterContent);
		panel.add(senderLabel);
		panel.add(contentLabel);


		panel.setLayout(null);	// configuration of main frame 
		gui.setResizable(false);
		gui.add(panel, BorderLayout.CENTER);
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);


	}




	public static void main(String[] args) throws Exception {
		javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());

		new ChatHistory_GUI();

	}
}