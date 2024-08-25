package info;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import algorithms.Coordinates;
import algorithms.Toolbox;
import main.Launcher;
import states.SimulationState;

public class InformationPanel {

	public static List<DataPoint>[] carsData = new List[2];
	public static List<DataPoint>[] carbonData = new List[1];
	public static List<DataPoint>[] kidsData = new List[1];
	public static Graph carsGraph = new Graph(new Coordinates(Launcher.WIDTH + 30, 60 + Graph.GRAPH_HEIGHT), carsData);
	public static Graph carbonGraph = new Graph(new Coordinates(Launcher.WIDTH + 30, 130 + Graph.GRAPH_HEIGHT * 2), carbonData);
	public static Graph kidsGraph = new Graph(new Coordinates(Launcher.WIDTH + 30, 200 + Graph.GRAPH_HEIGHT * 3), kidsData);
	
	public static Slider speedSlider = new Slider(0.001, 50, new Coordinates(Launcher.WIDTH + 12, Launcher.HEIGHT - 12), new Coordinates(Launcher.WIDTH + 200, Launcher.HEIGHT - 12));
	
	public static void initializeData() {
		carsData[0] = Information.totalCarsData;
		carsData[1] = Information.stationaryCarsData;
		carbonData[0] = Information.carbonEmissionsData;
		kidsData[0] = Information.kidsDeliveredData;
	}
	
	public static void renderInformationPanel(Graphics2D g) {
		// Background
		g.setColor(Color.WHITE);
		g.fillRect(Launcher.WIDTH, 0, 700, 1400);
		
		// Time
		renderTime(g);
		
		// Car Info
		renderCarInfo(g);
		
		// Carbon Info
		renderCarbonInfo(g);
		
		// Kids Delivered Info
		renderKidsInfo(g);
	}
	
	public static void renderTime(Graphics2D g) {
		String timeString = Toolbox.timeString();
		Toolbox.setFontSize(g, 15);
		Toolbox.setAlign(Toolbox.ALIGN_RIGHT, Toolbox.ALIGN_BOTTOM);
		g.setColor(Color.BLACK);
		Toolbox.drawText(g, timeString, Launcher.FULL_WIDTH - 5, Launcher.HEIGHT - 5);
		Toolbox.setFontSize(g, 12);
		Toolbox.setAlign(Toolbox.ALIGN_LEFT, Toolbox.ALIGN_BOTTOM);
		Toolbox.drawText(g, Toolbox.formatDecimal(SimulationState.simulationSpeed, 2) + "x Speed", Launcher.WIDTH + 5, Launcher.HEIGHT - 25);
		speedSlider.render(g);
	}
	
	public static void renderCarInfo(Graphics2D g) {
		int totalCars = Information.totalCars(), stationaryCars = Information.stationaryCars();
		Toolbox.setFontSize(g, 15);
		Toolbox.setAlign(Toolbox.ALIGN_LEFT, Toolbox.ALIGN_TOP);
		g.setColor(Color.BLACK);
		Toolbox.drawText(g, "Number of Cars: " + totalCars, Launcher.WIDTH + 10, 10);
		Toolbox.drawText(g, "Number of Stationary Cars: " + stationaryCars, Launcher.WIDTH + 10, 30);
		carsGraph.render(g);
	}
	
	public static void renderCarbonInfo(Graphics2D g) {
		double emissionRate = Information.emissionRate();
		Toolbox.setFontSize(g, 15);
		Toolbox.setAlign(Toolbox.ALIGN_LEFT, Toolbox.ALIGN_TOP);
		g.setColor(Color.BLACK);
		Toolbox.drawText(g, "CO2 Emission Rate: " + Toolbox.formatDecimal(emissionRate, 2) + " kg/min", Launcher.WIDTH + 10, 100 + Graph.GRAPH_HEIGHT);
		carbonGraph.render(g);
	}
	
	public static void renderKidsInfo(Graphics2D g) {
		Toolbox.setFontSize(g, 15);
		Toolbox.setAlign(Toolbox.ALIGN_LEFT, Toolbox.ALIGN_TOP);
		g.setColor(Color.BLACK);
		Toolbox.drawText(g, "Kids Delivered: " + SimulationState.kidsDelivered, Launcher.WIDTH + 10, 170 + Graph.GRAPH_HEIGHT * 2);
		//  + " (Average " + Toolbox.formatDecimal(Information.averageDeliveryRate(), 2) + " kids/min)"
		kidsGraph.render(g);
	}
	
}
