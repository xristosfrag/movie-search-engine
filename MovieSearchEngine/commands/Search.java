package commands;


import java.awt.Color;


import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;


public class Search implements ActionListener {
	private JButton[] buttons;
	private int[] buttons_pressed;
	private JTextField searchArea;
	private TextArea textArea;
	private StandardAnalyzer analyzer;
	private FSDirectory directory;
	private int[] typeMenu;
	private boolean type_enabled = false;
	private boolean release_year_enabled = false;
	private HashMap<String,String> history;
	private JMenu mnNewMenu;
	private JButton search_button;
	
	public Search(JButton[] buttons, int[] btns_pressed,JTextField searchArea, TextArea textArea, StandardAnalyzer analyzer, FSDirectory directory, int[] typeMenu, HashMap<String,String> history, JMenu mnNewMenu, JButton search_button) {
		this.buttons = buttons;
		this.buttons_pressed = btns_pressed;
		this.searchArea = searchArea;
		this.textArea = textArea;
		this.analyzer = analyzer;
		this.directory = directory;
		this.typeMenu = typeMenu;
		this.history = history;
		this.mnNewMenu = mnNewMenu;
		this.search_button = search_button;
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {	
		
		String text = textArea.getText();
		if(text.length() > 0) {
			this.textArea.setText("");
			text = textArea.getText();
		}	
		System.out.println("-----------------------------\n");
		try {
			DirectoryReader ireader = DirectoryReader.open(directory);
			
			//System.out.println("Reader "+ ireader.numDocs());
		    
		    IndexSearcher isearcher = new IndexSearcher(ireader);
			
		    ArrayList<String> array = getIndexes(buttons_pressed);		   
		    //printArrayList(array);
		    String text_query = searchArea.getText();
		    
		    Query query = null;
		    
		    if(array.size() == 0) {	//if no field selected
		    	
		    	if(searchArea.getText().equals("")) {
					textArea.setText("							RESULTS NOT FOUND					\n							NULL QUERY");
					return;
				}
		    	Query query_title = new QueryParser("Title", analyzer).parse(text_query);		    	
		    	Query query_desc = new QueryParser("Description", analyzer).parse(text_query);
		    	Query query_type = new QueryParser("listed_in", analyzer).parse(text_query);
		    	Query query_relyer = new QueryParser("Release Year", analyzer).parse(text_query);

		    	
		    	TopDocs top_title =  isearcher.search(query_title, 10);
		    	TopDocs top_desc =  isearcher.search(query_desc, 10);
		    	TopDocs top_type =  isearcher.search(query_type, 10);
		    	TopDocs top_relyer =  isearcher.search(query_relyer, 10);
		    	
		    	String result = "";
		    	if (top_title != null) {	
		    		for (int i=0; i < Math.min(5, top_title.scoreDocs.length); i++) {
		    			result += "Movie Title: "+ isearcher.doc(top_title.scoreDocs[i].doc).getField("Title").stringValue() +"\n"+
			        	"Description: "+ isearcher.doc(top_title.scoreDocs[i].doc).getField("Description").stringValue() + "\n\n";
			        }
		    	}
		    	if(top_desc != null){
		    		for (int i=0; i < Math.min(3, top_desc.scoreDocs.length); i++) {
		    			result += "Movie Title: "+ isearcher.doc(top_desc.scoreDocs[i].doc).getField("Title").stringValue() +"\n"+
			        	"Description: "+ isearcher.doc(top_desc.scoreDocs[i].doc).getField("Description").stringValue() + "\n\n";
			        }
		    	}
		    	if(top_type != null){
		    		for (int i=0; i < Math.min(3, top_type.scoreDocs.length); i++) {
		    			result += "Movie Title: "+ isearcher.doc(top_type.scoreDocs[i].doc).getField("Title").stringValue() +"\n"+
			        	"Description: "+ isearcher.doc(top_type.scoreDocs[i].doc).getField("Description").stringValue() + "\n\n";
			        }
		    	}
		    	if(top_relyer != null){
		    		for (int i=0; i < Math.min(2, top_relyer.scoreDocs.length); i++) {
		    			result += "Movie Title: "+ isearcher.doc(top_relyer.scoreDocs[i].doc).getField("Title").stringValue() +"\n"+
			        	"Description: "+ isearcher.doc(top_relyer.scoreDocs[i].doc).getField("Description").stringValue() + "\n\n";
			        }
		    	}
		    	if(!result.equals("")) {
		    		textArea.setText(result);	
		    		long tops_length = (top_title.totalHits.value + top_desc.totalHits.value + top_type.totalHits.value + top_relyer.totalHits.value);
		    	 	System.out.println("Total hits: " + tops_length);
		    	 	
			        for (int i=0; i < Math.min(5, top_title.scoreDocs.length); i++) {
			          System.out.println("Id: " + top_title.scoreDocs[i].doc + " score: "+ top_title.scoreDocs[i].score);
			        }
			        for (int i=0; i < Math.min(3, top_desc.scoreDocs.length); i++) {
				          System.out.println("Id: " + top_desc.scoreDocs[i].doc + " score: "+ top_desc.scoreDocs[i].score);
				        }
			        for (int i=0; i < Math.min(3, top_type.scoreDocs.length); i++) {
				          System.out.println("Id: " + top_type.scoreDocs[i].doc + " score: "+ top_type.scoreDocs[i].score);
				        }
			        for (int i=0; i < Math.min(2, top_relyer.scoreDocs.length); i++) {
				          System.out.println("Id: " + top_relyer.scoreDocs[i].doc + " score: "+ top_relyer.scoreDocs[i].score);
				        }
			        
			        String fields = "FIELDS: ";
				    for(String s:array) {
				    	fields = fields + s+" ";
				    }
				    fields += ".SEARCHED:  "+ text_query;
				    history.put(fields, text);
				    JMenuItem tmp_menu = new JMenuItem(fields);
				    mnNewMenu.add(tmp_menu);
				    
				    tmp_menu.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent ev) {
				    	clearSystem();
				    	
				    	//history.get
				    	
				    }
					});
					
		    	}else
		    		textArea.setText("							RESULTS NOT FOUND					");
		    	clearSystem();
		    	return;
		    }else {	//more than one field selected
		    	
		    	//one field selected
		    	if(array.size() == 1) { 
		    		if(array.get(0).equals("listed_in")) {		    			
		    			String type = getMovieType(typeMenu);
		    			System.out.println(type+")");
		    			query = new QueryParser(array.get(0), analyzer).parse(type);
		    		}
		    		else {
		    			if(searchArea.getText().equals("") && !type_enabled) {
		    				textArea.setText("							RESULTS NOT FOUND					\n							NULL QUERY");
		    				clearSystem();
		    				return;
		    			}
		    			
		    			query = new QueryParser(array.get(0), analyzer).parse(text_query);
		    		}
		    	}
		    	else if(array.size() > 1)
		    	{
		    		String result = "";
		    		Query queries[] = new Query[array.size()];
		    		
		    		
		    		if(text_query.equals("") && !type_enabled) {
		    			textArea.setText("							RESULTS NOT FOUND					\n							NULL QUERY");
		    			clearSystem();
		    			return;
		    		}
		    		
		    		if(type_enabled & !text_query.equals(""))		// Erwthmata me varos sto type
		    		{
		    			String type = getMovieType(typeMenu);
	    				System.out.println(type+")");
	    				
	    				
	    				TopDocs tops[] = new TopDocs[array.size() - 1];
	    				
	    				Query query1 = new QueryParser("listed_in", analyzer).parse(type);
	    		    	TopDocs top_type =  isearcher.search(query1, 8500);
	    		    	//tops[0] =  isearcher.search(query1, 10);
	    		    	
	    		    	//List<Set<Integer>> all_sets = new ArrayList<Set<Integer>>();;
	    		    	
	    		    	
	    		    	//fill the type_set. Contains Top Docs ordered by score
	    		    	Set<Integer> type_set = new HashSet<Integer>();
	    		    	for(int k=0; k < top_type.scoreDocs.length; k++) 
	    		    		type_set.add(top_type.scoreDocs[k].doc);
	    		    	System.out.println("Doc ids found in 'listed_in: "+type_set); 
	    		    	//all_sets.add(type_set);
	    		    	
	    		    	
	    		    	for(int i=0; i<tops.length; i++) 
		    			{
			    			
			    			if(array.get(i).equals("Title"))
			    			{
			    				queries[i] = new QueryParser("Title", analyzer).parse(text_query);
			    				
			    				tops[i] =  isearcher.search(queries[i], 8500);
			    				
			    				Set<Integer> set_tmp = new HashSet<Integer>();
			    				for(int k=0; k < tops[i].scoreDocs.length; k++) 
			    					set_tmp.add(tops[i].scoreDocs[k].doc);
			    				System.out.println("Doc ids found in 'Title: "+set_tmp); 
			    				//all_sets.add(set_tmp);
			    			}
			    			else if(array.get(i).equals("Release Year"))
			    			{
			    				queries[i] = new QueryParser("Release Year", analyzer).parse(text_query);
			    				
			    				tops[i] =  isearcher.search(queries[i], 8500);
			    				
			    				Set<Integer> set_tmp = new HashSet<Integer>();
			    				for(int k=0; k < tops[i].scoreDocs.length; k++) 
			    					set_tmp.add(tops[i].scoreDocs[k].doc);
			    				System.out.println("Doc ids found in 'Release Year: "+set_tmp);
			    				//all_sets.add(set_tmp);
			    			}
			    			else if(array.get(i).equals("Description"))
			    			{
			    				queries[i] = new QueryParser("Description", analyzer).parse(text_query);
			    				
			    				tops[i] =  isearcher.search(queries[i], 8500);
			    				//System.out.println("Doc ids found in 'Description: !!!");
			    				
			    				Set<Integer> set_tmp = new HashSet<Integer>();
			    				System.out.println("Doc ids found in 'Description: [");
			    				for(int k=0; k < tops[i].scoreDocs.length; k++) {
			    					set_tmp.add(tops[i].scoreDocs[k].doc);
			    					System.out.print(tops[i].scoreDocs[k].doc+ ", ");
			    				}
			    				System.out.println("]");
			    			
			    			}
		    			}

	    		    	
	    		    	//Find the intersection of all sets. 
	    		         // vres 8eseis koinwn kai scores kai valta sto hashmap.
	    		         
	    		         ArrayList<Integer> intersection = new ArrayList<Integer>(); 		// ola ta koina twn ana dyo topDocs pinakwn
	    		         for(int k=0; k<tops.length; k++)
	    		         {
	    		        	 int score = 0;
	    		        	 HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
	    		        	
	    		        	 TopDocs top_other = tops[k];
	    		        	 
	    		        	 if(k == 0)
	    		        	 {
	    		        		 TopDocs top_simantikhs = top_type;	//tops[k];
	    		        	 	
		    		        	 for(int i=0; i<top_other.scoreDocs.length; i++)
			    		         {
			    		        	 for(int j=0; j<top_simantikhs.scoreDocs.length; j++)
			    		        	 {
			    		        		 //System.out.println(top_other.scoreDocs[j].doc +"     "+ top_type.scoreDocs[j].doc);
			    		        		 if(top_other.scoreDocs[i].doc == top_simantikhs.scoreDocs[j].doc) {
			    		        			 map.put(top_other.scoreDocs[i].doc, score);
			    		        			 score ++;
			    		        		 }
			    		        	 }
			    		         }
	    		        	 
	    		        	 }else 
	    		        	 {	 
	    		        		//Integer[] array_tmp = intersection.toArray(new Integer[0]);
	    		        		 //top_simantikhs = array_tmp; //iso me to prohgoumeno intersection

	    		        		 int top_simantikhs[] = intersection.stream().mapToInt(Integer::intValue).toArray();
	    		        		 
	    		        		 for(int i=0; i<top_other.scoreDocs.length; i++)
			    		         {
			    		        	 for(int j=0; j<top_simantikhs.length; j++)
			    		        	 {
			    		        		 //System.out.println(top_other.scoreDocs[j].doc +"     "+ top_type.scoreDocs[j].doc);
			    		        		 if(top_other.scoreDocs[i].doc == top_simantikhs[j]) {
			    		        			 map.put(top_other.scoreDocs[i].doc, score);
			    		        			 score ++;
			    		        		 }
			    		        	 }
			    		         }
	    		        	 }
	    		        	 Map<Integer, Integer> hm1 = sortByValue(map);		         
		    		         System.out.println("Sorted Map   : " + hm1);
		    		         

		    		         int intersection_arr[] = setToIntArray(hm1.keySet());
		    		         for (int i : intersection_arr) {
		    		        	 intersection.add(i);
		    		         }
	    		         }
	    		         
	    		         Integer[] all_intersections = intersection.toArray(new Integer[0]);
	    		         
	    		         
	    		         int mexri = Math.min(10, all_intersections.length);	//to plh8os twn koinwn documents.  EDW EINAI DIATETAGMENA ME TH SEIRA
	    		         System.out.println("Total hits: " + mexri);
	    		         
	    		         for (int i=0; i < mexri; i++) {
	 				    	 
	    		        	 System.out.println("Id: " + all_intersections[i]);
	 				    	 
	 				    	 text += "Movie Title: "+ isearcher.doc(all_intersections[i]).getField("Title").stringValue() +"\n"+
		    			        	"Description: "+ isearcher.doc(all_intersections[i]).getField("Description").stringValue() + "\n\n";
		    			        	
	 				     }	

	    		         textArea.setText(text);
	    		         
	    		        String fields = "FIELDS: ";
	 				    for(String s:array) {
	 				    	fields = fields + s+" ";
	 				    }
	 				    fields += ".SEARCHED:  "+ text_query;
	 				    history.put(fields, text);
	 				    JMenuItem tmp_menu = new JMenuItem(fields);
	 				    mnNewMenu.add(tmp_menu);
    				     
		    			 clearSystem();
		    			 return;
		    		}
		    		else if(release_year_enabled & !text_query.equals(""))		// Erwthmata me varos sto type
		    		{
	    				TopDocs tops[] = new TopDocs[array.size() - 1 ];
	    				array.remove(array.indexOf("Release Year"));
	    				
	    				Query query1 = new QueryParser("Release Year", analyzer).parse(text_query);
	    		    	TopDocs top_relyer =  isearcher.search(query1, 8500);
	    		    	
	    		    	//List<Set<Integer>> all_sets = new ArrayList<Set<Integer>>();;
	    		    	
	    		    	
	    		    	//fill the type_set. Contains Top Docs ordered by score
	    		    	Set<Integer> type_set = new HashSet<Integer>();
	    		    	for(int k=0; k < top_relyer.scoreDocs.length; k++) 
	    		    		type_set.add(top_relyer.scoreDocs[k].doc);
	    		    	System.out.println("Doc ids found in 'Release Year: "+type_set); 
	    		    	//all_sets.add(type_set);
	    		    	
	    		    	
	    		    	for(int i=0; i<tops.length; i++) 
		    			{
			    			
			    			if(array.get(i).equals("Title"))
			    			{
			    				queries[i] = new QueryParser("Title", analyzer).parse(text_query);
			    				
			    				tops[i] =  isearcher.search(queries[i], 8500);
			    				
			    				Set<Integer> set_tmp = new HashSet<Integer>();
			    				for(int k=0; k < tops[i].scoreDocs.length; k++) 
			    					set_tmp.add(tops[i].scoreDocs[k].doc);
			    				System.out.println("Doc ids found in 'Title: "+set_tmp); 
			    				//all_sets.add(set_tmp);
			    			}
			    			else if(array.get(i).equals("listed_in"))
			    			{
			    				String type = getMovieType(typeMenu);
			    				System.out.println(type+")");
			    				
			    				queries[i] = new QueryParser("listed_in", analyzer).parse(type);
			    				
			    				tops[i] =  isearcher.search(queries[i], 8500);
			    				
			    				Set<Integer> set_tmp = new HashSet<Integer>();
			    				for(int k=0; k < tops[i].scoreDocs.length; k++) 
			    					set_tmp.add(tops[i].scoreDocs[k].doc);
			    				System.out.println("Doc ids found in 'listed_in: "+set_tmp);
			    				//all_sets.add(set_tmp);
			    			}
			    			else if(array.get(i).equals("Description"))
			    			{
			    				queries[i] = new QueryParser("Description", analyzer).parse(text_query);
			    				
			    				tops[i] =  isearcher.search(queries[i], 8500);
			    				
			    				Set<Integer> set_tmp = new HashSet<Integer>();
			    				for(int k=0; k < tops[i].scoreDocs.length; k++) 
			    					set_tmp.add(tops[i].scoreDocs[k].doc);
			    				System.out.println("Doc ids found in 'Description: "+set_tmp);
			    				//all_sets.add(set_tmp);
			    			}
		    			}

	    		    	
	    		    	//Find the intersection of all sets. 
	    		    	
	    		         // vres 8eseis koinwn kai scores kai valta sto hashmap.
	    		        
	    		         ArrayList<Integer> intersection = new ArrayList<Integer>(); 		// ola ta koina twn ana dyo topDocs pinakwn
	    		         for(int k=0; k<tops.length; k++)
	    		         {
	    		        	 int score = 0;
	    		        	 HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
	    		        	
	    		        	 TopDocs top_other = tops[k];
	    		        	 
	    		        	 if(k == 0)
	    		        	 {
	    		        		 TopDocs top_simantikhs = top_relyer;	//tops[k];
	    		        	 	
		    		        	 for(int i=0; i<top_other.scoreDocs.length; i++)
			    		         {
			    		        	 for(int j=0; j<top_simantikhs.scoreDocs.length; j++)
			    		        	 {
			    		        		 //System.out.println(top_other.scoreDocs[j].doc +"     "+ top_type.scoreDocs[j].doc);
			    		        		 if(top_other.scoreDocs[i].doc == top_simantikhs.scoreDocs[j].doc) {
			    		        			 map.put(top_other.scoreDocs[i].doc, score);
			    		        			 score ++;
			    		        		 }
			    		        	 }
			    		         }
	    		        	 
	    		        	 }else 
	    		        	 {	 
	    		        		//Integer[] array_tmp = intersection.toArray(new Integer[0]);
	    		        		 //top_simantikhs = array_tmp; //iso me to prohgoumeno intersection

	    		        		 int top_simantikhs[] = intersection.stream().mapToInt(Integer::intValue).toArray();
	    		        		 
	    		        		 for(int i=0; i<top_other.scoreDocs.length; i++)
			    		         {
			    		        	 for(int j=0; j<top_simantikhs.length; j++)
			    		        	 {
			    		        		 //System.out.println(top_other.scoreDocs[j].doc +"     "+ top_type.scoreDocs[j].doc);
			    		        		 if(top_other.scoreDocs[i].doc == top_simantikhs[j]) {
			    		        			 map.put(top_other.scoreDocs[i].doc, score);
			    		        			 score ++;
			    		        		 }
			    		        	 }
			    		         }
	    		        	 }
	    		        	 Map<Integer, Integer> hm1 = sortByValue(map);		         
		    		         System.out.println("Sorted Map   : " + hm1);
		    		         

		    		         int intersection_arr[] = setToIntArray(hm1.keySet());
		    		         for (int i : intersection_arr) {
		    		        	 intersection.add(i);
		    		         }
	    		         }
	    		         
	    		         Integer[] all_intersections = intersection.toArray(new Integer[0]);
	    		         
	    		         
	    		         int mexri = Math.min(10, all_intersections.length);	//to plh8os twn koinwn documents.  EDW EINAI DIATETAGMENA ME TH SEIRA
	    		         System.out.println("Total hits: " + mexri);
	    		         
	    		         for (int i=0; i < mexri; i++) {
	 				    	 
	    		        	 System.out.println("Id: " + all_intersections[i]);
	 				    	 
	 				    	 text += "Movie Title: "+ isearcher.doc(all_intersections[i]).getField("Title").stringValue() +"\n"+
		    			        	"Description: "+ isearcher.doc(all_intersections[i]).getField("Description").stringValue() + "\n\n";
		    			        	
	 				     }
	    		         textArea.setText(text);
	    		         
	    		        String fields = "FIELDS: ";
	 				    for(String s:array) {
	 				    	fields = fields + s+" ";
	 				    }
	 				    fields += ".SEARCHED:  "+ text_query;
	 				    history.put(fields, text);
	 				    JMenuItem tmp_menu = new JMenuItem(fields);
	 				    mnNewMenu.add(tmp_menu);
		    			 
	    				 clearSystem();
		    			 return;
		    		}
		    		else
		    		{
		    			for(int i=0; i<queries.length; i++) 
		    			{
			    			
			    			if(array.get(i).equals("Title") && !text_query.equals(""))
			    			{
			    				queries[i] = new QueryParser("Title", analyzer).parse(text_query);
			    				
			    		    	TopDocs top_title =  isearcher.search(queries[i], 10);
			    		    	
			    		    	int titles = Math.min(5, top_title.scoreDocs.length);
			    				for (int j=0; j <titles;  j++) {
					    			result += "Movie Title: "+ isearcher.doc(top_title.scoreDocs[j].doc).getField("Title").stringValue() +"\n"+
						        	"Description: "+ isearcher.doc(top_title.scoreDocs[j].doc).getField("Description").stringValue() + "\n\n";
						        }
			    				for (int m=0; m < titles; m++) {
							          System.out.println("Id: " + top_title.scoreDocs[m].doc + " score: "+ top_title.scoreDocs[m].score);
							    }
			    			}
			    			else if(array.get(i).equals("Description") && !text_query.equals("")) 
			    			{
			    				queries[i] = new QueryParser("Description", analyzer).parse(text_query);
			    				
			    		    	TopDocs top_desc =  isearcher.search(queries[i], 10);

			    		    	int descs = Math.min(3, top_desc.scoreDocs.length);
			    				for (int j=0; j < descs; j++) {
					    			result += "Movie Title: "+ isearcher.doc(top_desc.scoreDocs[j].doc).getField("Title").stringValue() +"\n"+
						        	"Description: "+ isearcher.doc(top_desc.scoreDocs[j].doc).getField("Description").stringValue() + "\n\n";
						        }
			    				for (int m=0; m < descs; m++) {
							          System.out.println("Id: " + top_desc.scoreDocs[m].doc + " score: "+ top_desc.scoreDocs[m].score);
							    }

			    			}else if(array.get(i).equals("listed_in")) 
			    			{
			    				String type = getMovieType(typeMenu);
			    				System.out.println(type+")");
			    				queries[i] = new QueryParser("listed_in", analyzer).parse(type);
			    				
			    		    	TopDocs top_type =  isearcher.search(queries[i], 10);
			    				
			    		    	int types = Math.min(3, top_type.scoreDocs.length);
			    				for (int j=0; j < types; j++) {
					    			result += "Movie Title: "+ isearcher.doc(top_type.scoreDocs[j].doc).getField("Title").stringValue() +"\n"+
						        	"Description: "+ isearcher.doc(top_type.scoreDocs[j].doc).getField("Description").stringValue() + "\n\n";
						        }
			    				for (int m=0; m < types; m++) {
							          System.out.println("Id: " + top_type.scoreDocs[m].doc + " score: "+ top_type.scoreDocs[m].score);
							    }

			    			}else if(array.get(i).equals("Release Year") && !text_query.equals("")) 
			    			{
			    				queries[i] = new QueryParser("Release Year", analyzer).parse(text_query);
			    				
			    		    	TopDocs top_relyer =  isearcher.search(queries[i], 10);

			    		    	int relsy = Math.min(2, top_relyer.scoreDocs.length);
			    				for (int j=0; j < relsy; j++) {
					    			result += "Movie Title: "+ isearcher.doc(top_relyer.scoreDocs[j].doc).getField("Title").stringValue() +"\n"+
						        	"Description: "+ isearcher.doc(top_relyer.scoreDocs[j].doc).getField("Description").stringValue() + "\n\n";
						        }
			    				for (int m=0; m < relsy; m++) {
							          System.out.println("Id: " + top_relyer.scoreDocs[m].doc + " score: "+ top_relyer.scoreDocs[m].score);
							    }
			    			}
		    			}
		    			
		    			textArea.setText(result);
			    		if(result.equals(""))
			    			textArea.setText("							RESULTS NOT FOUND					");
			    		
			    		String fields = "FIELDS: ";
					    for(String s:array) {
					    	fields = fields + s+" ";
					    }
					    fields += ".SEARCHED:  "+ text_query;
					    history.put(fields, text);
					    JMenuItem tmp_menu = new JMenuItem(fields);
					    mnNewMenu.add(tmp_menu);
   				     	
			    		clearSystem();
			    		return;
		    		}
		    		
		    	}	
		    }
		   
