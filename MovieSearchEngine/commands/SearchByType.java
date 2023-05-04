package commands;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;

public class SearchByType implements ActionListener {
	private JMenu mnNewMenu_1;
	
	
	public SearchByType(JMenu mnNewMenu_1) {
		this.mnNewMenu_1 = mnNewMenu_1;
		
	}
	public void actionPerformed(ActionEvent arg0) {
		
		if(mnNewMenu_1.getBackground() == Color.LIGHT_GRAY) {
			mnNewMenu_1.setBackground(Color.CYAN);
			mnNewMenu_1.setText("Kides");
			
		}
		else if(mnNewMenu_1.getBackground() == Color.CYAN) {
			mnNewMenu_1.setBackground(Color.LIGHT_GRAY);
			
		}
	}
}
