package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.AbstractQueuedLongSynchronizer;

import algorithms.Coordinates;
import algorithms.Toolbox;
import states.SimulationState;

public class Node {
	
	public Coordinates location;
	public int index;
	public Car queuedCar = null;
	
	public double defaultInputPerMinute = 0, inputPerMinute = 0;
	public boolean isInputNode = false;
	
	public List<Road> nextRoads = new ArrayList<Road>();
	
	public Node(double x, double y) {
		location = new Coordinates(x, y);
		SimulationState.nodes.add(this);
		index = indexCounter;
		indexCounter++;
	}
	
	public Node(double x, double y, double inputPerMinute) {
		location = new Coordinates(x, y);
		SimulationState.nodes.add(this);
		this.defaultInputPerMinute = inputPerMinute;
		this.inputPerMinute = inputPerMinute;
		isInputNode = true;
		index = indexCounter;
		indexCounter++;
	}
	
	public String toString() {
		return "[Node " + index + "]";
	}
	
	public void tick() {
		inputPerMinute = inputRate();
	}
	
	public void render(Graphics2D g) {
		Toolbox.drawCoordinates(g, location, Color.GREEN);
		Toolbox.setAlign(Toolbox.ALIGN_CENTER, Toolbox.ALIGN_CENTER);
		g.setColor(Color.RED);
		Toolbox.setFontSize(g, (int) (4 * (1 + (SimulationState.zoom - 1) * 0.6)));
		Coordinates screen = Toolbox.coordinatesToScreen(location);
		Toolbox.drawText(g, "" + index, (int) screen.x, (int) screen.y);
	}
	
	public double inputRate() {
		if (inputPerMinute == 0) return 0;
		
		double calmnessFactor = 0.25;
		
		double result;
		
		if (SimulationState.time < 9) {
			result = Toolbox.map(SimulationState.time, 0, 9, defaultInputPerMinute * calmnessFactor, defaultInputPerMinute);
		} else if (SimulationState.time < 12) {
			result = defaultInputPerMinute;
		} else {
			result = Toolbox.constrain(Toolbox.map(SimulationState.time, 12, 15, defaultInputPerMinute, defaultInputPerMinute * calmnessFactor), defaultInputPerMinute * calmnessFactor, defaultInputPerMinute);
		}
		
		if (index == 19 || index == 27 || index == 30) {
			List<Car> queue = SimulationState.roads.get(2).queue;
			if (queue.size() > 15) {
				return result * 0.4;
			} else if (queue.size() > 22) {
				return result * 0.2;
			}
		}
		
		return result;
	}
	
