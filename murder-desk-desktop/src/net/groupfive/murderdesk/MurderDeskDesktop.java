package net.groupfive.murderdesk;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class MurderDeskDesktop {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Murder Desk";
		cfg.useGL20 = true;
		cfg.width = 640;
		cfg.height = 400;
		
		new LwjglApplication(new MurderDesk(), cfg);
	}
}
