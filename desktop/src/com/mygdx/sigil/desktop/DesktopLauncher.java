package com.mygdx.sigil.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.sigil.SigilGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Sygaldrist";
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new SigilGame(), config);
	}
}
