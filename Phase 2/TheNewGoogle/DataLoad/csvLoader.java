package DataLoad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class csvLoader {

	
	public ArrayList<String[]> csv_loader(String pathToCsv){
		
		ArrayList<String[]> ar = new ArrayList<String[]>();
		
		String line = " ";
		try   
		{  
			//parsing a CSV file into BufferedReader class constructor  
			BufferedReader br = new BufferedReader(new FileReader(pathToCsv));
			
			while ((line = br.readLine()) != null)   //returns a Boolean value  
			{  
				String[] line_to_ar = new String[6];
				String[] words = line.split(",");    // use comma as separator    

				for(int i=0; i<6; i++) {
					if(words[i] != null) 
						line_to_ar[i] = words[i];
				}	
				ar.add(line_to_ar);
			}  
			
			br.close();
			return ar;
		}   
		catch (IOException e)   
		{  
			e.printStackTrace();  
		}
		return null;  
	}
	
}
