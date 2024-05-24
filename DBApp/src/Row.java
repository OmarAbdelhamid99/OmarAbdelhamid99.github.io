
import java.io.*;
import java.util.*;

public class Row implements Serializable, Comparable {
	private String key;
	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Object getVal(String cs) {

		return this.getColNameValue().get(cs);
	}

	public Hashtable<String, Object> getHtblcolNameValue() {
		return htblcolNameValue;
	}

	public void setHtblcolNameValue(Hashtable<String, Object> htblcolNameValue) {
		this.htblcolNameValue = htblcolNameValue;
	}

	private Hashtable<String, Object> htblcolNameValue;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Row(String key, Hashtable<String, Object> htblcolNameValue) {
		this.setColNameValue(htblcolNameValue);
		this.setKey(key);
	}

	public Hashtable<String, Object> getColNameValue() {
		return htblcolNameValue;
	}

	public void setColNameValue(Hashtable<String, Object> colNameValue) {
		this.htblcolNameValue = colNameValue;
	}

//@Override
//	public int compareTo(Object o) {
//	
//	String t = (String)o;
//	
//	for(Object key: this.getColNameValue().values()) {
//		String s = String.valueOf(key.getClass());
//		s = s.replace("class ", "");
//		
//		switch(s) {
//		
//		case("java.lang.String"):
//			
//			break;
//		
//		case("java.lang.Double"):
//
//			break;
//		
//		case("java.lang.Integer"):
//
//			break;
//		
//		case("java.lang.Date"):
//
//			break;
//		
//		default: 
//		
//		}
//	
//	
//	return 0;
//		
//	
//		
//	}
//	return 0;
//
//}

	public void print() {
		System.out.println(this.getIndex() + "\t" + this.getColNameValue().toString());
	}

	public int compareTo(Object o) {

		String type = String.valueOf(this.getColNameValue().get(this.getKey()).getClass());
		type = type.replace("class ", "");
		switch (type.toLowerCase()) {
		case "java.lang.integer":
			return ((Integer) Integer.parseInt(this.getColNameValue().get(this.getKey()) + ""))
					.compareTo(Integer.parseInt(o + ""));
		case "java.lang.double":
			return ((Double) Double.parseDouble(this.getColNameValue().get(this.getKey()) + ""))
					.compareTo(Double.parseDouble(o + ""));
		case "java.util.date":
			Date date1 = (Date) this.getColNameValue().get(this.getKey());
			Date date2 = (Date) o;
			return date1.compareTo(date2);
		case "java.lang.string":
			return ((String) this.getColNameValue().get(this.getKey())).compareTo((String) o);
		default:
			return 0;
		}
	}

}
