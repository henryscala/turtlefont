package turtlefont.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

import turtlefont.Utils;
import turtlefont.geometry.LineCrossPointSolver;
import turtlefont.geometry.LineParametricEquation;
import turtlefont.geometry.Vector2;
import turtlefont.grammar.GrammarElement;
import turtlefont.grammar.GrammarParse;
import turtlefont.grammar.Point;
import turtlefont.grammar.PolyLine;
import turtlefont.grammar.SExpr;



public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final JFileChooser fileChooser = new JFileChooser();
	PaintPanel paintPanel = new PaintPanel();
	EditPanel editPanel = new EditPanel();
	JTabbedPane tabbedPane = new JTabbedPane();
	
	ActionListener zoomModeListener = new ActionListener() {  
        public void actionPerformed(ActionEvent event) {  
            AbstractButton aButton = (AbstractButton) event.getSource();  
            boolean selected = aButton.getModel().isSelected();  
            paintPanel.zoomMode = selected; 
        }  
    };  
    
	ActionListener drawPointListener = (s) -> {
		String line = JOptionPane.showInputDialog("input point specified by x,y. E.g. 100,100 ");
		if (line == null || line.equals("")) {
			JOptionPane.showMessageDialog(this, "you haven't specify a point");
			return;
		}
		String[] words = line.split(",",2);
		if (words == null || words.length != 2) {
			JOptionPane.showMessageDialog(this, "you haven't specify a correct point");
			return;
		}
		double x = Double.valueOf(words[0].trim());
		double y = Double.valueOf(words[1].trim());
		Point p = new Point(x,y);
		paintPanel.addGrammarElement(p);
	};
	
	ActionListener drawLineListener = (s) -> {
		String line = JOptionPane.showInputDialog("input line specified by x1,y1,x2,y2. E.g. 100,100,200,200 ");
		if (line == null || line.equals("")) {
			JOptionPane.showMessageDialog(this, "you haven't specify 2 points");
			return;
		}
		String[] words = line.split(",",4);
		if (words == null || words.length != 4) {
			JOptionPane.showMessageDialog(this, "you haven't specify correct 2 points");
			return;
		}
		double x1 = Double.valueOf(words[0].trim());
		double y1 = Double.valueOf(words[1].trim());
		double x2 = Double.valueOf(words[2].trim());
		double y2 = Double.valueOf(words[3].trim());
		Point p1 = new Point(x1,y1);
		Point p2 = new Point(x2,y2);
		PolyLine pl = new PolyLine();
		pl.pointList.add(p1);
		pl.pointList.add(p2);
		paintPanel.addGrammarElement(pl);;
		
	};
	
	private PolyLine inputLine() {
		String line = JOptionPane.showInputDialog("input line specified by x1,y1,x2,y2. E.g. 100,100,200,200 ");
		if (line == null || line.equals("")) {
			JOptionPane.showMessageDialog(this, "you haven't specify 2 points");
			return null;
		}
		String[] words = line.split(",",4);
		if (words == null || words.length != 4) {
			JOptionPane.showMessageDialog(this, "you haven't specify correct 2 points");
			return null;
		}
		double x1 = Double.valueOf(words[0].trim());
		double y1 = Double.valueOf(words[1].trim());
		double x2 = Double.valueOf(words[2].trim());
		double y2 = Double.valueOf(words[3].trim());
		Point p1 = new Point(x1,y1);
		Point p2 = new Point(x2,y2);
		PolyLine pl = new PolyLine();
		pl.pointList.add(p1);
		pl.pointList.add(p2);
		paintPanel.addGrammarElement(pl);
		return pl; 
	}
	
	ActionListener drawCrossPointOfTwoLinesListener  = (s) -> {
		PolyLine pl1 = inputLine(); 
		PolyLine pl2 = inputLine();
		Vector2 p1 = new Vector2(pl1.pointList.get(0).x,pl1.pointList.get(0).y);
		Vector2 p2 = new Vector2(pl1.pointList.get(1).x,pl1.pointList.get(1).y);
		LineParametricEquation line1 = new LineParametricEquation(p1,p2);
		Vector2 p3 = new Vector2(pl2.pointList.get(0).x,pl2.pointList.get(0).y);
		Vector2 p4 = new Vector2(pl2.pointList.get(1).x,pl2.pointList.get(1).y);
		LineParametricEquation line2 = new LineParametricEquation(p3,p4);
		LineCrossPointSolver solver = new LineCrossPointSolver();
		solver.line1 = line1; 
		solver.line2 = line2;
//		System.out.println("line1\n " + pl1);
//		System.out.println("line2\n " + pl2);
		Vector2 crossPoint = solver.calculateCrossPoint();
		
		Point point = new Point(crossPoint.getX(),crossPoint.getY());
//		System.out.println("cross\n " + point);
		paintPanel.addGrammarElement(point);
		JOptionPane.showMessageDialog(this, "cross point is " + crossPoint);
	};
	
	ActionListener clearCanvasListener = (s)->{
		paintPanel.grammarElementList.elementList.clear();
	};
	
	ActionListener playFileListener = (s)->{
		
		ArrayList<SExpr> sexprList;
		try {
			sexprList = SExpr.parse(editPanel.textArea.getText());
		} catch (Exception e) {
			e.printStackTrace();
			return; 
		}
		GrammarParse parse = new GrammarParse();
		HashMap<String, GrammarElement> grammarElementMap;
		
		try {
			grammarElementMap = parse.parse(sexprList);
		} catch (Exception e) {
			e.printStackTrace();
			return; 
		}
		GrammarElement maxLenElement = null; 
		double maxLen = 0; 
		for(GrammarElement element:grammarElementMap.values()) {
			if(element.len()>maxLen) {
				maxLen = element.len();
				maxLenElement = element; 
			}
		}
//		String charToPlay = JOptionPane.showInputDialog("input the char to play");
//		if (charToPlay == null || charToPlay.equals("")) {
//			JOptionPane.showMessageDialog(this, "you haven't specify a char to play");
//			return;
//		}
//		
//		GrammarElement element = grammarElementMap.get( charToPlay );
		if(maxLenElement == null) {
			JOptionPane.showMessageDialog(this, "does not find a char to play?");
			return; 
		}
		tabbedPane.setSelectedComponent(paintPanel);
		paintPanel.addGrammarElement(maxLenElement);
	};
	ActionListener fileSaveListener = (s)->{
		//In response to a button click:
		int returnVal = fileChooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				Files.write(Paths.get(file.getPath()), editPanel.textArea.getText().getBytes("UTF-8"), 
						StandardOpenOption.WRITE,
						StandardOpenOption.CREATE,
						StandardOpenOption.TRUNCATE_EXISTING);	
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	};
	ActionListener fileOpenListener = (s)->{
		//In response to a button click:
		int returnVal = fileChooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				String grammar = Utils.readFileContent(file);
				editPanel.textArea.setText(grammar);
				tabbedPane.setSelectedComponent(editPanel);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
	};
	public MainFrame() throws Exception{
		
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("File");
		JMenuItem openFontFile = new JMenuItem("Open"); 
		JMenuItem saveFontFile = new JMenuItem("Save"); 
		fileMenu.add(openFontFile); 
		fileMenu.add(saveFontFile); 
		
		JMenu drawMenu = new JMenu("Draw");
		JMenuItem playFontFile = new JMenuItem("Play"); 
		JMenuItem clearCanvas = new JMenuItem("ClearCanvas"); 
		
		JMenuItem drawPoint = new JMenuItem("DrawPoint");
		JMenuItem drawLine = new JMenuItem("DrawLine");
		JMenuItem drawCrossPointOfTwoLines = new JMenuItem("DrawCrossPointOfTwoLines");
		JCheckBoxMenuItem zoomMode = new JCheckBoxMenuItem("zoomMode",true);
		drawMenu.add(playFontFile);
		drawMenu.add(clearCanvas);
		drawMenu.add(zoomMode);
		drawMenu.add(drawPoint);
		drawMenu.add(drawLine);
		drawMenu.add(drawCrossPointOfTwoLines);
		
		menuBar.add(fileMenu); 
		menuBar.add(drawMenu);
		
		openFontFile.addActionListener(fileOpenListener);
		saveFontFile.addActionListener(fileSaveListener);
		playFontFile.addActionListener(playFileListener);
		clearCanvas.addActionListener(clearCanvasListener);
		drawPoint.addActionListener(drawPointListener);
		drawLine.addActionListener(drawLineListener);
		zoomMode.addActionListener(zoomModeListener);
		drawCrossPointOfTwoLines.addActionListener(drawCrossPointOfTwoLinesListener);
		
		
		tabbedPane.addTab("show", paintPanel);
		tabbedPane.addTab("edit", editPanel);
		
		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		this.setSize(800, 800);
		this.setVisible(true);
	}
	public static void main(String[] args) throws Exception {
		
		
		
		
		
		 new MainFrame(); 
		
		
	}
}