		    TopDocs top = null;		    
		    
		    top = isearcher.search(query, 10);
		    
		    if (top != null) 
		    {		    	
		        System.out.println("Total hits: " + top.totalHits);
		        for (int i=0; i < top.scoreDocs.length; i++) {
		          System.out.println("Id: " + top.scoreDocs[i].doc + " score: "+ top.scoreDocs[i].score);
		        }
		        
		        for (int i=0; i < top.scoreDocs.length; i++) {
			        
		        	text += "Movie Title: "+ isearcher.doc(top.scoreDocs[i].doc).getField("Title").stringValue() +"\n"+
		        	"Description: "+ isearcher.doc(top.scoreDocs[i].doc).getField("Description").stringValue() + "\n\n";
		        	
		        	textArea.setText(text);
		        }
		    }
		    if(text.equals(""))
    			textArea.setText("							RESULTS NOT FOUND					");
		    
		    String fields = "FIELDS: ";
		    for(String s:array) {
		    	fields = fields + s+" ";
		    }
		    fields += ".SEARCHED:  "+ text_query;
		    history.put(fields, text);
		    JMenuItem tmp_menu = new JMenuItem(fields);
		    mnNewMenu.add(tmp_menu);
		    
		} catch (IOException | ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		clearSystem();
	}
	
	
	private int[] setToIntArray(Set<Integer> intersection_data)
	{
		int n = intersection_data.size();
        int arr[] = new int[n];
  
        int m = 0;
        for (Integer x : intersection_data)
            arr[m++] = x;
        
        return arr;
	}
	
	public static HashMap<Integer, Integer>
    sortByValue(HashMap<Integer, Integer> hm)
    {
        HashMap<Integer, Integer> temp
            = hm.entrySet()
                  .stream()
                  .sorted((i1, i2)
                              -> i1.getValue().compareTo(
                                  i2.getValue()))
                  .collect(Collectors.toMap(
                      Map.Entry::getKey,
                      Map.Entry::getValue,
                      (e1, e2) -> e1, LinkedHashMap::new));
 
        return temp;
    }
	
	private void clearSystem() {
		//Clear System
		for(JButton btn: buttons) 
			btn.setBackground(Color.LIGHT_GRAY);
		for(int i=0; i<buttons_pressed.length; i++)
			buttons_pressed[i] = 0;
		for(int i=0; i< typeMenu.length; i++)
			typeMenu[i] = 0;
		
		type_enabled = false;
		release_year_enabled = false;
	}
	
	private ArrayList<String> getIndexes(int[] ar){
		ArrayList<String> array = new ArrayList<String>();
		
		
		for(int i = 0; i < ar.length; i++) {
			if (ar[i] == 1) {
				if(i == 0)
					array.add("Title");
				else if(i == 1) {
						array.add("Release Year");
						release_year_enabled = true;
				}
				else if(i == 2)
						array.add("Description");
				else if(i == 3) {
						array.add("listed_in");
						type_enabled = true;
				}
			}
		}
		System.out.println("Buttons pressed: ");
		printArrayList(array);
		return array;
	}
	
	private void printArrayList(ArrayList<String> ar) {
		
		for(String s: ar) {
			if(s.equals("listed_in")) 
				System.out.print("("+s+": ");
			else System.out.print(s+", ");
		}
		if(!type_enabled)System.out.println();	
	}
	
	
	private String getMovieType(int[] types) {
		int getIndex = -1;
		
		for(int i=0; i < types.length; i++) {
			if(types[i] == 1)
				getIndex = i;
		}
		
		if(getIndex == 0)
			return "Dramas";
		else if(getIndex == 1)
			return "Comedies";
		else if(getIndex == 2)
			return "Sports";
		else if(getIndex == 3)
			return "Documentaries";
		else if(getIndex == 4)
			return "Thrillers";
		else if(getIndex == 5)
			return "Fantasy";
		else if(getIndex == 6)
			return "Romantic";
		else if(getIndex == 7)
			return "Independent Movies";
		else if(getIndex == 8)
			return "Anime Features";
		else
			return null;
	}
	
	//TEST FOR VALIDATION OF THE CORRECT ORDER FOR SOME QUERIES
	private void test()
	{
		HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
        
		int x[] =  {1,5,7,2,6,43,87};
		int y[] = {7,9,13,1,33,34};
        // vres 8eseis koinwn kai scores kai valta sto hashmap.
        int score = 0;
        for(int i=0; i<y.length; i++)
        {
       	 for(int j=0; j<x.length; j++)
       	 {
       		 //System.out.println(top_other.scoreDocs[j].doc +"     "+ top_type.scoreDocs[j].doc);
       		 if(x[j] == y[i]) {
       			 System.out.println(x[j]);
       			 map.put(x[j], score);
       			 score ++;
       		 }
       	 }
        }
        Map<Integer, Integer> hm1 = sortByValue(map);		         
        System.out.println("Sorted Map   : " + hm1);
	}
}

