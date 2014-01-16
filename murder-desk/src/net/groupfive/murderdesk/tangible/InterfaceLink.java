package net.groupfive.murderdesk.tangible;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.Enumeration;

import javax.swing.SwingUtilities;

import com.badlogic.gdx.Input.Keys;

import net.groupfive.murderdesk.Main;
import net.groupfive.murderdesk.gdx.model.World;
import net.groupfive.murderdesk.gdx.screens.GameScreen;

public class InterfaceLink implements SerialPortEventListener{
	SerialPort serialPort;
        /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
			"/dev/tty.usbmodemfa121", // Teis's Macbook Pro
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
	};
	
	boolean on = false;
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void initialize() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			if (currPortId.getName().substring(0, 17).equals("/dev/tty.usbmodem")){
				portId = currPortId;
				break;
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				parse(inputLine);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
	private void parse(String s){
		try{
			World world = ((GameScreen) Main.murderDesk.getScreen()).getWorld();
			if(s.equals("t1") && on){
				world.getCurrentRoom().getCurrentTrap().deactivate();
				world.getCurrentRoom().setCurrentTrap(0);
				Main.gui.logToConsole("Trap 1 selected.");
			} else if(s.equals("t2") && on){
				world.getCurrentRoom().getCurrentTrap().deactivate();
				world.getCurrentRoom().setCurrentTrap(1);
				Main.gui.logToConsole("Trap 2 selected.");
			} else if(s.equals("t3") && on){
				world.getCurrentRoom().getCurrentTrap().deactivate();
				world.getCurrentRoom().setCurrentTrap(2);
				Main.gui.logToConsole("Trap 3 selected.");
			} else if(s.equals("ton") && on){
				world.getCurrentRoom().getCurrentTrap().activate();
				Main.gui.logToConsole("Trap activated.");
			} else if(s.equals("toff") && on){
				world.getCurrentRoom().getCurrentTrap().deactivate();
				Main.gui.logToConsole("Trap deactivated.");
			} else if(s.equals("boot") && !on){
				System.out.println("[interface] Commence booting" );
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run () {
						Main.boot();
					}
				});
				System.out.println("[interface] Boot succesfull");
				on = true;
			} else if(s.equals("shutdown") && on){
				on = false;
			//	Main.gui.shutDown();
			} else if(s.equals("boom") && on){
				// TODO Missile switch
			} else if(s.equals("cam1") && on){
				world.setCurrentRoom(0);
			} else if(s.equals("cam2") && on){
				world.setCurrentRoom(1);
			} else if(s.equals("cam3") && on){
				world.setCurrentRoom(2);
			} else if(s.equals("door0") && on){
				world.getCurrentRoom().getDoors().get(0).setOpened(false);
				world.getCurrentRoom().getDoors().get(1).setOpened(false);
			} else if(s.equals("door1") && on){
				world.getCurrentRoom().getDoors().get(0).setOpened(true);
				world.getCurrentRoom().getDoors().get(1).setOpened(false);
			} else if(s.equals("door2") && on){
				world.getCurrentRoom().getDoors().get(0).setOpened(false);
				world.getCurrentRoom().getDoors().get(1).setOpened(true);
			} else if(s.equals("up") && on){
				((GameScreen)Main.murderDesk.getScreen()).keyDown(Keys.UP);
			} else if(s.equals("down") && on){
				((GameScreen)Main.murderDesk.getScreen()).keyDown(Keys.DOWN);
			} else if(s.equals("left") && on){
				((GameScreen)Main.murderDesk.getScreen()).keyDown(Keys.LEFT);
			} else if(s.equals("right") && on){
				((GameScreen)Main.murderDesk.getScreen()).keyDown(Keys.RIGHT);
			} else if(s.equals("center") && on){
				((GameScreen)Main.murderDesk.getScreen()).keyUp(Keys.UP);
				((GameScreen)Main.murderDesk.getScreen()).keyUp(Keys.DOWN);
				((GameScreen)Main.murderDesk.getScreen()).keyUp(Keys.LEFT);
				((GameScreen)Main.murderDesk.getScreen()).keyUp(Keys.RIGHT);
			} else if((s.split(":")[0]).equals("intensity")){
				// TODO set intensity
			} else if((s.split(":")[0]).equals("light")){
				// TODO set light
			}
			else{
				if(on){
					System.out.println("[interface] Unknown command");
					Main.gui.logToConsole("The interface received an unknown command. Please contact the system administrator.");
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}