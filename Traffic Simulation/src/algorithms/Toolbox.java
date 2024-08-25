package algorithms;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.Launcher;
import states.SimulationState;

public class Toolbox {

	public static BufferedImage flipImage(BufferedImage image) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(image, null);
	}

	public static int mouseX() {
		Point mousePosition = Launcher.looper.getDisplay().getCanvas().getMousePosition();
		if (mousePosition == null) {
			return -1;
		} else {
			return mousePosition.x;
		}
	}

	public static int mouseY() {
		Point mousePosition = Launcher.looper.getDisplay().getCanvas().getMousePosition();
		if (mousePosition == null) {
			return -1;
		} else {
			return mousePosition.y;
		}
	}
	
	public static Coordinates mouseCoordinates() {
		return screenToCoordinates(new Coordinates(mouseX(), mouseY()));
	}

	public static void drawCenteredImage(Graphics2D g, BufferedImage image, int x, int y, int width, int height) {
		g.drawImage(image, x - width / 2, y - height / 2, width, height, null);
	}
	
	public static void drawCoordinates(Graphics2D g, Coordinates coordinates, Color color) {
		g.setColor(color);
		Coordinates screenCoordinates = Toolbox.coordinatesToScreen(coordinates);
		g.setStroke(new BasicStroke(2));
		double radius = 2 * (1 + (SimulationState.zoom - 1) * 0.6);
		g.drawOval((int) (screenCoordinates.x - radius), (int) (screenCoordinates.y - radius), (int) (2 * radius), (int) (2 * radius));
	}
	
	public static void drawScreenCoordinates(Graphics2D g, Coordinates coordinates, Color color) {
		g.setColor(color);
		g.setStroke(new BasicStroke(2));
		double radius = 2 * (1 + (SimulationState.zoom - 1) * 0.6);
		g.drawOval((int) (coordinates.x - radius), (int) (coordinates.y - radius), (int) (2 * radius), (int) (2 * radius));
	}
	
	public static Coordinates coordinatesToScreen(Coordinates coordinates) {
		Coordinates center = new Coordinates(Launcher.WIDTH / 2, Launcher.HEIGHT / 2);
		center.x += (coordinates.x - SimulationState.displayCenter.x) * SimulationState.zoom;
		center.y += (-coordinates.y - SimulationState.displayCenter.y) * SimulationState.zoom;
		return center;
	}
	
	public static Coordinates screenToCoordinates(Coordinates coordinates) {
		Coordinates center = new Coordinates(0, 0);
		center.x = (coordinates.x - Launcher.WIDTH / 2.0) / SimulationState.zoom + SimulationState.displayCenter.x;
		center.y = ((coordinates.y - Launcher.HEIGHT / 2.0) / SimulationState.zoom + SimulationState.displayCenter.y);
		return center;
	}

	public static double constrain(double x, double lowerBound, double upperBound) {
		if (x > upperBound)
			return upperBound;
		if (x < lowerBound)
			return lowerBound;
		return x;
	}

	public static double map(double input, double startMin, double startMax, double mappedMin, double mappedMax) {
		return mappedMin + (input - startMin) * (mappedMax - mappedMin) / (startMax - startMin);
	}

	public static Coordinates map(double t, double min, double max, Coordinates origin, Coordinates destination) {
		double x = Toolbox.map(t, min, max, origin.x, destination.x);
		double y = Toolbox.map(t, min, max, origin.y, destination.y);
		return new Coordinates(x, y);
	}
	
	public static double distance(Coordinates p1, Coordinates p2) {
		double deltaX = p1.x - p2.x, deltaY = p1.y - p2.y;
		return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
	}

	public static void setFontSize(Graphics2D g, float size) {
		g.setFont(Launcher.gameFont.deriveFont(size));
	}

	public static void drawText(Graphics2D g, String text, int x, int y) {

		int height = g.getFontMetrics().getAscent() - g.getFontMetrics().getDescent();
		int width = g.getFontMetrics().stringWidth(text);
		int textX, textY;

		if (alignX == ALIGN_LEFT) {
			textX = x;
		} else if (alignX == ALIGN_CENTER) {
			textX = x - width / 2;
		} else {
			textX = x - width;
		}

		if (alignY == ALIGN_TOP) {
			textY = y + height;
		} else if (alignY == ALIGN_CENTER) {
			textY = y + height / 2;
		} else {
			textY = y;
		}

		g.drawString(text, textX, textY);

	}

	public static void drawBoundedText(Graphics2D g, String text, int x, int y, int textBoxWidth) {

		TextLayout layout = new TextLayout(text, g.getFont(), g.getFontRenderContext());
		String[] splitted = text.split("\n");
		List<String> lines = new ArrayList<String>();
		for (String line : splitted) {
			if (line.equals("")) {
				lines.add("");
				continue;
			}
			String str = "";
			String[] words = line.split(" ");
			int strWidth = 0;
			for (int i = 0; i < words.length; i++) {
				String word = words[i] + ((i == words.length - 1) ? "" : " ");
				int wordWidth = g.getFontMetrics().stringWidth(word);
				if (strWidth + wordWidth > textBoxWidth) {
					lines.add(str);
					str = word;
					strWidth = wordWidth;
				} else if (i == words.length - 1) {
					str += word;
					lines.add(str);
					str = "";
					strWidth = 0;
				} else {
					str += word;
					strWidth += wordWidth;
				}
			}
			lines.add(str);
		}
		for (int i = 0; i < lines.size(); i++) {
			g.drawString(lines.get(i), x, (int) (y + i * (layout.getBounds().getHeight() + 3)));
		}

	}
	
	public static String timeString() {
		int fullMinutes = (int) SimulationState.time;
		int seconds = (int) ((SimulationState.time - fullMinutes) * 60.0);
		return "8:" + (15 + fullMinutes) + ":" + String.format("%02d", seconds) + " AM";
	}
	
	public static String formatDecimal(double number, int right) {
		return String.format("%." + right + "f", number);
	}

	public static int alignX = 0, alignY = 0;
	public static final int ALIGN_TOP = 0, ALIGN_LEFT = 0, ALIGN_CENTER = 1, ALIGN_BOTTOM = 2, ALIGN_RIGHT = 2;

	public static void setAlignX(int align) {
		alignX = align;
	}

	public static void setAlignY(int align) {
		alignY = align;
	}

	public static void setAlign(int x, int y) {
		alignX = x;
		alignY = y;
	}

}
