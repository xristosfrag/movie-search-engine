package GUI;


import java.awt.EventQueue;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFrame;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import DataLoad.csvLoader;
import commands.CommandsFactory;

import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.TextArea;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Button;
import javax.swing.JTextPane;


public class app {

	private JFrame frmNetfilxDb;
	private JTextField searchArea;
	private JButton changeByTitle_btn;
	private JButton searchByRYear_btn;
	private JButton searchByDesc_btn;
	private JLabel lblNewLabel;
	private JButton search_button;
	private JMenuBar menuBar;
	
	private JMenuItem mntmNewMenuItem_5;
	private JMenuItem mntmNewMenuItem_6;
	private JMenuItem mntmNewMenuItem_7;
	private JMenuItem mntmNewMenuItem_8;
	private JMenuItem mntmNewMenuItem_9;
	private JMenuItem mntmNewMenuItem_10;
	private JMenuItem mntmNewMenuItem_11;
	private JMenuItem mntmNewMenuItem_12;
	private JMenuItem mntmNewMenuItem_13;
	
	public JButton buttons[];
	
	private static StandardAnalyzer analyzer;
	private static FSDirectory directory;
	private Button button;
	private Button delete_searchArea;
	
	
	private HashMap<String,String> history = new HashMap<String,String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {			
				try {
					app window = new app();
					window.frmNetfilxDb.setVisible(true);					
				} catch (Exception e) {
					e.printStackTrace();
				}   
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public app() throws IOException {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frmNetfilxDb = new JFrame();
		frmNetfilxDb.setResizable(false);
		frmNetfilxDb.setTitle("NETFILX DB");
		frmNetfilxDb.getContentPane().setForeground(Color.BLACK);
		frmNetfilxDb.setForeground(Color.BLACK);
		frmNetfilxDb.setBackground(Color.BLACK);
		frmNetfilxDb.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		frmNetfilxDb.setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\UNIVERSITY\\SEMESTER_10\\Anakthsh_Plhroforias\\images.jpg"));
		frmNetfilxDb.setBounds(100, 100, 913, 701);
		frmNetfilxDb.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		CommandsFactory commands_factory = new CommandsFactory();
		
		searchArea = new JTextField();
		searchArea.setBounds(261, 110, 374, 31);
		searchArea.setText("Search movies or type a keyword");
		searchArea.setToolTipText("");
		searchArea.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 152, 856, 2);
		
		lblNewLabel = new JLabel("Search");
		lblNewLabel.setForeground(new Color(0, 204, 255));
		lblNewLabel.setBounds(384, 58, 129, 45);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 28));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		search_button = new JButton("Search");
		search_button.setBounds(645, 114, 122, 23);
		searchArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					search_button.doClick();
				}
			}
		});
		frmNetfilxDb.getContentPane().setLayout(null);
		frmNetfilxDb.getContentPane().add(separator);
		frmNetfilxDb.getContentPane().add(searchArea);
		frmNetfilxDb.getContentPane().add(search_button);
		frmNetfilxDb.getContentPane().add(lblNewLabel);
		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setBounds(28, 176, 838, 424);
		frmNetfilxDb.getContentPane().add(textArea);
		
		frmNetfilxDb.setJMenuBar(menuBar);
		
		menuBar = new JMenuBar();
		frmNetfilxDb.setJMenuBar(menuBar);
		
		button = new Button("Search By:");
		button.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
		button.setEnabled(false);
		menuBar.add(button);
		
		changeByTitle_btn = new JButton("Title");
		changeByTitle_btn.setBackground(Color.LIGHT_GRAY);
		menuBar.add(changeByTitle_btn);
		
		searchByRYear_btn = new JButton("Release Year");
		searchByRYear_btn.setBackground(Color.LIGHT_GRAY);
		menuBar.add(searchByRYear_btn);
		searchByRYear_btn.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		int  typeMenu[] = {0,0,0,0,0,0,0,0,0};
		
		JMenu mnNewMenu_1 = new JMenu("Type");
		menuBar.add(mnNewMenu_1);
		
		mntmNewMenuItem_11 = new JMenuItem("Romantic");
		mnNewMenu_1.add(mntmNewMenuItem_11);
		
		mntmNewMenuItem_5 = new JMenuItem("Dramas");
		mnNewMenu_1.add(mntmNewMenuItem_5);
		
		mntmNewMenuItem_7 = new JMenuItem("Sports");
		mnNewMenu_1.add(mntmNewMenuItem_7);
		
		mntmNewMenuItem_9 = new JMenuItem("Thrillers");
		mnNewMenu_1.add(mntmNewMenuItem_9);
		
		mntmNewMenuItem_10 = new JMenuItem("Fantasy");
		mnNewMenu_1.add(mntmNewMenuItem_10);
		
		mntmNewMenuItem_6 = new JMenuItem("Comedies");
		mnNewMenu_1.add(mntmNewMenuItem_6);
		
		mntmNewMenuItem_8 = new JMenuItem("Documentaries");
		mnNewMenu_1.add(mntmNewMenuItem_8);
		
		mntmNewMenuItem_12 = new JMenuItem("Independent Movies");
		mnNewMenu_1.add(mntmNewMenuItem_12);
		
		mntmNewMenuItem_13 = new JMenuItem("Anime Features");
		mnNewMenu_1.add(mntmNewMenuItem_13);
		
		searchByDesc_btn = new JButton("Description");
		searchByDesc_btn.setBackground(Color.LIGHT_GRAY);
		menuBar.add(searchByDesc_btn);
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setEnabled(false);
		menuBar.add(textPane);
		
		JMenu mnNewMenu = new JMenu("History");
		menuBar.add(mnNewMenu);
		
		delete_searchArea = new Button("X");
		delete_searchArea.setForeground(new Color(255, 0, 0));
		delete_searchArea.setBackground(Color.WHITE);
		delete_searchArea.setFont(new Font("Dialog", Font.BOLD, 12));
		delete_searchArea.setBounds(240, 110, 15, 32);
		frmNetfilxDb.getContentPane().add(delete_searchArea);
		
		buttons = new JButton[] {changeByTitle_btn,searchByRYear_btn, searchByDesc_btn};
		int[] buttons_pressed = {0,0,0,0};
		
		//---------------------------ACTION LISTENERS----------------------------
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ev) {
		            if(typeMenu[0] == 0) {
		            	typeMenu[0] = 1;
		            	
		            	typeMenu[1] = 0;
		            	typeMenu[2] = 0;
		            	typeMenu[3] = 0;
		            	typeMenu[4] = 0;
		            	typeMenu[5] = 0;
		            	typeMenu[6] = 0;
		            	typeMenu[7] = 0;
		            	typeMenu[8] = 0;
		            	buttons_pressed[3] = 1;
		            }else {
		            	typeMenu[0] = 0;
		            	buttons_pressed[3] = 0;
		            }
		    }
		});
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ev) {
		    	if(typeMenu[1] == 0) {
	            	typeMenu[1] = 1;
	            	
	            	typeMenu[0] = 0;
	            	typeMenu[2] = 0;
	            	typeMenu[3] = 0;
	            	typeMenu[4] = 0;
	            	typeMenu[5] = 0;
	            	typeMenu[6] = 0;
	            	typeMenu[7] = 0;
	            	typeMenu[8] = 0;
	            	buttons_pressed[3] = 1;
	            }else {
	            	typeMenu[1] = 0;
	            	buttons_pressed[3] = 0;
	            }
		    }
		});
		mntmNewMenuItem_7.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ev) {
		    	if(typeMenu[2] == 0) {
	            	typeMenu[2] = 1;
	            	
	            	typeMenu[0] = 0;
	            	typeMenu[1] = 0;
	            	typeMenu[3] = 0;
	            	typeMenu[4] = 0;
	            	typeMenu[5] = 0;
	            	typeMenu[6] = 0;
	            	typeMenu[7] = 0;
	            	typeMenu[8] = 0;
	            	buttons_pressed[3] = 1;
	            }else {
	            	typeMenu[2] = 0;
	            	buttons_pressed[3] = 0;
	            }
		    }
		});
		mntmNewMenuItem_8.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ev) {
		    	if(typeMenu[3] == 0) {
	            	typeMenu[3] = 1;
	            	
	            	typeMenu[0] = 0;
	            	typeMenu[1] = 0;
	            	typeMenu[2] = 0;
	            	typeMenu[4] = 0;
	            	typeMenu[5] = 0;
	            	typeMenu[6] = 0;
	            	typeMenu[7] = 0;
	            	typeMenu[8] = 0;
	            	buttons_pressed[3] = 1;
	            }else {
	            	typeMenu[3] = 0;
	            	buttons_pressed[3] = 0;
	            }
		    }
		});
		mntmNewMenuItem_9.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ev) {
		    	if(typeMenu[4] == 0) {
	            	typeMenu[4] = 1;
	            	
	            	typeMenu[0] = 0;
	            	typeMenu[1] = 0;
	            	typeMenu[2] = 0;
	            	typeMenu[3] = 0;
	            	typeMenu[5] = 0;
	            	typeMenu[6] = 0;
	            	typeMenu[7] = 0;
	            	typeMenu[8] = 0;
	            	buttons_pressed[3] = 1;
	            }else {
	            	typeMenu[4] = 0;
	            	buttons_pressed[3] = 0;
	            }
		    }
		});
		mntmNewMenuItem_10.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ev) {
		    	if(typeMenu[5] == 0) {
	            	typeMenu[5] = 1;
	            	
	            	
	            	typeMenu[0] = 0;
	            	typeMenu[1] = 0;
	            	typeMenu[2] = 0;
	            	typeMenu[3] = 0;
	            	typeMenu[4] = 0;
	            	typeMenu[6] = 0;
	            	typeMenu[7] = 0;
	            	typeMenu[8] = 0;
	            	buttons_pressed[3] = 1;
	            }else {
	            	typeMenu[5] = 0;
	            	buttons_pressed[3] = 0;
	            }
		    }
		});
		mntmNewMenuItem_11.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ev) {
		    	if(typeMenu[5] == 0) {
	            	typeMenu[6] = 1;
	            	
	            	typeMenu[0] = 0;
	            	typeMenu[1] = 0;
	            	typeMenu[2] = 0;
	            	typeMenu[3] = 0;
	            	typeMenu[4] = 0;
	            	typeMenu[5] = 0;
	            	typeMenu[7] = 0;
	            	typeMenu[8] = 0;
	            	buttons_pressed[3] = 1;
	            }else {
	            	typeMenu[6] = 0;
	            	buttons_pressed[3] = 0;
	            }
		    }
		});
		mntmNewMenuItem_12.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ev) {
		    	if(typeMenu[7] == 0) {
	            	typeMenu[7] = 1;
	            	
	            	typeMenu[0] = 0;
	            	typeMenu[1] = 0;
	            	typeMenu[2] = 0;
	            	typeMenu[3] = 0;
	            	typeMenu[4] = 0;
	            	typeMenu[5] = 0;
	            	typeMenu[6] = 0;
	            	typeMenu[8] = 0;
	            	buttons_pressed[3] = 1;
	            }else {
	            	typeMenu[7] = 0;
	            	buttons_pressed[3] = 0;
	            }
		    }
		});
		mntmNewMenuItem_13.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent ev) {
		    	if(typeMenu[8] == 0) {
	            	typeMenu[8] = 1;
	            	
	            	typeMenu[0] = 0;
	            	typeMenu[1] = 0;
	            	typeMenu[2] = 0;
	            	typeMenu[3] = 0;
	            	typeMenu[4] = 0;
	            	typeMenu[5] = 0;
	            	typeMenu[6] = 0;
	            	typeMenu[7] = 0;
	            	buttons_pressed[3] = 1;
	            }else {
	            	typeMenu[8] = 0;
	            	buttons_pressed[3] = 0;
	            }
		    }
		});
		
		
		ActionListener delete_searchAreaListener = commands_factory.createCommand("DeleteSearch",null,null,searchArea,null,null,null,null,null,null,null);
		delete_searchArea.addActionListener(delete_searchAreaListener);
		
		ActionListener titleButtonListener = commands_factory.createCommand("Title",buttons,buttons_pressed,null,null,null,null,null,null,null,null);
		changeByTitle_btn.addActionListener(titleButtonListener);
		
		ActionListener releaseYearButtonListener = commands_factory.createCommand("Release Year",buttons,buttons_pressed,null,null,null,null,null,null,null,null);
		searchByRYear_btn.addActionListener(releaseYearButtonListener);
		
		//ActionListener typeButtonListener = commands_factory.createCommand("Type",null,null,null,null,null,null,null, mnNewMenu_1);
		//mnNewMenu_1.addActionListener(typeButtonListener);
		
		ActionListener descriptionButtonListener = commands_factory.createCommand("Description",buttons,buttons_pressed,null,null,null,null,null,null,null,null);
		searchByDesc_btn.addActionListener(descriptionButtonListener);
		
		//INITIALIZE OBJECTS
		try {
			boolean create = false;
			if(create)
				createIndex();
			else {
				System.out.println("Welcome to netflix DB!");
				directory = FSDirectory.open(Paths.get("D:\\UNIVERSITY\\SEMESTER_10\\Anakthsh_Plhroforias\\data"));
				analyzer = new StandardAnalyzer();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ActionListener searchButtonListener = commands_factory.createCommand("Search",buttons,buttons_pressed,searchArea,textArea,analyzer,directory, typeMenu, history, mnNewMenu, search_button);
		search_button.addActionListener(searchButtonListener);
		
		//directory.close();
	}
	
	private void createIndex() throws Exception
	{
		String pathToCsv = "D:\\UNIVERSITY\\SEMESTER_10\\Anakthsh_Plhroforias\\datasets\\netflix_titles.csv\\raw_data.csv";
		csvLoader csvl = new csvLoader();
		ArrayList<String[]> ar = null;
		ArrayList<String[]> ar_n = new ArrayList<String[]>();
		ar = csvl.csv_loader(pathToCsv);
		
		//Format data
		for(String[] s: ar) {
			ar_n.add(restoreLine(s));				
		}
		ar = ar_n;
		printArraylist(ar);
		
		System.out.println(ar.size());
		
		//-----------------------------------------------------------------------------
		String indexPath = "D:\\UNIVERSITY\\SEMESTER_10\\Anakthsh_Plhroforias\\data";
	    String docsPath = "D:\\UNIVERSITY\\SEMESTER_10\\Anakthsh_Plhroforias\\datasets\\netflix_titles.csv\\raw_data.csv";
	    boolean create = true;

	    final Path docDir = Paths.get(docsPath);
	    if (!Files.isReadable(docDir)) {
	      System.out.println("Document directory '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
	      System.exit(1);
	    }
	    
	    Date start = new Date();
	    try {
	      System.out.println("Indexing to directory '" + indexPath + "'...");

	      directory = FSDirectory.open(Paths.get(indexPath));
	      analyzer = new StandardAnalyzer();
	      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
	      
	      if (create) {
	        // Create a new index in the directory, removing any
	        // previously indexed documents:
	        iwc.setOpenMode(OpenMode.CREATE);
	     } else {
	        // Add new documents to an existing index:
	        iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
	      }

	      // Optional: for better indexing performance, if you
	      // are indexing many documents, increase the RAM
	     // buffer.  But if you do this, increase the max heap
	      // size to the JVM (eg add -Xmx512m or -Xmx1g):
	      //
	      // iwc.setRAMBufferSizeMB(256.0);
	      //System.out.println("HERE");
	      IndexWriter iwriter = new IndexWriter(directory, iwc);
	      initalizeDocuments(analyzer,iwriter, ar);

	      // NOTE: if you want to maximize search performance,
	      // you can optionally call forceMerge here.  This can be
	      // a terribly costly operation, so generally it's only
	      // worth it when your index is relatively static (ie
	      // you're done adding documents to it):
	      //
	      // writer.forceMerge(1);

	      iwriter.close();

	      Date end = new Date();
	      System.out.println(end.getTime() - start.getTime() + " total milliseconds");

	    } catch (IOException e) {
	      System.out.println(" caught a " + e.getClass() +
	       "\n with message: " + e.getMessage());
	    }
	}
	private static Document createDocument(String[] line){
		
		Document doc = new Document();
		
	    doc.add(new Field("Title", line[0], TextField.TYPE_STORED));
	    doc.add(new Field("Director", line[1], TextField.TYPE_NOT_STORED));
	    doc.add(new Field("Release Year", line[2], TextField.TYPE_STORED));
	    doc.add(new Field("Duration", line[3], TextField.TYPE_NOT_STORED));
	    doc.add(new Field("listed_in", line[4], TextField.TYPE_STORED));
	    doc.add(new Field("Description", line[5], TextField.TYPE_STORED));
	    
	    return doc;    
	}
	
	static void initalizeDocuments(StandardAnalyzer analyzer,IndexWriter iwriter, ArrayList<String[]> ar) throws Exception {
		
	    //IndexWriterConfig config = new IndexWriterConfig(analyzer);
	    //IndexWriter iwriter = new IndexWriter(directory, config);

	    for(String[] line: ar) {
	    	Document doc = createDocument(line);
	    	
	    	iwriter.addDocument(doc);
	    }
	    iwriter.close();
	}
	
	private static String[] restoreLine(String[] line) {
		
		String[] line_tmp = new String[6];
		
		for(int i=0; i<line.length; i++){	//String field: line) {
			
			String field = line[i];
			String s = "";
			
			char[] characters = field.toCharArray(); 
			
			
			for(int j=0; j<characters.length; j++) {
				if(Character.compare(characters[j], '_') == 0) {
					characters[j] = ',';
				}
				s += characters[j];
			}
		
			line_tmp[i] = s;
		}
		return line_tmp;
	}

	private static void printArraylist(ArrayList<String[]> ar) {
		for(String[] line: ar) {
			for(String s: line)
				System.out.print(s+" ");
			System.out.println();
		}
	}
}


/*		DHMIOYRGIA EVRETHRIOU ME PROSERINA DIRECTORIES
 * /*
		String prefix = "TempDirectory";
		Path dir = (Path)Paths.get("D:\\UNIVERSITY\\SEMESTER_10\\Anakthsh_Plhroforias\\data");
		//File file = path.toFile();
				
	    Path indexPath;
		try {
			analyzer = new StandardAnalyzer();
			indexPath = Files.createTempDirectory(dir, prefix);
			directory = FSDirectory.open(indexPath);
			
			try {
				// create docs for lucene
				initalizeDocuments(analyzer, directory, ar);
				
				
				//queries
				//read();//analyzer, directory);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
