package com.epwenker.viewMaster.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.epwenker.viewMaster.model.ViewMasterModel;

public class ViewMasterGUI extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private static ViewMasterModel model = new ViewMasterModel();;
	
	private JTextField urlField;
	private JLabel urlLabel;
	private JButton saveButton;
	
	private static final String APP_TITLE = "ViewMaster";
	
	
	public ViewMasterGUI() {
		super();
		
		setSize(300, 60);
		setLocation(50, 50);
		setResizable(false);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		urlLabel = new JLabel("URL:");
		urlField = new JTextField();
		urlField.setPreferredSize(new Dimension(100, 20));
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		
		/*JPanel urlPanel = new JPanel();
		urlPanel.setPreferredSize(new Dimension(300, 50));
		urlLabel = new JLabel("URL:");
		urlField = new JTextField();
		urlField.setPreferredSize(new Dimension(100, 20));
		urlPanel.add(urlLabel);
		urlPanel.add(urlField);*/
		
		
		
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setPreferredSize(new Dimension(300, 100));
		panel.add(urlLabel);
		panel.add(urlField);
		panel.add(saveButton);
					
		Container c = getContentPane();
		c.add(panel);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new ViewMasterGUI();
	}


	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == saveButton) {
			try {
				model.saveToFile(getFileName(), urlField.getText());
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, "Unable to save requirements file.");
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		}
	}
	
	private String getFileName() {
		JFileChooser fc = new JFileChooser("./");
		fc.setDialogTitle("ViewMaster Save");
		fc.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
		fc.setApproveButtonText("Select");
		int returnVal = fc.showOpenDialog(this);
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File file = fc.getSelectedFile();
		return file.getAbsolutePath();
	}

}