// ================================================================================= //
/*
Query query2 = new QueryParser(array.get(0), analyzer).parse(text_query);
TopDocs top_other =  isearcher.search(query2, 8500);
	    		    	
 Set<Integer> set1 = new HashSet<Integer>();
 Set<Integer> set2 = new HashSet<Integer>();


for(int k=0; k < top_type.scoreDocs.length; k++) 
	set1.add(top_type.scoreDocs[k].doc);

for(int k=0; k < top_other.scoreDocs.length; k++)
	set2.add(top_other.scoreDocs[k].doc);
 
 System.out.println("Doc ids found in 'listed_in: "+set1); 
 System.out.println("\n\n\n");
 System.out.println(set2); 
 System.out.println("\n\n\n");
 
 HashMap<Integer,Integer> map = new HashMap<Integer,Integer>();
 
 // vres 8eseis koinwn kai scores kai valta sto hashmap.
 int score = 0;
 for(int i=0; i<top_other.scoreDocs.length; i++)
 {
	 for(int j=0; j<top_type.scoreDocs.length; j++)
	 {
		 //System.out.println(top_other.scoreDocs[j].doc +"     "+ top_type.scoreDocs[j].doc);
		 if(top_other.scoreDocs[i].doc == top_type.scoreDocs[j].doc) {
			 map.put(top_other.scoreDocs[i].doc, score);
			 score ++;
		 }
	 }
 }
 Map<Integer, Integer> hm1 = sortByValue(map);		         
 System.out.println("Sorted Map   : " + hm1);
 

 int keys[] = setToIntArray(hm1.keySet());
 
 
 int mexri = Math.min(10, keys.length);	//to plh8os twn koinwn documents.  EDW EINAI DIATETAGMENA ME TH SEIRA
 System.out.println("Total hits: " + mexri);
 
 for (int i=0; i < mexri; i++) {
 	 
	 System.out.println("Id: " + keys[i]);
 	 
 	 text += "Movie Title: "+ isearcher.doc(keys[i]).getField("Title").stringValue() +"\n"+
        	"Description: "+ isearcher.doc(keys[i]).getField("Description").stringValue() + "\n\n";
        	
  }*/
