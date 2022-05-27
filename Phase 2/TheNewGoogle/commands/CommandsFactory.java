package commands;

import java.awt.TextArea;

import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JTextField;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.FSDirectory;


public class CommandsFactory {

	public CommandsFactory() {}

	public ActionListener createCommand(String s,JButton[] buttons, int[] btns_pressed, JTextField searchArea, TextArea textArea, 
			StandardAnalyzer analyzer, FSDirectory directory, int[] typeMenu, HashMap<String,String> history,JMenu mnNewMenu, JButton search_button)
	{
		if(s.equals("Title")) {
			return new SearchByTitle(buttons, btns_pressed);
		}
		else if(s.equals("Description")) {
			return new SearchByDescription(buttons, btns_pressed);
		}
		else if(s.equals("Release Year")) {
			return new SearchByReleaseYear(buttons, btns_pressed);
		}
		/*else if(s.equals("Type")) {
			return new SearchByType(mnNewMenu_1);
		}*/
		else if(s.equals("Search")) {
			return new Search(buttons, btns_pressed, searchArea, textArea, analyzer, directory, typeMenu, history, mnNewMenu, search_button);
		}
		else if(s.equals("DeleteSearch")) {
			return new DeleteSearch(searchArea);
		}
		else
			return null;
	}
}
