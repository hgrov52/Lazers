import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class EndPoint {


	private int width;
	private Color color;
	private Point location;
	
	private boolean win;
	
	public EndPoint(){
		this(null,20,Color.red);
	}

	public EndPoint(Point loc, int w, Color c){
		width = w;
		color = c;
		location = loc;
		win=false;
	}

	public boolean contains(int x, int y){
		 return(x>location.x && location.x+width>x && y>location.y && location.y+width>y);
	}

	public boolean contains(Point p){
		return contains(p.x,p.y);
	}

	public void paint(Graphics g, Point mouseCors){
		g.setColor(color);
		for(int i=1;i<width;i+=4){
			g.drawOval(location.x+((width-i)/2), location.y+((width-i)/2), i,i);
		}
		
	}
	
	public void setColor(Color c){
		color=c;
	}
	
	public boolean getWin(){
		return win;
	}
}
