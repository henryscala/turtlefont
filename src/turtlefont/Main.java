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

class PaintPanel extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final double STEP_LEN = 0.06;
	static final int TIMER_PERIOD = 50;//milli seconds
	GrammarElement element;
	Timer timer = new Timer(TIMER_PERIOD,this);//100 milli seconds 
	double totalDistance; 
	double currDistance;
	public PaintPanel() throws Exception{
		timer.start();
		this.setPreferredSize(new Dimension(800,600));
		this.setBackground(Color.white);
		

	}
	public void setGrammarElement(GrammarElement element) {
		this.element = element; 
		totalDistance = element.len(); 
		currDistance = 0; 
	}
	
	//timer fires
	@Override
	public void actionPerformed(ActionEvent e){
		currDistance += STEP_LEN; 
		validate();
		repaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		if(element != null) {
			drawGrammarElement(g2,element,100,100,300,300,currDistance);
		}
	}
	private void drawGrammarElement(Graphics2D g,GrammarElement element, int x, int y, int width, int height, double distance) {
		double currDistance = 0; 
		if (element instanceof GrammarElementList) {
			GrammarElementList list = (GrammarElementList)element;
			for(int i=0;i<list.elementList.size();i++) {
				GrammarElement child = list.elementList.get(i);
				double lineDistance = child.len();
				if (currDistance + lineDistance <= distance) {
					drawGrammarElement(g,child,x,y,width,height,distance);
					currDistance += lineDistance;
				} else {
					drawGrammarElement(g,child,x,y,width,height, distance - currDistance);
					return;
				}
				
			}
			return; 
		}
		if (element instanceof PolyLine) {
			PolyLine pl =  (PolyLine)element; 
			Point point = pl.pointList.get(0); 
	
			for (int i=1;i<pl.pointList.size();i++) {
				Point pointNext = pl.pointList.get(i);
				double lineDistance = point.distance(pointNext);
				double x1 = (point.x*width)+x;
				double y1 = (point.y*height)+y;
				double x2 = (pointNext.x*width)+x;
				double y2 = (pointNext.y*height)+y;
				double percent;
				if (currDistance + lineDistance <= distance) {	
					g.drawLine((int)x1,(int)y1 ,(int)x2 ,(int)y2 );
					currDistance += lineDistance;
				} else {
					percent = 1.0 - (currDistance+lineDistance-distance) / lineDistance;
					assert percent >= 0;
					assert percent <= 1; 	
					double xmiddle=x1+(x2-x1)*percent;
					double ymiddle=y1+(y2-y1)*percent;
					g.drawLine((int)x1,(int)y1 ,(int)xmiddle ,(int)ymiddle );
					currDistance += lineDistance*percent; 
					return; 
				}
				
				
				point = pointNext; 
			}
			return; 
		}
		assert(false); 
	}
}

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

class Utils{
	public static String readFileContent(File file) throws Exception {

		FileInputStream fileInputStream;

		fileInputStream = new FileInputStream(file);
		byte[] crunchifyValue = new byte[(int) file.length()];
		fileInputStream.read(crunchifyValue);
		fileInputStream.close();

		String fileContent = new String(crunchifyValue, "UTF-8");
		return fileContent;

	}
}
