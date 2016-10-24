import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;


public class Lazer {

	public static final double NORTHEAST = -Math.PI/4;
	public static final double NORTHWEST = -3*Math.PI/4;
	public static final double SOUTHEAST = Math.PI/4;
	public static final double SOUTHWEST = 3*Math.PI/4;

	private Point origin;
	private Color color;
	private double initialDirection;
	private ArrayList<Segment> segments;
	private double dist;

	public Lazer(){
		this(null,SOUTHEAST,Color.red,45);
	}

	public Lazer(Point origin,double direction,Color color,double dist){
		this.origin=origin;
		this.initialDirection=direction;
		segments = new ArrayList<Segment>();
		segments.add(new Segment(origin,direction));
		this.dist=dist;
	}


	public void paint(Graphics g,ArrayList<Block> blocks, ArrayList<EndPoint> endPoints){
		g.setColor(color);
		int width=8;
		g.drawOval(origin.x-width/2,origin.y-width/2,width,width);
		width/=2;
		g.drawOval(origin.x-width/2,origin.y-width/2,width,width);
		for(int index=0;index<segments.size();index++){
			//if(segments.get(index-1)!=null && segments.get(index).getDirection()==segments.get(index-1).getDirection())segments.remove(index);
			Point end = determineEndPoint(segments.get(index).getStart(),blocks,segments.get(index).getDirection(),endPoints);			
			g.drawLine(segments.get(index).getStart().x,segments.get(index).getStart().y,end.x,end.y);
			g.drawOval(end.x-width/2,end.y-width/2,width,width);
			System.out.println(segments.size());
			if(segments.size()>100)break;
		}
		
	}

	/*

	 -once it hits a block, add 22 in the direction PI/2 clockwise and if b.contains() then add another Pi/2 
	 	-do it three times, then else{ break } and make it the end like if it hit an unreflecting block
	 -make sure direction does not equal the old direction plus Pi
	 -need the generated segment to follow the rules in determineEndPoint just like its last segment


	 */

	public Point determineEndPoint(Point start, ArrayList<Block> blocks, double direction, ArrayList<EndPoint> endPoints ){
		
		for(EndPoint ep:endPoints){
			ep.setColor(Color.red);
		}
		
		double dist=0;
		boolean clear=true;
		Point end=null;
		while(clear=true){

			for(EndPoint ep:endPoints){
				if(ep.contains(quickEndPoint(start,dist,direction))){
					ep.setColor(Color.green);
				}
				
			}
			
			for(Block b:blocks){
				if(b.contains(quickEndPoint(start,dist+this.dist/2,direction))){
					end =  quickEndPoint(start,dist,direction);
					if(b.getType().substring(0,5).equals("Glass")){
						addNextSegment(direction,end,b);
					}
					else if(b.getType().substring(0,5).equals("UnRef")) {
						return end;
					}
					else{		
						addNextSegment(direction,end,b);
						return end;					
					}
				}
			}
			if(clear==true)dist+=this.dist;
			if(dist>3000 || clear==false)break;
		}
		end =  new Point((int) (dist*Math.cos(direction)+start.x), (int) (start.y+dist*Math.sin(direction)));
		return end;
	}
	
	public void addNextSegment(double direction, Point end, Block b){
		int counter=1;
		while(counter<4){
			if(!b.contains(quickEndPoint(end,22,direction+((Math.PI/2)*counter)))&&direction+((Math.PI/2)*counter)!=direction+Math.PI){
				segments.add(new Segment(end,direction+((Math.PI/2)*counter)));
				break;
			}
			counter++;
		}
	}

	public Point quickEndPoint(Point start, double dist){
		return quickEndPoint(start,dist,initialDirection);
	}

	public Point quickEndPoint(Point start, double dist, double direction){
		return new Point((int) (dist*Math.cos(direction)+start.x), (int) (start.y+dist*Math.sin(direction)));
	}

	public Double getDirection(){
		return initialDirection;
	}

	public void setDirection(Integer direction){
		this.initialDirection=direction;
	}
	public void clear(){
		segments.clear();
		segments.add(new Segment(origin,initialDirection));
	}

}
