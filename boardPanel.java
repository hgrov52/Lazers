
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class boardPanel extends JPanel{

	private Board board;
	private JLabel[][] blockSpaces;
	GridLayout grid;



	private int gap = 10;

	public boardPanel(){
		board = new Board();
		setLayout(new GridLayout(board.getNumRows(), board.getNumCols()));
		grid = (GridLayout) getLayout();
		grid.setHgap(20);
		grid.setVgap(20);		
		blockSpaces=new JLabel[board.getNumRows()][board.getNumCols()];
		for(int i=0; i<board.getNumRows(); i++){
			for(int j=0; j<board.getNumCols(); j++){
				ImageIcon image = new ImageIcon(new GlassBlock().getImage());
				blockSpaces[i][j]=new JLabel(image);
				add(blockSpaces[i][j]);
			}
		}
	}

	public void snap(Block b, int subtractTopPanel){

		remove(b);

		double blockposY = b.getY()+b.getHeight()/2;
		double blockposX = b.getX()+b.getWidth()/2; 
		boolean snapped = false;
		for(int i=0; i<blockSpaces.length; i++){
			for(int j=0; j<blockSpaces[i].length; j++){

				int spotX = blockSpaces[i][j].getX()+getX();
				int spotY = blockSpaces[i][j].getY()+getY();
				int spotW = blockSpaces[i][j].getWidth();
				int spotH = blockSpaces[i][j].getHeight();
				int maxX = blockSpaces[0][blockSpaces[0].length-1].getX()+getX()+spotW+gap;
				int maxY = blockSpaces[blockSpaces.length-1][0].getY()+getY()+spotH+gap+subtractTopPanel;
				int minX = blockSpaces[0][0].getX()+getX()-gap;
				int minY = blockSpaces[0][0].getY()+getY()-gap+subtractTopPanel;

				boolean betweenXCors = (blockposX>spotX-gap-1 && blockposX<spotX+spotW+gap+1);
				boolean betweenYCors = (blockposY>spotY+subtractTopPanel-gap-1 && blockposY<spotY+spotH+subtractTopPanel+gap+1);

				if (betweenXCors && betweenYCors ){
					board.autoSetSpot(i,j,b,blockSpaces,blockposX,blockposY,getX(),getY(),subtractTopPanel);
					b.setPlaced(true);
					snapped = true;
				}
				//special cases left/right - - if its not above the highest, its below the lowest 
				else if (betweenYCors && maxX<blockposX && !snapped){
					board.autoSetSpot(i,blockSpaces[0].length-1,b,blockSpaces,blockposX,blockposY,getX(),getY(),subtractTopPanel);
					b.setPlaced(true);
					snapped = true;
				}
				else if (betweenXCors && blockposY<minY && !snapped){
					board.autoSetSpot(i,j,b,blockSpaces,blockposX,blockposY,getX(),getY(),subtractTopPanel);
					b.setPlaced(true);
					snapped = true;
				}
				//special cases up/down - if its not above the highest, its below the lowest 
				else if (betweenXCors && maxY<blockposY && !snapped){
					board.autoSetSpot(blockSpaces.length-1,j,b,blockSpaces,blockposX,blockposY,getX(),getY(),subtractTopPanel);
					b.setPlaced(true);
					snapped = true;
				}
				else if (betweenYCors && blockposX<minX &&  !snapped){
					board.autoSetSpot(i,j,b,blockSpaces,blockposX,blockposY,getX(),getY(),subtractTopPanel);
					b.setPlaced(true);
					snapped = true;
				}
				//Q2
				else if(minX>blockposX && minY>blockposY&&!snapped){
					board.autoSetSpot(0,0,b,blockSpaces,blockposX,blockposY,getX(),getY(),subtractTopPanel);
					b.setPlaced(true);
					snapped = true;
				}
				//Q3
				else if(minX>blockposX && maxY<blockposY&&!snapped){
					board.autoSetSpot(blockSpaces.length-1,0,b,blockSpaces,blockposX,blockposY,getX(),getY(),subtractTopPanel);
					b.setPlaced(true);
					snapped = true;
				}
				//Q4
				else if(maxX<blockposX && maxY<blockposY&&!snapped){
					board.autoSetSpot(blockSpaces.length-1,blockSpaces[0].length-1,b,blockSpaces,blockposX,blockposY,getX(),getY(),subtractTopPanel);
					b.setPlaced(true);
					snapped = true;
				}
				//Q1
				else if(maxX<blockposX && minY>blockposY&&!snapped){
					board.autoSetSpot(0,blockSpaces[0].length-1,b,blockSpaces,blockposX,blockposY,getX(),getY(),subtractTopPanel);
					b.setPlaced(true);
					snapped = true;
				}
				if(!snapped){
					if(b.getOldX()!=null)
						b.setCors(b.getOldX(), b.getOldY());
					if(b.getOldBoardX()!=null)
						if(b.getType().substring(b.getType().length()-5,b.getType().length()).equals("Block"))board.setSpot(b.getOldBoardX(),b.getOldBoardY(), b);
				}
			}
		}
	}
	
	public void setToRandomSpot(Block b, int top){
		int row = (int)(Math.random()*getNumRows());
		int col = (int)(Math.random()*getNumCols());
		if(board.getSpot(row,col)==null && !b.getPlaced()){
			remove(b);
			board.setSpot(row,col,b);
			
			int spotX = blockSpaces[row][col].getX()+getX();
			int spotY = blockSpaces[row][col].getY()+getY();
			b.setCors(spotX-3,spotY+top-3);
			b.setPlaced(true);
			
		}
		else setToRandomSpot(b,top);
	}

	public void setToRandomSpot(ArrayList<Block> blocks, int top){
		for(Block b:blocks){
			remove(b);
			setToRandomSpot(b,top);
		}
	}

	public void remove(Block b){
		for(int i=0; i<board.getNumRows(); i++){
			for(int j=0; j<board.getNumCols(); j++){
				if(board.getSpot(i, j)!=null && board.getSpot(i, j).equals(b)){
					b.setOldBoardX(i);
					b.setOldBoardY(j);
					board.remove(i, j);
				}
			}
		}
	}

	public void clear(){
		board.clear();
	}

	public int getNumRows(){
		return board.getNumRows();
	}

	public int getNumCols(){
		return board.getNumCols();
	}

	public String toString(){
		return board.toString();
	}

	public JLabel getBoardBlock(int row, int col){
		return blockSpaces[row][col];
	}

	public int getGap(){
		return gap;
	}
	public void setGap(int g){
		gap = g;
	}
	
	public int getHGap(){
		return grid.getHgap();
	}
	
	public int getVGap(){
		return grid.getVgap();
	}

	public Point north(int x,int y,int width, int height, int extra){
		int xpos=blockSpaces[x][y].getX()+getX()-width/2+blockSpaces[x][y].getWidth()/2;
		int ypos=blockSpaces[x][y].getY()+getY()-height/2+extra-grid.getVgap()/2;
		return new Point(xpos,ypos);
	}
	public Point east(int x,int y,int width, int height, int extra){
		int xpos=blockSpaces[x][y].getX()+getX()+blockSpaces[x][y].getWidth()-width/2+grid.getHgap()/2;
		int ypos=blockSpaces[x][y].getY()+getY()+blockSpaces[x][y].getHeight()/2-height/2+extra;
		return new Point(xpos,ypos);
	}
	public Point south(int x,int y,int width, int height, int extra){
		int xpos=blockSpaces[x][y].getX()+getX()+blockSpaces[x][y].getWidth()/2-width/2;
		int ypos=blockSpaces[x][y].getY()+getY()+blockSpaces[x][y].getHeight()-height/2+extra+grid.getVgap()/2;
		return new Point(xpos,ypos);
	}
	public Point west(int x,int y,int width, int height, int extra){
		int xpos=blockSpaces[x][y].getX()+getX()-width/2-grid.getHgap()/2+5;
		int ypos=blockSpaces[x][y].getY()+getY()-height/2+blockSpaces[x][y].getHeight()/2+extra;
		return new Point(xpos,ypos);
	}


}
