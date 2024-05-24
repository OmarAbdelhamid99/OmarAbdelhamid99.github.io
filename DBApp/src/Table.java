import java.io.*;
import java.util.*;

public class Table implements Serializable {
	public static final long serialVersionUID = 1L;
	private String strTableName;
	private String strClusteringKeyColumn;
	private Hashtable<String,String> htblColNameType;
	private Hashtable<String,String> htblColNameMin;
	private Hashtable<String,String> htblColNameMax ;
	private Vector<Page> pages;
	public Vector<Integer> pagesavailable = new Vector<Integer>();
	private Date lastUpdated;
	
	

	public Vector<Integer> getPagesavailable() {
		return pagesavailable;
	}

	public void setPagesavailable(Vector<Integer> pagesavailable) {
		this.pagesavailable = pagesavailable;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Table (String strTableName, String strClusteringKeyColumn, Hashtable<String, String> htblColNameType, Hashtable<String,String> htblColNameMin, Hashtable<String,String> htblColNameMax) {
		this.setStrTableName(strTableName);
		this.setPages(new Vector());
		this.getPages().add(new Page());
		this.setHtblColNameMax(htblColNameMax);
		this.setHtblColNameMin(htblColNameMin);
		this.setHtblColNameType(htblColNameType);
		this.setStrClusteringKeyColumn(strClusteringKeyColumn);
	}
	
	public void print() {
		System.out.println("Table " + this.getStrTableName());
		int c = 1;
		for(Page p: this.getPages()) {
//			if(c>0)
			System.out.println("Page #" + c);
			p.print();
			c++;
		}
		
	}
	

	public Vector<Page> getPages() {
		return pages;
	}

	public void setPages(Vector<Page> pages) {
		this.pages = pages;
	}

	
	public String getStrTableName() {
		return strTableName;
	}


	public void setStrTableName(String strTableName) {
		this.strTableName = strTableName;
	}



	public String getStrClusteringKeyColumn() {
		return strClusteringKeyColumn;
	}



	public void setStrClusteringKeyColumn(String strClusteringKeyColumn) {
		this.strClusteringKeyColumn = strClusteringKeyColumn;
	}



	public Hashtable<String, String> getHtblColNameType() {
		return htblColNameType;
	}



	public void setHtblColNameType(Hashtable<String, String> htblColNameType) {
		this.htblColNameType = htblColNameType;
	}



	public Hashtable<String, String> getHtblColNameMin() {
		return htblColNameMin;
	}



	public void setHtblColNameMin(Hashtable<String, String> htblColNameMin) {
		this.htblColNameMin = htblColNameMin;
	}



	public Hashtable<String, String> getHtblColNameMax() {
		return htblColNameMax;
	}



	public void setHtblColNameMax(Hashtable<String, String> htblColNameMax) {
		this.htblColNameMax = htblColNameMax;
	}
	
	public void insert(Row row) throws DBAppException {
		
		//check(row);
		
		
		String val = "";
		
		for(String s: row.getColNameValue().keySet()) {
			if(s.equals(this.getStrClusteringKeyColumn())) {
				val = String.valueOf(row.getColNameValue().get(s));	
				row.setKey(s);
			}
			//System.out.println(String.valueOf(val));
		}
		

		
		if(getPages().get((this.getPages().size())-1).isFull()) {
			this.getPages().add(new Page());	
		}
		
		
		
		this.getPages().get(this.getPages().size()-1).insertRow(row, val, this.getStrClusteringKeyColumn());
	
		for(Page p: this.getPages()) {
			if (!p.isFull() && !p.isEmpty())
				this.getPagesavailable().add(this.getPages().indexOf(p));
			p.setRowIndices();
		}
		
	}
	
//	public String getCKV(Row row) {
//		
//		for(String key: row.getColNameValue().keySet()) {
//			if(key.equals(this.getStrClusteringKeyColumn()))
//				return (String) row.getColNameValue().get(key);
//		}
//		return "";
//	}
	

	public static boolean validate(Row row) {
		int type = 0;
		ArrayList r = new ArrayList();
		for(Object key: row.getColNameValue().values()) {
			String s = String.valueOf(key.getClass());
			s = s.replace("class ", "");
			
			switch(s) {
			
			case("java.lang.String"):
				type = 1;
				break;
			
			case("java.lang.Double"):
				type = 2;
				break;
			
			case("java.lang.Integer"):
				type = 3;
				break;
			
			case("java.lang.Date"):
				type = 4;
				break;
				
			default: 
				type = 0;
			
			}
			r.add(type);
//			System.out.println(r);
//			System.out.println(r.contains(0));
		}
		
		return r.contains(0);
	}
	
	public void deletecond(Hashtable<String, Object> condition) {
		
	    Vector<Page> pages = this.getPages();

		
	    ArrayList<Row> rowsToDelete = new ArrayList<>();
	    for (Page page : pages) {
	        Vector<Row> rows = page.getRows();
	        for (Row row : rows) {
	            if (this.matchesConditions(row, condition)) {
	                rowsToDelete.add(row);
	            }
	        }
	    }
	    
	   
	    if (!rowsToDelete.isEmpty()) 
	        for (Row row : rowsToDelete) 
	            this.delete(row);
	        
	}
	
	
	boolean matchesConditions(Row row, Hashtable<String, Object> condition) {
	    for (String columnName : condition.keySet()) {
	        Object conditionValue = condition.get(columnName);
	        Object rowValue = row.getColNameValue().get(columnName);
	        
	        if (rowValue == null || !rowValue.equals(conditionValue)) {
	            return false;
	        }
	    }
	    
	    return true;
	}
	public void delete(Row row) {
	    Vector<Page> pages = getPages();
	    for (Page page : pages) {
	        page.delete(row);
	        page.setRowIndices();
	        
	    }
	    
	    this.setLastUpdated(new Date());
	}
	
	public void clear() {
		
		this.setPages(new Vector());
		this.getPages().add(new Page());
		
	}

//	
//	public void check(Row row) {
//		
//		row.compareTo(this.getCkeyClass());
//		
//	}
//	

	
//	public String getCkeyClass() {
//		
//		for( String key : this.htblColNameType.keySet()) {
//			if(this.getStrClusteringKeyColumn().compareTo(key)==0) {
//				return String.valueOf(this.htblColNameType.get(key).getClass());
//			}	
//		}
//		return "";
//	}
	
	public static void main(String [] args) throws DBAppException {
		
		Hashtable htblColNameType = new Hashtable( );
		htblColNameType.put("id", "java.lang.Integer");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.double");
		
		Hashtable htblColNameMin = new Hashtable( );
		htblColNameMin.put("id", 0);
		htblColNameMin.put("name", "AAAAA");
		htblColNameMin.put("gpa", 0.1); 

		
		Hashtable htblColNameMax = new Hashtable( );
		htblColNameMax.put("id", 99999);
		htblColNameMax.put("name", "ZZZZZ");
		htblColNameMax.put("gpa", 4.0); 
		
		Table tab = new Table("tab", "id", htblColNameType, htblColNameMin, htblColNameMax);

		

//		//System.out.println(r.getKey());
//		
		Hashtable ht4 = new Hashtable( );
		ht4.put("id", new Integer( 99 ));
		ht4.put("name", new String("Dalia" ) );
		ht4.put("gpa", new Double( 1.25 ) );
		Row s = new Row("id", ht4);
		tab.insert(s);
//		
		Hashtable ht5 = new Hashtable( );
		ht5.put("id", new Integer( 1 ));
		ht5.put("name", new String("Awel wahda" ) );
		ht5.put("gpa", new Double( 1.25 ) );
		Row f = new Row("id", ht5);
		tab.insert(f);
//		
		Hashtable ht6 = new Hashtable( );
		ht6.put("id", new Integer( 33 ));
		ht6.put("name", new String("Noor" ) );
		ht6.put("gpa", new Double( 1.25 ) );
		Row t = new Row("id", ht6);
		tab.insert(t); 
		
		Hashtable ht7 = new Hashtable();
		ht7.put("id", new Integer( 25 ));
		ht7.put("name", new String("Zaky " ) );
		ht7.put("gpa", new Double( 0.88 ) );
		Row h = new Row("id", ht7);
		tab.insert(h);
		
		Hashtable h1 = new Hashtable( );
		h1.put("id", new Integer( 2 ));
		h1.put("name", new String("Ahmed" ) );
		h1.put("gpa", new Double( 0.95 ) );
		Row r = new Row("id", h1);
		tab.insert(r);
		
//		r.print();
//		Table tab = new Table("tab", "id", htblColNameType, htblColNameMin, htblColNameMax);
//		tab.insert(r);
//		tab.print();
		
//		Hashtable<String,Object> delete = new Hashtable();
//		delete.put("id", new Integer( 3333 ) );		
//		
//		tab.deletecond(delete);
//		
//		Hashtable update = new Hashtable( );
//		update.put("id", new Integer( 2 ));
//		update.put("name", new String("Ahmed" ) );
//		update.put("gpa", new Double(2.0 ) );
//		
//		Page p = tab.getPages().get(1);
//		p.print();
//		p.updateRow(update, 0);
		
		tab.print();
		
	}


}