	public static int indexCounter = 0;
	public static void setupNodes() {
		double defaultInput = 6.0; // 9.0 is real world
		new Node(-513.0, 216.0); // Node 0
		new Node(-425.0, 125.0); // Node 1
		new Node(-410.8684873483799, 110.97977109517801); // Node 2
		new Node(-109.45701913373254, -198.55510429904373); // Node 3
		new Node(-113.39958722559311, -202.69756821975636); // Node 4
		new Node(-109.61159347820674, -222.64766862265785); // Node 5
		new Node(-105.57106681432796, -227.19326111952148); // Node 6
		new Node(-89.74266961997671, -227.04376527741854); // Node 7
		new Node(-85.8734993279189, -222.39513570616543); // Node 8
		new Node(-86.55763199217608, -202.04928906572604); // Node 9
		new Node(-90.3, -198.3); // Node 10
		new Node(-154.67091793598306, -280.5050552026865); // Node 11
		new Node(-149.47394302797454, -280.5050552026865, defaultInput); // Node 12
		new Node(-36.234595032420486, -280.5050552026865); // Node 13
		new Node(-28.028845177670206, -280.5050552026865, defaultInput); // Node 14
		new Node(-414.74522175999874, 106.91182251203941); // Node 15
		new Node(-28.22912005814853, -131.00799924858646); // Node 16
		new Node(-32.429120058148534, -127.20799924858646); // Node 17
		new Node(-428.78047313228296, 121.6528371007416); // Node 18
		new Node(-513.2127126346926, 209.04289391056048, defaultInput); // Node 19
		new Node(-426.9611308134278, 109.95158112843691); // Node 20
		new Node(-423.8589612849578, 107.32666845050072); // Node 21
		new Node(-459.3468467825834, 70.27247488461289); // Node 22
		new Node(-455.74684678258336, 66.8724748846129); // Node 23
		new Node(-474.1468467825834, 66.4724748846129); // Node 24
		new Node(-476.1468467825834, 61.072474884612895); // Node 25
		new Node(-513.5468467825834, 84.8724748846129); // Node 26
		new Node(-513.3468467825834, 79.27247488461289, defaultInput); // Node 27
		new Node(-414.50037828802584, 126.11630768273729); // Node 28
		new Node(-411.1003782880258, 122.71630768273728); // Node 29
		new Node(-288.80917944889484, 281.1820144479923, defaultInput); // Node 30
		new Node(-282.528610556252, 280.96544310686664); // Node 31
		new Node(69.64288933342984, -60.95917400000454); // Node 32
		new Node(72.69421778210933, -65.20450053729775); // Node 33
		new Node(82.11353603672865, -66.53116508020187); // Node 34
		new Node(86.09352966544104, -63.21450372294156); // Node 35
		new Node(87.95086002550681, -53.66251901403183); // Node 36
		new Node(83.57286703392319, -48.488527296705726); // Node 37
		new Node(72.82688423639975, -47.69252857096325); // Node 38
		new Node(68.31622479052571, -51.141856382513986); // Node 39
		new Node(41.84302859448648, -14.336503260144447); // Node 40
		new Node(38.00052060754495, -18.314845228379742); // Node 41
		new Node(41.11029709094126, -8.621197532491724); // Node 42
		new Node(37.00700067108802, -4.517901112638491); // Node 43
		new Node(112.7079439970443, 80.59663894260717); // Node 44
		new Node(116.3329439970443, 77.59663894260717); // Node 45
		new Node(112.2079439970443, 90.84663894260717); // Node 46
		new Node(115.4579439970443, 95.22163894260717); // Node 47
		new Node(92.87285747498731, 111.09773139783103); // Node 48
		new Node(95.98110451302614, 115.09404901816667); // Node 49
		new Node(97.4612221501875, 119.38639016593459); // Node 50
		new Node(95.24104569444548, 123.53071954998636); // Node 51
		new Node(133.42808073320825, 141.88417825078707); // Node 52
		new Node(134.61217484293732, 136.70376652072235); // Node 53
		new Node(130.65387136055605, -104.88081403823769); // Node 54
		new Node(127.17742361329753, -109.8471679628927); // Node 55
		new Node(173.8419288851353, 56.97654864077782); // Node 56
		new Node(178.2755505987621, 51.535285628599496); // Node 57
		new Node(171.64013693416805, 76.45599766353472); // Node 58
		new Node(176.19770261344095, 82.41589124412236); // Node 59
		new Node(194.9538382935256, 81.71472729346499); // Node 60
		new Node(200.56314989878456, 76.80657963886341); // Node 61
		new Node(145.0011415298465, 130.73628700748844); // Node 62
		new Node(151.60944447329913, 131.74189832497038); // Node 63
		new Node(156.35018354142818, 146.25143304863812); // Node 64
		new Node(158.5050649360323, 140.50508266302714); // Node 65
		new Node(135.49412579490988, 151.8603324489599); // Node 66
		new Node(141.69029550240475, 155.15204760606656); // Node 67
		new Node(247.498522102263, 183.39163613075735); // Node 68
		new Node(243.873522102263, 175.76663613075735); // Node 69
		new Node(253.70062730195036, 154.9653658492954); // Node 70
		new Node(261.4844339846213, 147.18155916662445); // Node 71
		new Node(285.1241431690294, 155.8302332584811); // Node 72
		new Node(278.7817821683346, 148.91129398499578); // Node 73
		new Node(277.05204734996323, 183.5059903524223); // Node 74
		new Node(284.2592757598438, 175.72218366975133); // Node 75
		new Node(513.3502568416989, -74.75459240919082, defaultInput); // Node 76
		new Node(513.3502568416989, -84.5330912002301); // Node 77
		new Node(339.3163380646016, 239.99946387333955); // Node 78
		new Node(344.80709864374694, 229.38399342032523); // Node 79
		new Node(434.1234707311776, 280.63109215901494, defaultInput); // Node 80
		new Node(453.89020881610077, 281.36319356956767); // Node 81
		new Node(-21.481739952449942, 41.09491820803236); // Node 82
		new Node(-17.744977869978793, 45.869669757856606); // Node 83
		new Node(-16.291792615684457, 52.92799813585766); // Node 84
		new Node(-20.858946272038082, 56.66476021832881); // Node 85
		new Node(3.8150303075381515, 84.83289889204073); // Node 86
		new Node(6.72140081612682, 79.43535366180463); // Node 87
		new Node(80.83384878513792, 116.59537659304547); // Node 88
		new Node(83.11742561331472, 111.82062504322124); // Node 89
		new Node(78.78868234822951, 124.35018242416288); // Node 90
		new Node(82.79499707925984, 127.91135107396761); // Node 91
		new Node(36.571924107958054, 166.44892982224516); // Node 92
		new Node(40.60797201749115, 170.19668859538302); // Node 93
		new Node(42.049417699467256, 177.40391700526354); // Node 94
		new Node(37.72508065353894, 182.88141059677275); // Node 95
		new Node(60.49992242876138, 201.04362618967167); // Node 96
		new Node(63.959392065504034, 195.27784346176725); // Node 97
		new Node(124.21182157210518, 177.69220614165877); // Node 98
		new Node(129.40102602721916, 180.57509750561098); // Node 99
		new Node(29.002952244179653, -2.5537190655252524); // Node 100
		new Node(25.35478215057215, -5.65466364509163); // Node 101
		new Node(279.23088201499297, -138.29801491103817); // Node 102
		new Node(273.1156506220411, -147.47086200046607); // Node 103
		new Node(412.23716481169754, -279.7127408730516); // Node 104
		new Node(426.7608393699584, -282.00595264540857, defaultInput); // Node 105
		new Node(255.89649163935087, 177.3404870733065); // Node 106
		new Node(250.89649163935087, 171.2154870733065); // Node 107
	}

