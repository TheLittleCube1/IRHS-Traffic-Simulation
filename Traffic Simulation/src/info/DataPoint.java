package info;

public class DataPoint {
	
	public double t, data;
	
	public DataPoint(double t, double data) {
		this.t = t;
		this.data = data;
	}
	
	public String toString() {
		return ("(" + t + ", " + data + ")");
	}
	
}
