import java.awt.Point;


public class Segment {
	
	private Point start;
	private double direction;
	
	public Segment(Point p, double dir){
		start=p;
		direction=dir;
		while(direction>Math.PI*2){
			direction-=Math.PI*2;
		}
	}
	
	public Point getStart(){
		return start;
	}
	
	public void setStart(Point p){
		start=p;
	}
	
	public double getDirection(){
		return direction;
	}
	
	public void setDirection(double dir){
		direction=dir;
	}
	
}
