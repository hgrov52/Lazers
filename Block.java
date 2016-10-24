
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/*

	Blocks: 
		Regular
		Glass
		Un-Movable
		Non-Reflecting
 *Not a Valid Board Spot


 */

public class Block{

	//should these be in the subclasses instead?
	//hold its image
	//hold the image of the ghost block when its being moved
	//if its a valid spot 
	//know if its unmovable 

	private BufferedImage printedImage;
	private BufferedImage image;
	private BufferedImage ghost;
	private BufferedImage selected;

	//if visible is false, then all other booleans set to false 
	private boolean visible=true;
	private boolean movable=true;
	private String type;
	private boolean placed=false;

	/*
		 reflects can either be 
		 	-reflects
		 	-refracts
		 	-absorbs
		 	-notAffected
	 */
	private String reflects;

	
	private Integer oldX;
	private Integer oldY;
	private Integer oldBoardX;
	private Integer oldBoardY;
	
	//Integers because they flash up at 0,0 during snap calcs
	private Integer x;
	private Integer y;
	private int width;
	private int height;

	public Block(){
		this("RegularBlock.png","RegularBlockGhost.png","RegularBlockSelected.png");
	}

	public Block(String s){
		this((s+".png"),(s+"Ghost.png"),(s+"Selected.png"));
	}
	
	public Block(Block b){
		printedImage = b.getPrintedImage();
		image = b.getImage();
		ghost = b.getGhost();
		selected = b.getSelected();
		type = b.getType();
		visible = b.getVisible();
		movable = b.getMovable();
		placed= b.getPlaced();
		reflects = b.getReflects();
		width = b.getWidth();
		height = b.getHeight();
		x = b.getX();
		y = b.getY();
	}

	public Block(String i, String g, String s){

		URL urli=null;
		URL urlg=null;
		URL urls=null;

		if(i!=null)urli = this.getClass().getResource(i);
		if(g!=null)urlg = this.getClass().getResource(g);
		if(s!=null)urls = this.getClass().getResource(s);

		try {
			//image = ImageIO.read(getClass().getResourceAsStream("/workspace/Lazers/src/RegularBlock.png"));
			if(urli!=null)image = ImageIO.read(urli);
			if(urlg!=null)ghost = ImageIO.read(urlg);
			if(urls!=null)selected = ImageIO.read(urls);
		} catch (IOException e) {}

		int index =0;
		while(i.charAt(index)!='.'){
			index++;
		}
		type = i.substring(0,index);
		printedImage=image;
		
		x=null;
		y=null;
		oldX=null;
		oldY=null;
		width=50;
		height=50;

	}

	public Block(String i, String g, String s, String t){
		this(i,g,s);
		type=t;
	}

	public void setMovable(boolean movable){
		this.movable=movable;
	}

	public boolean getMovable(){
		return movable;
	}

	public void setReflects(String reflects){
		this.reflects=reflects;
	}

	public String getReflects(){
		return reflects;
	}

	public void setVisible(boolean visible){
		this.visible=visible;
		if(!visible){
			movable=visible;
			reflects="notAffected";
		}
	}

	public boolean getVisible(){
		return visible;
	}

	public boolean contains(int x, int y){
		if(getX()!=null)
		return (x<getX()+getWidth() && x>getX() && y<getY()+getHeight() && y>getY());
		else return false;
		
	}

	public boolean contains(Point p){
		if(p!=null)
		return contains(p.x,p.y);
		else return false;
	}

	public void setImage(BufferedImage image){
		this.image=image;
	}

	public BufferedImage getImage(){
		return image;
	}

	public void setGhost(BufferedImage ghost){
		this.ghost=ghost;
	}

	public BufferedImage getGhost(){
		return ghost;
	}

	public void setSelected(BufferedImage selected){
		this.selected=selected;
	}

	public BufferedImage getSelected(){
		return selected;
	}

	public void moveTo(int x, int y){
		if(movable)
			setCors(x,y);
	}
	
	public void setCors(Point p){
		setCors(p.x,p.y);
	}

	public void setCors(int x, int y){
		
			this.x=x;
			this.y=y;
		
	}

	public Integer getX(){
		return x;
	}
	
	public Integer getY(){
		return y;
	}

	public void setWidth(int width){
		this.width=width;
	}

	public int getWidth(){
		return width;
	}

	public void setHeight(int height){
		this.height=height;
	}

	public int getHeight(){
		return height;
	}
	
	public boolean getPlaced(){
		return placed;
	}
	
	public void setPlaced(boolean p){
		placed=p;
	}
	
	public BufferedImage getPrintedImage(){
		return printedImage;
	}
	
	public void setPrintedImage(BufferedImage b){
		printedImage = b;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String s){
		type = s;
	}
	
	public Integer getOldX(){
		return oldX;
	}
	
	public void setOldX(int x){
		oldX=x;
	}
	
	public Integer getOldY(){
		return oldY;
	}
	
	public void setOldY(int y){
		oldY=y;
	}
	
	public Integer getOldBoardX(){
		return oldBoardX;
	}
	
	public void setOldBoardX(int x){
		oldBoardX=x;
	}
	
	public Integer getOldBoardY(){
		return oldBoardY;
	}
	
	public void setOldBoardY(int y){
		oldBoardY=y;
	}
}
