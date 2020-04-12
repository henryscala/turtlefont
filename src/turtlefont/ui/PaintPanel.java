package turtlefont.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.Timer;

import turtlefont.grammar.GrammarElement;
import turtlefont.grammar.GrammarElementList;
import turtlefont.grammar.Point;
import turtlefont.grammar.PolyLine;

public class PaintPanel extends JPanel implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final double ZOOM_MODE_STEP_LEN = 0.06;
	static final double NORMAL_MODE_STEP_LEN = 3;
	static final int TIMER_PERIOD = 50;//milli seconds
	static final int WIDTH = 600; 
	static final int HEIGHT = 600; 
	static final int LINE_WIDTH = 3; 
	static final int LEFT_MARGIN = 40;
	static final int TOP_MARGIN = 40; 
	static final int NUM_CHAR_PER_ROW = 40;
	static final int NUM_CHAR_PER_COL = 16;
	static final int CHAR_SIZE = 40;
	static final int SEPARATOR_SIZE = 4; 
	boolean zoomMode = true; 
	public HashMap<String, GrammarElement> grammarElementMap = new HashMap<String, GrammarElement>(); 
	public GrammarElementList grammarElementList = new GrammarElementList();
	Timer timer = new Timer(TIMER_PERIOD,this);//100 milli seconds 
	double totalDistance; 
	double currDistance;
	public PaintPanel() throws Exception{
		timer.start();
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setSize(new Dimension(WIDTH,HEIGHT));
		this.setBackground(Color.white);
	}
	
	public void resetDistance() {
		totalDistance = grammarElementList.len(); 
		currDistance = 0; 
	}
	public void addGrammarElement(GrammarElement element) {
		grammarElementList.elementList.add(element);
		
		arrangeEachChar();
		resetDistance();
	}

	public void arrangeEachChar() {
		int row = 0; 
		int col = 0; 
		for (int i=0;i<grammarElementList.elementList.size();i++) {
			GrammarElement element = grammarElementList.elementList.get(i);
			double x = LEFT_MARGIN + col * CHAR_SIZE + SEPARATOR_SIZE*col;
			double y = TOP_MARGIN + row * CHAR_SIZE + SEPARATOR_SIZE*row;
			GrammarElement.relocate(element, x, y, CHAR_SIZE, CHAR_SIZE); 
			col++;
			if (col >= NUM_CHAR_PER_ROW) {
				col = 0;
				row++;
			}
		}
	}
	

	//timer fires
	@Override
	public void actionPerformed(ActionEvent e){
		//if (zoomMode) {
		//	currDistance += ZOOM_MODE_STEP_LEN; 
		//} else {
			currDistance += NORMAL_MODE_STEP_LEN;
		//}
		validate();
		repaint();
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(LINE_WIDTH));
		drawCoordinate(g2); 
		if(grammarElementList.elementList.size() > 0) {
			//if (zoomMode) {
			//	drawGrammarElement(g2,grammarElementList,LEFT_MARGIN,TOP_MARGIN,WIDTH-2*LEFT_MARGIN,HEIGHT-2*TOP_MARGIN,currDistance);
		
			//} else {
				drawGrammarElement(g2,grammarElementList,0,0,1,1,currDistance);
			//}
		}
	}
	private void drawCoordinate(Graphics2D g) {
		final int COORDINATE_INTERVAL = 50;
		g.drawLine(0, 0, WIDTH, 0);
		for (int i=0;i<WIDTH;i+=COORDINATE_INTERVAL) {
			g.drawLine(i, 0, i, 5*LINE_WIDTH);
			g.drawString(String.valueOf(i), i, 6*LINE_WIDTH);
		}
		g.drawLine(0, 0, 0, HEIGHT);
		for (int i=0;i<HEIGHT;i+=COORDINATE_INTERVAL) {
			if (i == 0) {
				continue;
			}
			g.drawLine(0, i, 5*LINE_WIDTH, i);
			g.drawString(String.valueOf(i), LINE_WIDTH, i);
		}
	}
	private void drawGrammarElement(Graphics2D g,GrammarElement element, int x, int y, int width, int height, double distance) {
		double currDistance = 0; 
		if (element instanceof Point) {
			Point p = (Point)element;
			g.drawArc((int)(p.x*width)+x, (int)(p.y*height)+y, LINE_WIDTH*2, LINE_WIDTH*2, 0, 360);
			return; 
		}
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