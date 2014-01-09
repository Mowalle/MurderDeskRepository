package net.groupfive.murderdesk.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultCaret;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import sun.swing.SwingUtilities2;
import net.groupfive.murderdesk.GUI;
import net.groupfive.murderdesk.Main;

@SuppressWarnings("serial")
public class NormalText extends JTextPane {
	
	StyleContext sc;
	Style s;
	
	public NormalText(){
		setOpaque(false);
		setMargin(new Insets(5,10,10,10));
		// style
	    sc = new StyleContext();
	    s = sc.addStyle("RegularText", null);
	    s.addAttribute(StyleConstants.Foreground, Color.white);
	    s.addAttribute(StyleConstants.FontSize, 11);
	    s.addAttribute(StyleConstants.FontFamily, GUI.ftRegular.getFamily());
	    setParagraphAttributes(s, false);
	    DefaultCaret caret = (DefaultCaret)this.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
	
	public void append(String s) { // better implementation--uses
		int len = getDocument().getLength(); // same value as getText().length();
		setCaretPosition(len); // place caret at the end (with no selection)
		replaceSelection(s); // there is no selection, so inserts at caret
	}
	
	public void setSpacing(float f){
		s.addAttribute(StyleConstants.LineSpacing, f);
	    setParagraphAttributes(s, false);
	}
}
