package turtlefont.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import turtlefont.grammar.GrammarElement;
import turtlefont.grammar.GrammarElementList;
import turtlefont.grammar.Point;
import turtlefont.grammar.PolyLine;

public class EditPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JTextArea textArea = new JTextArea(25, 80);
	private BoxLayout verticalBoxLayout = new BoxLayout(this,BoxLayout.Y_AXIS);
	public EditPanel() throws Exception{
		this.setLayout(verticalBoxLayout);
		this.setPreferredSize(new Dimension(800,600));
		this.setBackground(Color.white);
		this.add(textArea); 

	}

	
	
}