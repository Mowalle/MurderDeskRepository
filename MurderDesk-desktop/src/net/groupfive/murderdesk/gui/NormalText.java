package net.groupfive.murderdesk.gui;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import net.groupfive.murderdesk.GUI;

@SuppressWarnings("serial")
public class NormalText extends JTextPane {
	
	StyleContext sc;
	Style s;
	
	public NormalText(){
		setOpaque(false);
		setMargin(new Insets(5,10,10,10));
		// style
	    this.sc = new StyleContext();
	    this.s = sc.addStyle("RegularText", null);
	    this.s.addAttribute(StyleConstants.Foreground, Color.white);
	    this.s.addAttribute(StyleConstants.FontSize, 11);
	    this.s.addAttribute(StyleConstants.FontFamily, GUI.ftMinecraftia.getFamily());
	    setParagraphAttributes(this.s, false);
	    DefaultCaret caret = (DefaultCaret)this.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
	
	public void append(String s) { // better implementation--uses
		int len = getDocument().getLength(); // same value as getText().length();
		setCaretPosition(len); // place caret at the end (with no selection)
		replaceSelection(s); // there is no selection, so inserts at caret
	}
	
	public void setSpacing(float f){
		this.s.addAttribute(StyleConstants.LineSpacing, f);
	    setParagraphAttributes(s, false);
	}
	
	public Style getStyle(){
		return s;
	}
	
	public void setStyle(Style s){
		this.s = s;
		setParagraphAttributes(s, false);
	}
	
	/*
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D)g;
	    RenderingHints rh = new RenderingHints(
	    		RenderingHints.KEY_TEXT_ANTIALIASING,
	            RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
	    g2.setRenderingHints(rh);
	}
	*/
}
