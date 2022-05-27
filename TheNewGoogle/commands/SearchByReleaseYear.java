package commands;

import java.awt.Color;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import javax.swing.JButton;

public class SearchByReleaseYear implements ActionListener {
	private JButton[] buttons;
	private int[] buttons_pressed;
	
	public SearchByReleaseYear(JButton[] buttons, int[] buttons_pressed) {
		this.buttons = buttons;
		this.buttons_pressed = buttons_pressed;
	}
	public void actionPerformed(ActionEvent arg0) {
		
		if(buttons[1].getBackground() == Color.LIGHT_GRAY) {
			buttons[1].setBackground(Color.CYAN);
			buttons_pressed[1] = 1;
		}
		else if(buttons[1].getBackground() == Color.CYAN) {
			buttons[1].setBackground(Color.LIGHT_GRAY);
			buttons_pressed[1] = 0;
		}
	}
}
