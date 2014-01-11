package net.groupfive.murderdesk.data;

public class Objective{
	private String description;
	private int reward;
	
	public Objective(String description, int reward){
		this.description = description;
		this.reward = reward;
	}
	
	public String getDescription(){
		return description;
	}
	
	public int getValue(){
		return reward;
	}
}
