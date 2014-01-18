package net.groupfive.murderdesk.tangible;

import java.io.BufferedReader;
import java.io.IOException;
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
import net.groupfive.murderdesk.screens.GameScreen;

public class InterfaceLink implements SerialPortEventListener{
	SerialPort serialPort;
        /** The port we're normally going to use. */
	
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
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		
		String[] split = s.split(":");
		GameScreen screen = ((GameScreen) Main.murderDesk.getScreen());
		
		if(on){
			if(split[0].equals("intensity")){
				
				int intensity = Integer.parseInt(split[1])/10;
				System.out.println(intensity);
				if(screen.getWorld().getCurrentRoom().getCurrentTrap().isActive()){
					screen.getWorld().getCurrentRoom().getCurrentTrap().setIntensity(intensity);
				}
			} else if(split[0].equals("light")){
				screen.getWorldRenderer().setLight(Float.parseFloat(split[1])/100);
			} else{
				switch(s){
				case "t1":
					screen.getWorld().getCurrentRoom().getCurrentTrap().deactivate();
					screen.getWorld().getCurrentRoom().setCurrentTrap(0);
					Main.gui.logToConsole("Trap 1 selected.");
					break;
				case "t2":
					screen.getWorld().getCurrentRoom().getCurrentTrap().deactivate();
					screen.getWorld().getCurrentRoom().setCurrentTrap(1);
					Main.gui.logToConsole("Trap 2 selected.");
					break;
				case "t3":
					screen.getWorld().getCurrentRoom().getCurrentTrap().deactivate();
					screen.getWorld().getCurrentRoom().setCurrentTrap(2);
					Main.gui.logToConsole("Trap 3 selected.");
					break;
				case "ton":
					screen.getWorld().getCurrentRoom().getCurrentTrap().activate();
					Main.gui.logToConsole("Trap activated.");
					break;
				case "toff":
					screen.getWorld().getCurrentRoom().getCurrentTrap().deactivate();
					Main.gui.logToConsole("Trap deactivated.");
					break;
				case "cam1":
					screen.getWorld().setCurrentRoom(0);
					Main.gui.changeRoom(0);
					break;
				case "cam2":
					screen.getWorld().setCurrentRoom(1);
					Main.gui.changeRoom(1);
					break;
				case "cam3":
					screen.getWorld().setCurrentRoom(2);
					Main.gui.changeRoom(2);
					break;
				case "door0":
					screen.getWorld().getCurrentRoom().getDoors().get(0).setOpen(false);
					screen.getWorld().getCurrentRoom().getDoors().get(1).setOpen(false);
					break;
				case "door1":
					screen.getWorld().getCurrentRoom().getDoors().get(0).setOpen(true);
					screen.getWorld().getCurrentRoom().getDoors().get(1).setOpen(false);
					break;
				case "door2":
					screen.getWorld().getCurrentRoom().getDoors().get(0).setOpen(false);
					screen.getWorld().getCurrentRoom().getDoors().get(1).setOpen(true);
					break;
				case "up":
					screen.keyDown(Keys.UP);
					break;
				case "down":
					screen.keyDown(Keys.DOWN);
					break;
				case "left":
					screen.keyDown(Keys.LEFT);
					break;
				case "right":
					screen.keyDown(Keys.RIGHT);
					break;
				case "center":
					screen.keyUp(Keys.UP);
					screen.keyUp(Keys.DOWN);
					screen.keyUp(Keys.LEFT);
					screen.keyUp(Keys.RIGHT);
					break;
				case "boom":
					screen.keyDown(Keys.END);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run () {
							Main.gui.kill();
						}
					});
					break;
				case "shutdown":
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run () {
							Main.gui.shutDown();
						}
					});
				default:
					System.out.println("[interface] Unknown command");
					Main.gui.logToConsole("The interface received an unknown command. Please contact the system administrator.");
					break;
				}
			}
		} else{
			if(s.equals("boot")){
				System.out.println("[interface] Commence booting" );
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run () {
						Main.boot();
					}
				});
				on = true;
			}
		}
	}
}