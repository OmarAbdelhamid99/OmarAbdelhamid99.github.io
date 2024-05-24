import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DBApp {
	static int MaximumRowsCountinTablePage;
    static int MaximumEntriesinOctreeNode;
    private Hashtable<String, Table> tables;


	public static int getMaximumRowsCountinTablePage() {
		return MaximumRowsCountinTablePage;
	}
	public static void setMaximumRowsCountinTablePage(int maximumRowsCountinTablePage) {
		MaximumRowsCountinTablePage = maximumRowsCountinTablePage;
	}
	public static int getMaximumEntriesinOctreeNode() {
		return MaximumEntriesinOctreeNode;
	}
	public static void setMaximumEntriesinOctreeNode(int maximumEntriesinOctreeNode) {
		MaximumEntriesinOctreeNode = maximumEntriesinOctreeNode;
	}
	
	public void init( ) throws IOException {
		
		tables = new Hashtable();
		
		Properties prop = new Properties();
        String fileName = "src/resources/DBApp.config";
        
        FileInputStream ip = new FileInputStream(fileName);
   
        prop.load(ip);

         this.setMaximumRowsCountinTablePage(Integer.parseInt(prop.getProperty("MaximumRowsCountinTablePage")));
         this.setMaximumEntriesinOctreeNode(Integer.parseInt(prop.getProperty("MaximumEntriesinOctreeNode")));

//         System.out.print(this.getMaximumRowsCountinTablePage() + " ");
//         System.out.print(this.getMaximumEntriesinOctreeNode());

         
         File newd = new File("src/resources");
         if (!newd.exists()) {
             newd.mkdirs();
         }
        
        }


	public void createTable(String strTableName,
	 String strClusteringKeyColumn,
	Hashtable<String,String> htblColNameType,
	Hashtable<String,String> htblColNameMin,
	Hashtable<String,String> htblColNameMax )
	throws DBAppException, IOException, ClassNotFoundException {
	Boolean istrue;
	String filePath = "src/resources/metadata.csv";
    
        if (isTableExists(strTableName))
            throw new DBAppException("Table Already Exists");
	
   	    Table tab = new Table(strTableName,strClusteringKeyColumn,htblColNameType,htblColNameMin,htblColNameMax);
   	    tables.put(strTableName, tab);
   	    
		for (Map.Entry<String, String> entry : htblColNameType.entrySet()) {
			if(strClusteringKeyColumn==entry.getKey()) {
				istrue= true;
			}
			else {
				istrue=false;
			}
            String s=strTableName+", "+entry.getKey()+", "+entry.getValue()+", "+ istrue+", "+ "null"+", "+ "null"+", "+
			String.valueOf(htblColNameMin.get(entry.getKey()))+", "+ String.valueOf(htblColNameMax.get(entry.getKey())) + "\n";
    	    try {
    	        FileWriter writer = new FileWriter(filePath, true);
    	        BufferedWriter bufferz=new BufferedWriter(writer);
    	        bufferz.write(s);
    	        bufferz.flush();
    	        bufferz.close();
    	        writer.close();
    	    } catch (IOException e) {
    	        System.err.println("Error writing metadata to CSV file: " + e.getMessage());
    	    }
    		
        }
        System.out.println("Metadata written to " + filePath);

		serTable(tab);
		serPage(strTableName, tab.getPages().get(0), 1);
//		
			

	}


	public void createIndex(String strTableName,
	String[] strarrColName) throws DBAppException {
		
	}
	// following method inserts one row only.
	// htblColNameValue must include a value for the primary key
	
	public void insertIntoTable(String strTableName,
	 Hashtable<String,Object> htblColNameValue)
	 throws DBAppException, ClassNotFoundException, IOException, ParseException {
		
		if (!isTableExists(strTableName))
            throw new DBAppException("Table " + strTableName + " Does Not Exist");
		
		Table tab = deserTable(strTableName);
		

		String Ckey = tab.getStrClusteringKeyColumn();
	
		
		for(String key: tab.getHtblColNameType().keySet()) {
			if(key.compareTo(Ckey)==0 && (!(htblColNameValue.containsKey(key))))
				throw new DBAppException("Columns Provided Do Not Contain Primary Key");

		}

		Row r = new Row("", htblColNameValue );
		
		boolean t = tab.validate(r);
		//System.out.print(t);
		if(t==true) throw new DBAppException("Invalid Data Types");
		//(tab.getPages()).get(tab.getPagecnt()).getRows().add(r);
		
		for (String colName : htblColNameValue.keySet()) {
            Object value = htblColNameValue.get(colName);
            String colType = String.valueOf(tab.getHtblColNameType().get(colName));
            colType.replace("class ", "");
            Object minString = tab.getHtblColNameMin().get(colName);
            Object maxString = tab.getHtblColNameMax().get(colName);
            
            Object min;
            Object max;
            switch (colType.toLowerCase()) {
            case "java.lang.integer" : min = (Integer)minString;
            max = (Integer)maxString;
            if((Integer)htblColNameValue.get(colName)> (Integer)max || (Integer) htblColNameValue.get(colName)<(Integer)min)
            	throw new DBAppException("Values Not Within Range");
            break;
            case "java.lang.double" : min = (Double)minString;
            max = (Double)maxString;
            max = (Double)maxString;
            if((Double)htblColNameValue.get(colName)> (Double)max || (Double) htblColNameValue.get(colName)<(Double)min)
            	throw new DBAppException("Values Not Within Range");
            break;
            case "java.util.date" : min = (Date)minString;
            max = (Date)minString;
            max = (Integer)maxString;
            if(String.valueOf(htblColNameValue.get(colName)).compareTo(String.valueOf( max))>0 || String.valueOf(htblColNameValue.get(colName)).compareTo(String.valueOf(min))<0)
            	throw new DBAppException("Values Not Within Range");
            break;
            default: min = String.valueOf(minString);
            max = String.valueOf(maxString);
            if(String.valueOf(htblColNameValue.get(colName)).compareTo(String.valueOf(max)) >0 || String.valueOf(htblColNameValue.get(colName)).compareTo(String.valueOf(min)) <0)
            	throw new DBAppException("Values Not Within Range");
            }
            // Check if the column name is valid
            if (!tab.getHtblColNameMin().containsKey(colName)) {
                throw new DBAppException("Column " + colName + " does not exist in table " + strTableName);
            }
            if (!tab.getHtblColNameMax().containsKey(colName)) {
                throw new DBAppException("Column " + colName + " does not exist in table " + strTableName);
            }
            // Check if the column value is null
            if (value == null) {
                throw new DBAppException("Column " + colName + " value cannot be null");
            }
            
           
        }
		
		tab.insert(r);
		
		for(Page p: tab.getPages())
			serPage(strTableName, p, tab.getPages().indexOf(p)+1);
		
	   serTable(tab);


	}
	
	// following method updates one row only
	// htblColNameValue holds the key and new value
	// htblColNameValue will not include clustering key as column name
	// strClusteringKeyValue is the value to look for to find the row to update.
	public void updateTable(String strTableName,
	 String strClusteringKeyValue,
	Hashtable<String,Object> htblColNameValue )
	throws DBAppException, ClassNotFoundException, IOException, ParseException {
		
		
		 if (!isTableExists(strTableName))
	            throw new DBAppException("Table " + strTableName + "Does Not Exist");
		 
		    Table aly = deserTable(strTableName);
		   // System.out.println(aly.getPagesavailable().size());
		    boolean flag = false;
		    
			for (String colName : htblColNameValue.keySet()) {
	            Object value = htblColNameValue.get(colName);
	            String colType = String.valueOf(aly.getHtblColNameType().get(colName));
	            colType.replace("class ", "");
	            Object minString = aly.getHtblColNameMin().get(colName);
	            Object maxString = aly.getHtblColNameMax().get(colName);
	            //System.out.println(colType);
	            Object min;
	            Object max;
	            switch (colType.toLowerCase()) {
	            case "java.lang.integer" : min = (Integer)minString;
	            max = (Integer)maxString;
	            if((Integer)htblColNameValue.get(colName)> (Integer)max || (Integer) htblColNameValue.get(colName)<(Integer)min)
	            	throw new DBAppException("Values Not Within Range");
	            break;
	            case "java.lang.double" : min = (Double)minString;
	            max = (Double)maxString;
	            max = (Double)maxString;
	            if((Double)htblColNameValue.get(colName)> (Double)max || (Double) htblColNameValue.get(colName)<(Double)min)
	            	throw new DBAppException("Values Not Within Range");
	            break;
	            case "java.util.date" : min = (Date)minString;
	            max = (Date)minString;
	            max = (Integer)maxString;
	            if(((String) htblColNameValue.get(colName)).compareTo((String) max)>0 || ((String) htblColNameValue.get(colName)).compareTo((String) min)<0)
	            	throw new DBAppException("Values Not Within Range");
	            break;
	            default: min = String.valueOf(minString);
	            max = String.valueOf(maxString);
	            if(((String) htblColNameValue.get(colName)).compareTo((String) (max)) >0 || ((String) htblColNameValue.get(colName)).compareTo((String) (min)) <0)
	            	throw new DBAppException("Values Not Within Range");
	            }
	            // Check if the column name is valid
	            if (!aly.getHtblColNameMin().containsKey(colName)) {
	                throw new DBAppException("Column " + colName + " does not exist in table " + strTableName);
	            }
	            if (!aly.getHtblColNameMax().containsKey(colName)) {
	                throw new DBAppException("Column " + colName + " does not exist in table " + strTableName);
	            }
	            // Check if the column value is null
	            if (value == null) {
	                throw new DBAppException("Column " + colName + " value cannot be null");
	            }
	            
	           
	        }
		    
		    for (int i = 0; i < aly.getPages().size(); i++) {
		        int pageno = aly.getPages().size();
		        //System.out.println(pageno);
		        Page aya = aly.getPages().get(i);
		        //aya.print();
		        //System.out.println(aya.checkrange(strClusteringKeyValue));
		       if (aya.checkrange(strClusteringKeyValue)) {
		            int pos = aya.binarySearch(strClusteringKeyValue);
		            flag = true; 
		            //System.out.println(flag + " " + pos);
		            aya.updateRow(htblColNameValue, pos);
		            serPage(strTableName, aya, i+1);
		            //aly = deserTable(strTableName);
		       }
		    }
		    serTable(aly);

		       
		    //
		    if (!flag) 
		        throw new DBAppException("You can't update this item");
		    

	 
	}

	public void deleteFromTable(String tableName, Hashtable<String, Object> condition)
	        throws DBAppException, ClassNotFoundException, IOException {
	    // Check if table exists
	    if (!isTableExists(tableName)) {
	        throw new DBAppException("Table " + tableName + " does not exist");
	    }
	    
	   
	    Table table = deserTable(tableName);
	    Vector<Page> pages = table.getPages();
	    
	    
	    ArrayList<Row> rowsToDelete = new ArrayList<>();
	    for (Page page : pages) {
	        Vector<Row> rows = page.getRows();
	        for (Row row : rows) {
	            if (table.matchesConditions(row, condition)) {
	                rowsToDelete.add(row);
	            }
	        }
	    }
	    
	   
	    if (!rowsToDelete.isEmpty()) {
	        for (Row row : rowsToDelete) {
	            table.delete(row);
	        }
	        for (Page page : pages) 
		    	serPage(tableName,page, pages.indexOf(page)+1);
	        serTable(table);
	    } else {
	        throw new DBAppException("No rows matching the given conditions were found in the table");
	    }
	}

	
	public Iterator selectFromTable(SQLTerm[] arrSQLTerms,
	 String[] strarrOperators)
	throws DBAppException{
		return null; //
		
	}
	
	
	private void serTable(Table table) throws IOException {
        
        	String filePath = "src/resources/" + table.getStrTableName() + "Data.ser";
            File file = new File(filePath);

           if(!file.exists())
            file.createNewFile();
            
            FileOutputStream fileOut = new FileOutputStream(filePath);
            
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(table);
            out.close();
            fileOut.close();
            System.out.print("Table " + table.getStrTableName() +" Saved \n");

    }
	
	private void serPage(String tableName, Page p, int pno) throws IOException {
		String filePath = "src/resources/" + tableName +pno + "Data.ser";
        File file = new File(filePath);

       if(!file.exists())
        file.createNewFile();
        
        FileOutputStream fileOut = new FileOutputStream(filePath);
        
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(p);
        out.close();
        fileOut.close();
        System.out.print("Page #" + pno +" Saved \n");

		
	}
	
	private Page deserPage( String tableName, int pno) throws IOException, ClassNotFoundException {
		String filePath = "src/resources/" + tableName + pno +"Data.ser";
        FileInputStream fileIn = new FileInputStream(filePath);
        
        ObjectInputStream in = new ObjectInputStream(fileIn);
        //in.readObject();
        Page p = (Page) in.readObject();
        //System.out.println(tables.containsKey(tableName));

        //tables.put(tableName, tab);
        in.close();
        fileIn.close();
		
		
		return p;
	}
	
	private Table deserTable(String tableName) throws IOException, ClassNotFoundException {
		
		if(tables.containsKey(tableName))
			return tables.get(tableName);
        
		else { String filePath = "src/resources/" + tableName + "Data.ser";

        FileInputStream fileIn = new FileInputStream(filePath);
        
        ObjectInputStream in = new ObjectInputStream(fileIn);
        //in.readObject();
        Table tab = (Table) in.readObject();
        //System.out.println(tables.containsKey(tableName));

        tables.put(tableName, tab);
        in.close();
        fileIn.close();
        System.out.println("Table " + tab.getStrTableName() +" Loaded \n");
        return tab;
		}
}
	
	
    private List<List<String>> readCSV(String tableName) {
    	String filePath = "src/resources/metadata.csv";
        
    	List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(tableName.replace("Over", ""))) {
                    records.add(Arrays.asList(values));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return records;
    }
    
    private boolean isTableExists(String strTableName) {
        
    	List<List<String>> records = readCSV(strTableName);
        for (List<String> record : records)
            if (record.contains(strTableName))
            	return true;

        return false;
    }
    
    public void printTable(String strTableName) throws ClassNotFoundException, IOException {
    	
    	Table tab = deserTable(strTableName);
    	tab.print();
    }
    
   public void clearTable(String strTableName) throws ClassNotFoundException, IOException {
    	
    	Table tab = deserTable(strTableName);
    	tab.clear();
    	serTable(tab);
    }
    

    
//   private boolean isTableExists(String strTableName) throws ClassNotFoundException, IOException {
//	   
//	   Table tab = deserTable(strTableName);
//	   
//        for (String name: this.getTables().keySet())
//            if (name == strTableName)
//            	return true;
//
//        return false;
//    }

	
	public static void main (String[]args) throws DBAppException, IOException, ClassNotFoundException, ParseException {
		
		
		String strTableName = "NewTest";
		DBApp dbApp = new DBApp( );
	    dbApp.init();
		Hashtable htblColNameType = new Hashtable( );
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		
		Hashtable htblColNameMin = new Hashtable( );
		htblColNameMin.put("id", 0);
		htblColNameMin.put("name", "AAAAAAAAAAAAAAA");
		htblColNameMin.put("gpa", 0.1); 

		
		Hashtable htblColNameMax = new Hashtable( );
		htblColNameMax.put("id", 99999);
		htblColNameMax.put("name", "ZZZZZZZZZZZZZZZ");
		htblColNameMax.put("gpa", 4.0); 


//s		dbApp.createTable( strTableName, "id", htblColNameType, htblColNameMin, htblColNameMax);
		
//		Hashtable ht4 = new Hashtable( );
//		ht4.put("id", new Integer( 99 ));
//		ht4.put("name", new String("Dalia" ) );
//		ht4.put("gpa", new Double( 1.25 ) );
//		dbApp.insertIntoTable(strTableName, ht4);
//
////		
//		Hashtable ht5 = new Hashtable( );
//		ht5.put("id", new Integer( 1 ));
//		ht5.put("name", new String("Awel" ) );
//		ht5.put("gpa", new Double( 1.25 ) );
//		dbApp.insertIntoTable(strTableName, ht5);
//
//
////		
//		Hashtable ht6 = new Hashtable( );
//		ht6.put("id", new Integer( 33 ));
//		ht6.put("name", new String("Noor" ) );
//		ht6.put("gpa", new Double( 1.25 ) );
//		dbApp.insertIntoTable(strTableName, ht6);
//
//		Hashtable ht7 = new Hashtable();
//		ht7.put("id", new Integer( 25 ));
//		ht7.put("name", new String("Aya" ) );
//		ht7.put("gpa", new Double( 0.88 ) );
//		dbApp.insertIntoTable(strTableName, ht7);
//		
//		Hashtable h1 = new Hashtable( );
//		h1.put("id", new Integer( 2 ));
//		h1.put("name", new String("Ahmed" ) );
//		h1.put("gpa", new Double( 0.95 ) );
//		dbApp.insertIntoTable(strTableName, h1);

		
		Hashtable<String,Object> delete = new Hashtable();
		delete.put("id", new Integer( 2 ) );
	//	dbApp.deleteFromTable(strTableName, delete);
	
		Hashtable update = new Hashtable( );
		update.put("id", new Integer( 2 ));
		update.put("name", new String("Ahmed" ) );
		update.put("gpa", new Double(2.0 ) );
//		dbApp.updateTable(strTableName, "2", update);
		
		//dbApp.clearTable(strTableName);
		dbApp.printTable(strTableName);
		
	}

	
}
