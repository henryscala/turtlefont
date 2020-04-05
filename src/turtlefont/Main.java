package turtlefont;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;

import turtlefont.grammar.GrammarElement;
import turtlefont.grammar.GrammarParse;
import turtlefont.grammar.SExpr;
import turtlefont.grammar.Utils;



public class Main extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final JFileChooser fileChooser = new JFileChooser();
	PaintPanel paintPanel = new PaintPanel();
	
	public Main() throws Exception{
		
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		JMenuItem openFontFile = new JMenuItem("Open"); 
		fileMenu.add(openFontFile); 
		menuBar.add(fileMenu); 
		openFontFile.addActionListener(a->{
			//In response to a button click:
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					String grammar = Utils.readFileContent(file);
					ArrayList<SExpr> sexprList = SExpr.parse(grammar);
					GrammarParse parse = new GrammarParse();
					HashMap<String, GrammarElement> grammarElementMap = parse.parse(sexprList);
					
					String names[] = file.getName().split("\\.");
					
			
					GrammarElement element = grammarElementMap.get( names[0] );
					paintPanel.setGrammarElement(element);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} 
		});
		
		this.getContentPane().add(paintPanel, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setSize(800, 600);
		this.setVisible(true);
	}
	public static void main(String[] args) throws Exception {
		
		
		
		
		
		 new Main(); 
		
		
	}
}

