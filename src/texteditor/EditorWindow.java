package texteditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.naming.spi.DirectoryManager;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;
import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

public class EditorWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String fileName = null;
	
	private JMenuBar menuBar = null;
	private JMenu fileMenu = null;
	private JMenuItem newMenuItem = null;
	private JMenuItem openMenuItem = null;
	private JMenuItem saveMenuItem = null;
	private JMenuItem saveAsMenuItem = null;
	
	private JEditorPane editingBox = null;
	
	public EditorWindow() {
		this.setTitle("Untitled - SimpleTextEditor");
		this.setSize(400, 300);
		this.setLayout(new BorderLayout());
		
		//set up the menu bar
		menuBar = new JMenuBar();		
		fileMenu = new JMenu();
		fileMenu.setText("File");
		newMenuItem = new JMenuItem("New");
		fileMenu.add(newMenuItem);
		openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);
		saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);
		saveAsMenuItem = new JMenuItem("Save As");
		fileMenu.add(saveAsMenuItem);
		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);
		
		//set up the editing text box
		editingBox = new JEditorPane();
		this.add(editingBox, BorderLayout.CENTER);
		
		//add listeners
		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				editingBox.setText("");
				fileName = null;
				updateState();
			}
		});
		
		openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fileName = fileChooser.getSelectedFile().getAbsolutePath();
					
					try {
						FileReader reader = new FileReader(fileName);
						Scanner scanner = new Scanner(reader);
						
						String text = "";
						while(scanner.hasNextLine()) {
							text += scanner.nextLine() + "\n";
						}
						
						editingBox.setText(text);
						
						updateState();
						
						scanner.close();
						reader.close();
					} catch (FileNotFoundException e1) {
						fileName = null;
					} catch (IOException e1) {
						fileName = null;
					}
				}
			}
		});
		
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileName != null) {
					try {
						FileWriter writer = new FileWriter(fileName);
						writer.write(editingBox.getText());
						writer.flush();
						writer.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}		
		});
		
		saveAsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
				if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					fileName = fileChooser.getSelectedFile().getAbsolutePath();
					
					try {
						FileWriter writer = new FileWriter(fileName);
						writer.write(editingBox.getText());
						writer.flush();
						writer.close();
						
						updateState();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}			
		});
		
//		editingBox.addCaretListener(new CaretListener() {
//			@Override
//			public void caretUpdate(CaretEvent arg0) {
//				
//				
//			}
//		});
		
		updateState();		
	}
	
	private void updateState() {
		String title = "Untitled - SimpleTextEditor";
		if (fileName != null) {
			String relativeFileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
			title = relativeFileName + " - SimpleTextEditor";
		}
		
		this.setTitle(title);
		
		saveMenuItem.setEnabled(fileName != null);
	}
}
