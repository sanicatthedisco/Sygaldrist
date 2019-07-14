package com.mygdx.sigil.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.sigil.SigilGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		System.setProperty("org.lwjgl.opengl.Display.enableOSXFullscreenModeAPI", "true");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Sygaldrist";
		config.width = 1920;
		config.height = 1080;
		//config.fullscreen = true;
		new LwjglApplication(new SigilGame(), config);
	}
}
