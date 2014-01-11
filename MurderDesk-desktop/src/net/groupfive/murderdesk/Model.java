package net.groupfive.murderdesk;

import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Model {
	
	private int theData;
	private final Controller c;
	
	public Model(Controller c_){
		// link controller (so it is shared for the whole application)
		this.c = c_;
		// system logic
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		Runnable task = new Runnable() {
		    public void run() {
		        createNumber();
		        c.broadcastOnEDT(new Message("int", "objectives", theData));
		    }
		};
		scheduler.scheduleAtFixedRate(task, 0, 500, TimeUnit.MILLISECONDS);
	}
	
	private void createNumber(){
		theData = (int) Math.round(Math.random() * 99);
	}
	
	public int getData(){
		return theData;
	}
	
	public void addObserver(Observer o){
		c.addObserver(o);
	}
}
