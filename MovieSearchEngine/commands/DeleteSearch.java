package commands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class DeleteSearch implements ActionListener {

	private JTextField searchArea;
	
	public DeleteSearch(JTextField searchArea) {
		this.searchArea = searchArea;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		searchArea.setText("");

	}

}
