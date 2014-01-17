package net.groupfive.murderdesk.gui;

public class Objective{
	private String description;
	private String title;
	private int reward;
	private boolean completed;
	
	public Objective(String title, String description, int reward){
		this.description = description;
		this.reward = reward;
		this.title = title;
	}
	
	public String getDescription(){
		return description;
	}
	
	public int getValue(){
		return reward;
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setCompleted(boolean completed){
		this.completed = completed;
	}
	
	public boolean getCompleted(){
		return completed;
	}
}
