package net.groupfive.murderdesk.data;

public class Room{
	private int id;
	private String name;
	private String[] traps;
	
	public Room(int id, String name){
		this.id = id;
		this.name = name;
		this.traps = new String[3];
	}
	
	public void addTrap(int id,String s){
		this.traps[id] = s;
	}
	
	public void addTraps(String s[]){
		this.traps = s;
	}
	
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getTrap(int i){
		return traps[i];
	}
	
	public String[] getTraps(){
		return traps;
	}
	
}
