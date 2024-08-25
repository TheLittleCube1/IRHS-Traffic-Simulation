package main;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import input.MouseManager;
import states.SettingState;
import states.SimulationState;

public class Launcher {
	
	public static final int WIDTH = 1000, HEIGHT = (int) (WIDTH * 0.5625), BAND_WIDTH = 450, FULL_WIDTH = WIDTH + BAND_WIDTH;
	public static Looper looper;
	public static SimulationState simulationState;
	public static SettingState settingState;
	public static Font gameFont;
	
	public static MouseManager mouseManager;

	public static void main(String[] args) {
		
		SimulationState.loadImages();
		
		looper = new Looper("Traffic", FULL_WIDTH, HEIGHT);
		looper.start();
		simulationState = looper.simulationState;
		settingState = looper.settingState;
		
		try {
			gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("FOT-Rodin Pro DB.otf")).deriveFont(30f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("FOT-Rodin Pro DB.otf")).deriveFont(30f));
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
		
	}

}