	public static final double[] EMPTY = {  }, ONE = { 1.0 }, TWO = { 0.5, 0.5 }, THREE = { 0.333, 0.333, 0.334 };
	public double[] probabilityBranches() {
		
		if (nextRoads.size() == 0) return EMPTY;
		else if (nextRoads.size() == 1) return ONE;
		
		if (index == 2 || index == 10) {
			return THREE;
		} else if (index == 4 || index == 6 || index == 8) {
			double entry = skewed(0.333, 0.6);
			return new double[] { (1.0 - entry) * 0.5, (1.0 - entry) * 0.5, entry };
		} else if (index == 21 || index == 28) {
			double entry = skewed(0.333, 0.6);
			return new double[] { (1.0 - entry) * 0.5, entry, (1.0 - entry) * 0.5 };
		} else if (index == 18) {
			double entry = skewed(0.4, 0.6);
			return new double[] { (1.0 - entry) * 0.25, entry, (1.0 - entry) * 0.75 };
		} else if (index == 33 || index == 35) {
			double entry = skewed(0.333, 0.4);
			return new double[] { 1.0 - entry * 2, entry * 0.7, entry * 1.3 };
		} else if (index == 57 || index == 60) {
			double entry = skewed(0.4, 0.6);
			return new double[] { entry, 1.0 - entry };
		} else if (index == 37) {
			return new double[] { 0.0, 0.5, 0.5 };
		} else if (index == 39 || index == 74) {
			return new double[] { 0.4, 0.4, 0.2 };
		} else if (index == 40) {
			return new double[] { 1.0 };
		} else if (index == 43 || index == 49 || index == 101) {
			return new double[] { 0.0, 1.0 };
		} else if (index == 51 || index == 53 || index == 63) {
			return new double[] { 0.0, 0.0, 1.0 };
		} else if (index == 58) {
			return new double[] { 0.65, 0.35 };
		} else if (index == 90) {
			return new double[] { 0.25, 0.00, 0.75 };
		} else if (index == 64) {
			return TWO;
		} else if (index == 66) {
			return new double[] { 0.6, 0.1, 0.3 };
		} else if (index == 71) {
			return new double[] { 0.1, 0.45, 0.45 };
		} else if (index == 72) {
			return new double[] { 0.4, 0.2, 0.4 };
		} else if (index == 89) {
			return new double[] { 0.2, 0.8 };
		} else if (index == 99) {
			if (!SimulationState.roads.get(62).queue.isEmpty()) {
				return new double[] { 1.0, 0.0 }; // take long way
			} else if (SimulationState.roads.get(43).minT() < Car.safeDistance) {
				return new double[] { 1.0, 0.0 };
			} else {
				return TWO;
			}
		}
		
		switch (nextRoads.size()) {
			case 0: return EMPTY;
			case 1: return ONE;
			case 2: return TWO;
			case 3: return THREE;
			default: return EMPTY;
		}
		
	}
	
	public double skewed(double min, double max) {
		double entry;
		if (SimulationState.time < 15) {
			entry = Toolbox.map(SimulationState.time, 0, 15, min, max);
		} else {
			entry = Toolbox.map(SimulationState.time, 15, 20, max, 0);
			entry = Toolbox.constrain(entry, 0, max);
		}
		return entry;
	}
	
	public Road chooseNextRoad() {
		double[] probabilities = probabilityBranches();
		double r = Math.random();
		for (int i = 0; i < nextRoads.size(); i++) {
			if (r > probabilities[i]) {
				r -= probabilities[i];
				continue;
			} else {
				return nextRoads.get(i);
			}
		}
		return null;
	}
	
}
