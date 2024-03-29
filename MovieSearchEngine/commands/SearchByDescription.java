package commands;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;


public class SearchByDescription implements ActionListener{
	private JButton[] buttons;
	private int[] buttons_pressed;
	
	public SearchByDescription(JButton[] buttons, int[] buttons_pressed) {
		this.buttons = buttons;
		this.buttons_pressed = buttons_pressed;
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if(buttons[2].getBackground() == Color.LIGHT_GRAY) {
			buttons[2].setBackground(Color.CYAN);
			buttons_pressed[2] = 1;
		}
		else if(buttons[2].getBackground() == Color.CYAN) {
			buttons[2].setBackground(Color.LIGHT_GRAY);
			buttons_pressed[2] = 0;
		}
	}
}
