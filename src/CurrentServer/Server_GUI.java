package CurrentServer;

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JList;

public class Server_GUI extends JFrame {
	private static final long serialVersionUID = -1780849261675807781L;
	private JPanel contentPane;
	private JTextField textField;
	private JButton closeButton;
	private JButton lauchButton;
	private JLabel lblPort;
	private JLabel IPaddress;
	private JScrollPane ChatLog;
	private JTextArea textArea;
	private JButton saveButton;
	private Server server = null;

	
	/**
	 * Create the frame.
	 */
	public Server_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 424, 440);
		setTitle("Server");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setResizable(false);
		setContentPane(contentPane);
		lblPort = new JLabel("Port :");
		lblPort.setBounds(5, 9, 60, 20);
		lblPort.setFont(new Font("Verdana", Font.PLAIN, 12));
		lblPort.setHorizontalAlignment(SwingConstants.CENTER);
		
		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setBounds(59, 6, 60, 24);
		textField.setFont(new Font("Verdana", Font.PLAIN, 12));
		textField.setText("8787");
		textField.setColumns(10);
		
		try {
			IPaddress = new JLabel(String.valueOf(InetAddress.getLocalHost()));
		} catch (UnknownHostException e2) {
			e2.printStackTrace();
		}
		
		IPaddress.setBounds(308, 5, 95, 26);
		
		closeButton = new JButton("CLOSE");
		closeButton.setBounds(215, 5, 92, 26);
		closeButton.setFont(new Font("Verdana", Font.PLAIN, 12));
		closeButton.setEnabled(false);
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				stopServer();
			}
		});
		
		lauchButton = new JButton("OPEN");
		lauchButton.setBounds(125, 5, 87, 26);
		lauchButton.setFont(new Font("Verdana", Font.PLAIN, 12));
		lauchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				startServer();
			}
		});
		
		ChatLog = new JScrollPane();
		ChatLog.setBounds(5, 34, 398, 372);
		
		saveButton = new JButton("SAVE LOG");
		saveButton.setBounds(308, 5, 95, 26);
		saveButton.setFont(new Font("Verdana", Font.PLAIN, 12));
		saveButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e){
				try{
					saveServerLog();
				}
				catch (IOException e1){
					e1.printStackTrace();
				}
			}
		});

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setCaretPosition(textArea.getText().length());
		textArea.addCaretListener(new CaretListener(){
			@Override
			public void caretUpdate(CaretEvent e){
				textArea.setSelectionStart(textArea.getText().length());
			}
		});

		ChatLog.setViewportView(textArea);
		contentPane.setLayout(null);
		contentPane.add(lblPort);
		contentPane.add(textField);
		contentPane.add(lauchButton);
		contentPane.add(closeButton);
		//contentPane.add(saveButton);
		contentPane.add(IPaddress);
		contentPane.add(ChatLog);
		

		this.addWindowListener(new java.awt.event.WindowAdapter(){ // Listener that executes the disconnect method if the user simply closes the window 
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					stopServer();
				} catch (Exception Y) {
					Y.printStackTrace();
				}
			}
			
		});

	}

	public void startServer(){
		if (server == null){
			// create a server thread
			server = new Server(Integer.parseInt(textField.getText()),textArea);
			server.start();
			// regServer = new RegisterThread(textArea);
			// regServer.start();
			lauchButton.setEnabled(false);
			closeButton.setEnabled(true);
			// System.out.println("im am here");
		}
	}
	
	public void stopServer(){
		if (server != null){
			server.shutdown(); //TODO
			server = null;
			lauchButton.setEnabled(true);
			closeButton.setEnabled(false);
			System.exit(0);
		}else{
			System.out.println("ERROR STOP SERVER");
		}
	}

	public void saveServerLog() throws IOException{
		String path = "D:\\log";
		String filename = "historical.log";
		File logData = new File(path, filename);
		if (!logData.exists()){
			logData.createNewFile();
		}
		FileWriter filewriter = new FileWriter(logData, true);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		filewriter.write(dateFormat.format(System.currentTimeMillis()) + "\r\n");
		filewriter.write(textArea.getText());
		filewriter.close();
	}
	
	public static void main(String[] args) {
		Server_GUI serverGui = new Server_GUI();
		serverGui.setVisible(true);
	}

}
