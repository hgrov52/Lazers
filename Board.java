import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JLabel;


public class Board {

	private Block[][] board;
	
	public Board(){
		this(5,5);
	}
	
	public Board(int sideLength){
		this(sideLength,sideLength);
	}
	
	public Board(int numRows,int numCols){
		board = new Block[numRows][numCols];
	}
	
	public Board(Block[][] otherBoard){
		board=otherBoard;
	}
	
	public boolean setSpot(int row, int col, Block block){
		board[row][col] = block;
		if(getSpot(row,col).equals(block))return true;
		else return false;
	}
	
	public Point autoSetSpot(int row, int col, Block b, JLabel[][] blockSpaces,double mouseX,double mouseY, int getX, int getY, int subtractTopPanel){		
		int spotX = blockSpaces[row][col].getX()+getX;
		int spotY = blockSpaces[row][col].getY()+getY;
		
		for(int i=0; i<getNumRows(); i++){
			for(int j=0; j<getNumCols(); j++){
				if(getSpot(i, j)!=null && getSpot(i, j).equals(b)){
					remove(i, j);
					
				}
			}
		}
		
		String type = b.getType().substring(b.getType().length()-5,b.getType().length());
		
		if(board[row][col]==null && type.equals("Block")){
		b.setCors(spotX-3,spotY+subtractTopPanel-3);
		setSpot(row,col,b);
		}
		
		else if(board[row][col]==null){
			b.setCors(spotX-3,spotY+subtractTopPanel-3);
		}
		
		else{
			if(b.getOldX()!=null)
			b.setCors(b.getOldX(),b.getOldY());
			if(b.getOldBoardX()!=null)
				if(type.equals("Block"))setSpot(b.getOldBoardX(),b.getOldBoardY(),b);
			
		}
		
		if(getSpot(row,col)!=null && getSpot(row,col).equals(b))return null;
		else return new Point(row,col);
		
	}
	
	public Block getSpot(int row, int col){
		return board[row][col];
	}
	
	public boolean remove(int row, int col){
		board[row][col]=null;
		if(getSpot(row,col)!=null && getSpot(row,col).equals(null))return true;
		else return false;
	}
	
	public void clear(){
		for(int row = 0;row<board.length;row++){
			for(int col = 0;col<board[row].length;col++){
				board[row][col]=null;
			}
		}
	}
	
	public boolean isValid(int row, int col){
		if(row < board.length && col < board[0].length && row>0 && col>0)
			return true;
		else return false;
	}
	
	public int getNumRows(){
		return board.length;
	}
	
	public int getNumCols(){
		return board[0].length;
		
	}
	
	public String toString(){
		String c = "";
		for(int i= 0; i<getNumRows();i++){
			c+="\n";
			c+="\n";
			for(int j= 0;j<getNumCols();j++){
				String s=null;
				s=(board[i][j]!=null)?board[i][j].getType():null;
				c += (s + "   ");
			}
		}
		return c;
	}
	
	public void print(){
		System.out.println(toString());
	}
}
