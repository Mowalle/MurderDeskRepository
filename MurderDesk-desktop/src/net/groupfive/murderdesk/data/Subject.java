package net.groupfive.murderdesk.data;

import java.util.ArrayList;

public class Subject{
	private String id;
	private String fName;
	private String lName;
	private ArrayList<String> story;
	
	public Subject(String id, String fName, String lName){
		this.id = id;
		this.fName = fName;
		this.lName = lName;
		this.story = new ArrayList<String>();
	}
	
	public void appendStory(String s){
		story.add(s);
	}
	
	public String getId(){
		return id;
	}
	
	public String getFirstName(){
		return fName;
	}
	
	public String getName(){
		return lName;
	}
	
	public String getStory(int i){
		return story.get(i);
	}
	
	public ArrayList<String> getFullStory(int i){
		return story;
	}
}
