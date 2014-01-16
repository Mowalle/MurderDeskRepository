package net.groupfive.murderdesk;

import java.util.ArrayList;

import net.groupfive.murderdesk.data.Objective;
import net.groupfive.murderdesk.data.Room;
import net.groupfive.murderdesk.data.Subject;

public final class DataController {
		
	private ArrayList<Room> rooms;
	private ArrayList<Subject> subjects;
	private ArrayList<Objective> objectives;
	
	public int CURRENT_ROOM = 0;
	public int CURRENT_SUBJECT = 0;
	public int CURRENT_OBJECTIVE = 0;

	
	
	// data
	
	public DataController(){
		rooms = new ArrayList<Room>();
		subjects = new ArrayList<Subject>();
		objectives = new ArrayList<Objective>();
		setData();
	}
	
	private void setData(){
		/*
		 * ROOMS
		 */
		String[] traps = new String[3];
		
		//0
		Room r1 = new Room(1, "Basement");
		traps = new String[] {"Open trap door", "Flood the room", "Unavailable"};
		r1.addTraps(traps);
		rooms.add(r1);
		
		//1
		Room r2 = new Room(2, "Sleeping room");
		traps = new String[] {"asdf 2.1", "asdf 2.2", "asdf 2.3"};
		r2.addTraps(traps);
		rooms.add(r2);
		
		/*
		 * PERSONAS
		 */
		//0
		Subject p1 = new Subject("140113-hifi", "Micheal \"Mike\"", "Johnson", "19 october 2012", "13 january 2014");
		p1.appendStory("This is Mike. Mike is a drug dealer. Not a biggy, but still he is doing his job good.");
		p1.appendStory("Beneath drug dealing he had some police records going on. He stole, fought and almost killed.");
		p1.appendStory("He is the stereotypical guy who just cause problems and cannot control his anger so he always get into bizarre, dangerous situations");
		p1.appendStory("He does not really think of the consequences that can happen with his actions.");
		p1.appendStory("No one really knows why he is stealing and fighting.");
		p1.appendStory("I guess it is one of the cases where he does not know what else to do with his life.");
		p1.appendStory(" ");
		p1.appendStory("But that is not true. In fact, he is loving and caring for his only child.");
		p1.appendStory("He steals money in order to buy his son books.");
		p1.appendStory("He fights because he does not want his kid to hang out with this kind of people.");
		p1.appendStory("He wants for his child to have a better life.");
		p1.appendStory("As said before he does not think very much of the consequences that can occur with this behaviour.");
		
		subjects.add(p1);
		
		/*
		 * OBJECTIVES
		 */
		objectives.add(new Objective("Let stones fall from above to scare the subject. Bring the pulse to 130", 500));
		objectives.add(new Objective("Find a way to nearly kill the subject and bring his pulse at least to 140", 3000));
		objectives.add(new Objective("Try to make the subject run around fast in the room. Use light (+2000) or sound.", 1500));
		objectives.add(new Objective("Play with the subject! Imprison the subject in the room! Close the door when the subject is close to it.", 200));
		
	}
	
	public Objective getObjective(int i){
		return objectives.get(i);
	}
	
	public ArrayList<Objective> getObjectives(){
		return objectives;
	}
	
	public Room getRoom(int i){
		return rooms.get(i);
	}
	
	public ArrayList<Room> getRooms(){
		return rooms;
	}
	
	public Subject getSubject(int i){
		return subjects.get(i);
	}
	
	public ArrayList<Subject> getSubjects(){
		return subjects;
	}
	
	public void log(){
		//System.out.println("Root element :" + doc.getDocumentElement());
	}
	
}
