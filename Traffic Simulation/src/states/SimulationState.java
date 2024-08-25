package states;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import algorithms.Coordinates;
import algorithms.Toolbox;
import display.ImageLoader;
import entities.Car;
import entities.Node;
import entities.Road;
import info.Information;
import info.InformationPanel;
import input.Button;
import input.MouseManager;
import main.Launcher;
import main.Looper;
import traffic.Intersection;
import traffic.TrafficLight;

public class SimulationState extends State {

	public static double zoom = 1.0, simulationSpeed = 2.0;
	public static Coordinates displayCenter = new Coordinates(0, 0);
	
	public static List<Car> cars = new ArrayList<Car>();
	public static List<Node> nodes = new ArrayList<Node>();
	public static List<Road> roads = new ArrayList<Road>();
	public static List<Intersection> intersections = new ArrayList<Intersection>();
	public static List<TrafficLight> trafficLights = new ArrayList<TrafficLight>();

	public static boolean debug = true;
	
	public static BufferedImage map;
	
	public static double time = 0; // 0 = 8:15 AM, each unit is one minute
	
	public static int kidsDelivered = 0;

	public SimulationState(Looper game) {
		super(game);
	}

	@Override
	public void initialize() {
		Node.setupNodes();
		Road.setupRoads();
		Intersection.setUpIntersections();
		Road.initializeRoadData();
		InformationPanel.initializeData();
		TrafficLight.initializeTrafficLights();
		Button.initialize();
	}

	@Override
	public void tick(Graphics2D g) {
		if (simulationSpeed == 0) {
			return;
		}
		Launcher.looper.getKeyManager().tick();
		for (Road road : roads) {
			road.tick();
		}
		for (Node node : nodes) {
			node.tick();
		}
		for (Car car : cars) {
			car.tick();
		}
		for (TrafficLight trafficLight : trafficLights) {
			trafficLight.tick();
		}
		time += 1 / (60.0 * Looper.idealFrameRate) * simulationSpeed;
		
		if (Looper.frameCount % ((int) (30 / Math.sqrt(simulationSpeed))) == 0) Information.tick();
		
		simulationSpeed = InformationPanel.speedSlider.value;
		if (MouseManager.sliderHeld) {
			double min = InformationPanel.speedSlider.minEndpoint.x, max = InformationPanel.speedSlider.maxEndpoint.x;
			double minValue = InformationPanel.speedSlider.min, maxValue = InformationPanel.speedSlider.max;
			double unconstrained = Toolbox.map(Toolbox.mouseX(), min, max, minValue, maxValue);
			InformationPanel.speedSlider.value = Toolbox.constrain(unconstrained, minValue, maxValue);
		}
		
		MouseManager.tick();
	}

	@Override
	public void render(Graphics2D g) {
		double widthToHeight = 1571.0 / 860.0;
		int displacementX = (int) (displayCenter.x * zoom);
		int displacementY = (int) (displayCenter.y * zoom);
		Toolbox.drawCenteredImage(g, map, Launcher.WIDTH / 2 - displacementX, Launcher.HEIGHT / 2 - displacementY, (int) (562 * widthToHeight * zoom), (int) (562 * zoom));
		
		if (debug) {
			for (Node node : nodes) {
				node.render(g);
			}
			for (Road road : roads) {
				road.render(g);
			}
		}
		
		for (TrafficLight trafficLight : trafficLights) {
			trafficLight.render(g);
		}
		
		for (Car car : cars) {
			car.render(g);
		}
		
		InformationPanel.renderInformationPanel(g);
		
		for (Button button : Button.buttons) {
			button.render(g);
		}
	}

	public static void loadImages() {
		map = ImageLoader.loadImage("/textures/Map/Map of IRHS.png");
	}

	public String toString() {
		return "Simulation State";
	}

}