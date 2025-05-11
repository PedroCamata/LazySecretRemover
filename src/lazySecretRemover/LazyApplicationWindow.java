package lazySecretRemover;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.Box;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.JSeparator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LazyApplicationWindow {

	private JFrame frame;
	
	private LazySecretRemoverService lazyService;
	
	private JButton secretsButton;
	private JButton filesButton;
	private JButton btnCloneRepo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					LazyApplicationWindow window = new LazyApplicationWindow();
					window.frame.setVisible(true);
					window.frame.setTitle("Lazy Secret Remover");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LazyApplicationWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.lazyService = new LazySecretRemoverService();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 620, 550);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		var textField = new JTextField();
		textField.setBounds(12, 12, 600, 40);
		frame.getContentPane().add(textField);
		textField.setToolTipText("Add your git repo here");
		textField.setColumns(10);
		
		JTextArea secretsTextArea = new JTextArea();
		secretsTextArea.setBounds(12, 86, 600, 168);
		frame.getContentPane().add(secretsTextArea);
		
		JScrollPane secretsTextAreaScroll = new JScrollPane (secretsTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		secretsTextAreaScroll.setBounds(12, 86, 600, 168);
		frame.getContentPane().add(secretsTextAreaScroll);
		
		secretsButton = new JButton("Remove Text Secrets");
		secretsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleButtons(false);
				lazyService.removeSecrets(secretsTextArea.getText());
				toggleButtons(true);
			}
		});
		secretsButton.setBounds(12, 251, 596, 25);
		secretsButton.setEnabled(false);
		frame.getContentPane().add(secretsButton);
		
		JTextArea filesTextArea = new JTextArea();
		filesTextArea.setBounds(12, 295, 600, 168);
		frame.getContentPane().add(filesTextArea);
		
		JScrollPane filesTextAreaScroll = new JScrollPane (filesTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		filesTextAreaScroll.setBounds(12, 295, 600, 168);
		frame.getContentPane().add(filesTextAreaScroll);
		
		filesButton = new JButton("Remove Files from history");
		filesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleButtons(false);
				lazyService.removeFiles(filesTextArea.getText());
				toggleButtons(true);
			}
		});
		filesButton.setBounds(12, 462, 596, 25);
		filesButton.setEnabled(false);
		frame.getContentPane().add(filesButton);
		
		btnCloneRepo = new JButton("Clone Repo (backup will be created)");
		btnCloneRepo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleButtons(false);
				lazyService.cloneRepo(textField.getText());
				toggleButtons(true);
			}
		});
		btnCloneRepo.setBounds(12, 52, 596, 25);
		frame.getContentPane().add(btnCloneRepo);
		
		JLabel lblSignature = new JLabel("Created by Pedro Camata Andreon");
		lblSignature.setBounds(12, 494, 259, 15);
		frame.getContentPane().add(lblSignature);
	}
	
	private void toggleButtons(boolean toggle) {
		secretsButton.setEnabled(toggle);
		filesButton.setEnabled(toggle);
		btnCloneRepo.setEnabled(toggle);
		
		if(toggle) {
			JOptionPane.showMessageDialog(null, "Done!");
		}
	}
}
