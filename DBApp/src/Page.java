
import java.io.*;
import java.util.*;

public class Page implements Serializable {
	private Vector<Row> rows;
	private Date lastUpdated;
	
	public Page() {
		this.setRows(new Vector());

	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}


	public boolean isFull() {
		if (this.getRows().size() == DBApp.getMaximumRowsCountinTablePage())
			return true;
		else return false;
	}
	
	public boolean isEmpty() {
		return this.getRows().size()==0;
	}
	
	
	public Vector<Row> getRows() {
		return rows;
	}

	public void setRows(Vector vector) {
		this.rows = vector;
	}

	public int binaryInsSort(String clusteringKeyValue, String cs) throws DBAppException {
	       
		if (this.isEmpty())
	            return 0;
		
		int left = 0;
        int right = this.getRows().size() - 1;
//        System.out.println(this.getRows().size());
//        System.out.println(this.getRows().get(0).getKey());

	
        while (left<=right) {
          int mid = left + (right - left) / 2;
          int prv = mid -1;
          if(mid==0)
        	  prv=0;
          
          String n = String.valueOf(this.getRows().get(mid).getVal(cs));
          String m = String.valueOf(this.getRows().get(prv).getVal(cs));
          int cmp = n.compareTo(clusteringKeyValue);
          int cmp2 = m.compareTo(clusteringKeyValue);
          
          //System.out.println(cmp + "\t" + cmp2 + "\t" + n + "\t" + m  + "\t" + clusteringKeyValue + "\t" + cs);
          if (cmp == 0) {
        	  throw new DBAppException("Cannot Insert Duplicate Primary Key Value");
          } else if (cmp < 0) {
        	  return mid+1;
//        		  
          } else if (cmp> 0){
        	  if(cmp2<0)
    		  return mid;
    	    return prv;
        	   
          }
        }
		return -1;
		  
      }
	
	public boolean checkrange(String value) {
		
		if(this.isEmpty())
			return false;
		
    	if(this.getRows().get(0).compareTo(value)<=0&&this.getRows().get((this.getRows().size())-1).compareTo(value)>=0) 
    		return true;	
    	else 
    		return false;
    }
	

	
	public int binarySearch(String clusteringKeyValue) {
		if (this.isEmpty())
            return 0;
        int left = 0;
        int right = this.getRows().size() - 1;
        while (left <= right) {
          int mid = left + (right - left) / 2;
          int cmp = this.getRows().get(mid).compareTo(clusteringKeyValue);
          if (cmp == 0) {
            return mid;
          } else if (cmp < 0) {
            left = mid + 1;
          } else {
            right = mid - 1;
          }
        }
        return -1;
      }
	
    public void updateRow(Hashtable<String,Object> htblColNameValue, int poss) {
    	Row khaled = this.getRows().get(poss);
    	Enumeration<String> colnames= htblColNameValue.keys();
    	while (colnames.hasMoreElements()) {
    		String colName = colnames.nextElement();
    		khaled.getColNameValue().put(colName, htblColNameValue.get(colName));
    	}
    }
    
    public void setRowIndices() {
    	
    	for(Row row: this.getRows())
    	row.setIndex(this.getRows().indexOf(row));
    	
    }
	
	public void insertRow(Row row, String cKeyVal, String cs) throws DBAppException {
	
//		boolean t = this.checkrange(cKeyVal);
//		if(!t) throw new DBAppException("Values Outside Column Range");
//		row.setKey(String.valueOf(this.getRows().size()-1));
		//System.out.println(pos);
		//this.getRows().add(row);
		//Collections.sort(this.getRows());
		
		if(this.isEmpty()) {
			this.getRows().add(row);
			row.setIndex(this.getRows().indexOf(row));
			return;
		}
			
		
		int pos = binaryInsSort(cKeyVal, cs);
		this.getRows().add(pos, row);
		this.setRowIndices();
		

		}
	
	public void print() {
		
		for(Row r: this.getRows())
			r.print();
		
	}

	
	public void delete(Row row) {
	    int index = this.getRows().indexOf(row);
	    if (index != -1) {
	        this.rows.remove(index);
	        this.lastUpdated = new Date();
	    }
	}

	public static void main(String [] args) throws DBAppException {
		
		Page p = new Page();
		Hashtable htblColNameValue = new Hashtable( );
		htblColNameValue.put("id", new Integer( 2343432 ));
		htblColNameValue.put("name", new String("Ahmed Noor" ) );
		htblColNameValue.put("gpa", new Double(5 ) );
		
		Hashtable ht2 = new Hashtable( );
		ht2.put("id", new Integer( 11111 ));
		ht2.put("name", new String("Ahmed Noor" ) );
		ht2.put("gpa", new Double(5 ) );
		
		Row r = new Row("id", htblColNameValue);
		Row r2 = new Row("id", ht2);

		p.getRows().add(r);
		p.getRows().add(r2);
		p.print();
		
	}

	
}
