package info;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import algorithms.Coordinates;
import algorithms.Toolbox;
import main.Launcher;

public class Graph {
	
	public static final int GRAPH_HEIGHT = 90;
	
	public Coordinates origin;
	public List<DataPoint>[] dataLists;
	
	public Graph(Coordinates origin, List<DataPoint>[] dataLists) {
		this.origin = origin;
		this.dataLists = dataLists;
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1));
		g.drawLine((int) origin.x, (int) origin.y, (int) origin.x, (int) origin.y - GRAPH_HEIGHT);
		g.drawLine((int) origin.x, (int) origin.y, (int) origin.x + Launcher.BAND_WIDTH - 60, (int) origin.y);
		
		double max = maximumValue();
		Toolbox.setAlign(Toolbox.ALIGN_RIGHT, Toolbox.ALIGN_CENTER);
		Toolbox.setFontSize(g, 12);
		g.setColor(new Color(100, 100, 100));
		if (Math.abs(((int) max) - max) < 0.0001) {
			Toolbox.drawText(g, "" + (int) max, (int) origin.x - 3, (int) origin.y - GRAPH_HEIGHT);
		} else {
			Toolbox.drawText(g, "" + Toolbox.formatDecimal(max, 1), (int) origin.x - 3, (int) origin.y - GRAPH_HEIGHT);
		}
		Toolbox.setAlign(Toolbox.ALIGN_CENTER, Toolbox.ALIGN_TOP);
		Toolbox.setFontSize(g, 11);
		Toolbox.drawText(g, "8:15:00 AM", (int) origin.x + 15, (int) origin.y + 5);
		Toolbox.drawText(g, Toolbox.timeString(), (int) origin.x + Launcher.BAND_WIDTH - 60 - 15, (int) origin.y + 5);
		
		for (List<DataPoint> data : dataLists) {
			if (data.size() >= 2) {
				for (int i = 0; i < data.size() - 1; i++) {
					int x1 = (int) Toolbox.map(data.get(i).t, 0, data.get(data.size() - 1).t, origin.x, origin.x + Launcher.BAND_WIDTH - 60);
					int y1 = (int) Toolbox.map(data.get(i).data, 0, max, origin.y, origin.y - GRAPH_HEIGHT);
					int x2 = (int) Toolbox.map(data.get(i + 1).t, 0, data.get(data.size() - 1).t, origin.x, origin.x + Launcher.BAND_WIDTH - 60);
					int y2 = (int) Toolbox.map(data.get(i + 1).data, 0, max, origin.y, origin.y - GRAPH_HEIGHT);
					g.drawLine(x1, y1, x2, y2);
					
				}
			}
		}
	}
	
	public double maximumValue() {
		double max = 0;
		for (List<DataPoint> list : dataLists) for (DataPoint dp : list) max = Math.max(max, dp.data);
		return max;
	}
	
}
