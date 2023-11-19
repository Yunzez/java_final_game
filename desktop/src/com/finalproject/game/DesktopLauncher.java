package com.finalproject.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	//! "vmArgs": "-XstartOnFirstThread" add this for mac
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Game");
		config.setWindowedMode(1920, 1080);
		// config.setResizable(false); // setResizable method used instead of resizable field
		new Lwjgl3Application(new FinalProjectGame(), config);
	}
}
