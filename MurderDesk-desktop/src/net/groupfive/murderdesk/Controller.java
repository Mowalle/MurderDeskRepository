package net.groupfive.murderdesk;

import java.awt.EventQueue;
import java.util.Observable;

public class Controller extends Observable {
	
	public void broadcast(String key, Object value, Boolean edt){
		
	}
	
	public void changeData(Object data){
		 setChanged(); // the two methods of Observable class
         notifyObservers(data);
	}
	
    public void changeDataOnEDT(final Object data) {
    	// Notify the observers on the EDT.
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setChanged(); // the two methods of Observable class
                notifyObservers(data);
            }
        });
    }

}
