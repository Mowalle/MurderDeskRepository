package net.groupfive.murderdesk.data;

import java.util.ArrayList;

public class Subject{
	private String id;
	private String fName;
	private String lName;
	private String dObtained;
	private String dTermination;
	private ArrayList<String> story;
	
	public Subject(String id, String fName, String lName, String dObtained, String dTermination){
		this.id = id;
		this.fName = fName;
		this.lName = lName;
		this.dObtained = dObtained;
		this.dTermination = dTermination;
		this.story = new ArrayList<String>();
	}
	
	public void appendStory(String s){
		story.add(s);
	}
	
	public String getDObtained(){
		return dObtained;
	}
	
	public String getDTermination(){
		return dTermination;
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
	
	public ArrayList<String> getFullStory(){
		return story;
	}
}
