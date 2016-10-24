
public class Level {

	private Board board;
	private static int difficulty;

	public Level(){
		this(1);
	}

	public Level(int difficulty){
		this.difficulty = difficulty;
	}

	public void generateBoard(){

		/*

		 4x4 easiest
		 10x10 hardest 

		 */
		if(Math.random()>.5)
			board = new Board((int)((Math.random()*100)%10));
		else board = new Board();

	}

	public void populateBlocks(){

	}

	public void addLazerOriginations(){

	}

	public void addPossibleLazerEndPoints(){

	}

	public int getAppropriateDimension(){
		int x = (int)(Math.random()*10);
		double rand = Math.random();
		while(rand<.5){
			rand=Math.random();
		}
		int dim = (int)(rand*x+4);
		return dim;
	}

	public static void main(String[] args){
		
	}

}
