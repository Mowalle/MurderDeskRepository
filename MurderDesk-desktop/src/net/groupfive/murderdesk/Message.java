package net.groupfive.murderdesk;

/**
 * Defines messages to be used in different parts of the application.
 * @author Teis
 *
 */
public class Message {
	private String type;
	private String key;
	private Object value;
	private Boolean edt;
	
	/**
	 * Creates a message.
	 * @param type Type of value. Accepts "String", "int", "double" and "float"
	 * @param key Name of the value
	 * @param value The value
	 */
	public Message(String type, String key, Object value){
		this(type, key, value, true);
	}
	
	/**
	 * Creates a message.
	 * @param type Type of value. Accepts "String", "int", "double" and "float"
	 * @param key Name of the value
	 * @param value The value
	 * @param edt Send the message on the EDT (for GUI updates) or on the main thread (for other purposes)
	 */
	public Message(String type, String key, Object value, Boolean edt){
		// check the type
		this.type = type;
		this.key = key;
		this.value = value;
		this.edt = edt;
	}
	
	public Double getDouble(){
		try{
			switch(type.toLowerCase()){
				case "double":
					return (Double) value;
				case "float":
					return (Double) value;
				case "int":
					return (Double) value;
				case "string":
					return Double.parseDouble((String) value);
				default:
					return new Double(0);
			}
		} catch(Exception e){
			return new Double(0);
		}
	}
	
	public Float getFloat(){
		try{
			switch(type.toLowerCase()){
				case "double":
					return (Float) value;
				case "float":
					return (Float) value;
				case "int":
					return (Float) value;
				case "string":
					return Float.parseFloat((String) value);
				default:
					return new Float(0);
			}
		} catch(Exception e){
			return new Float(0);
		}
	}
	
	public Integer getInt(){
		try{
			switch(type.toLowerCase()){
				case "double":
					return (Integer) value;
				case "float":
					return (Integer) value;
				case "int":
					return (Integer) value;
				case "string":
					return Integer.parseInt((String) value);
				default:
					return new Integer(0);
			}
		} catch(Exception e){
			return new Integer(0);
		}
	}
	
	public String getString(){
		try{
			switch(type.toLowerCase()){
				case "double":
					return String.valueOf((Double)value);
				case "float":
					return String.valueOf((Float)value);
				case "int":
					return String.valueOf((Integer)value);
				case "string":
					return (String) value;
				default:
					return new String("null");
			}
		} catch(Exception e){
			return new String("null");
		}
	}
	
	public String getType(){
		return type;
	}
	
	public String getKey(){
		return key;
	}
	
	public boolean isEDT(){
		return edt;
	}
	
}
