import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*
 PROBLEMS:
 
 	combo box doesnt respond to the mouse
 	unmovable blocks dissappear after you move one of the blocks
 			something about the refresh?
 	stop the mouse dragged ghost block from affecting the lazer
 	
 */
public class Game {
	private JFrame frame;
	private JPanel framePanel;
	private JPanel topPanel;
	private JPanel buttonPanel;
	private JPanel bottomPanel;
	private JPanel centerPanel;
	private boardPanel boardPanel;
	private JPanel gPane;
	private ArrayList<Block> blocks;
	private ArrayList<Lazer> lazers;
	private ArrayList<EndPoint> endPoints;
	private Block selectedBlock;
	private Block tempSelected;
	private Block tempGhost;
	private BufferedImage background;
	private JButton addBlockButton;
	private JComboBox<String> box;
	private String blockToAdd;
	private double oldW;
	private double oldH;
	private Point mouseLocation;
	private Block[] addBlockImages;

	public Game() {
		prepareBlocks();
		initialize();
		prepareLazers();
		boardPanel.clear();
		boardPanel.setToRandomSpot(blocks,topPanel.getHeight());
		
	}

	public void initialize(){ 
		frame = new JFrame();
		frame.setTitle("Lazers");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(500, 500));
		framePanel = new JPanel();
		framePanel.setPreferredSize(new Dimension(1000, 800));
		oldH=800;
		oldW=1000;
		frame.add(framePanel);
		blockToAdd=null;
		setAddBlockImages();
		gPane=new JPanel(){
			public void paintComponent(Graphics graphics){
				Graphics2D g2 = (Graphics2D) graphics;

				for(int i=0; i<blocks.size(); i++){
					if(blocks.get(i).getX()!=null && blocks.get(i).getY()!=null)
						g2.drawImage(blocks.get(i).getPrintedImage(), (int)blocks.get(i).getX(), (int)blocks.get(i).getY(), (int)blocks.get(i).getWidth(), (int)blocks.get(i).getHeight(), null);
				}
				updateLazers(g2);
				paintAddBlockImages(g2);
			}
		};
		gPane.setLayout(new BorderLayout());
		frame.setGlassPane(gPane);
		gPane.setVisible(true);
		gPane.setOpaque(false);
		frame.pack();gPane.getGraphics().setColor(Color.red);

		URL bg = getClass().getResource("Easy.png");
		try {
			background = ImageIO.read(bg);
		} catch (IOException e1) {}

		selectedBlock = null;

		topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(framePanel.getWidth(), framePanel.getHeight()/10));
		topPanel.setBackground(Color.black);
		
		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(framePanel.getWidth(), framePanel.getHeight()/10));
		buttonPanel.setBackground(Color.green);
		buttonPanel.setOpaque(false);
		
		gPane.add(buttonPanel,BorderLayout.NORTH);

		bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(framePanel.getWidth(), framePanel.getHeight()/10));
		bottomPanel.setBackground(Color.black);

		centerPanel = new JPanel(){
			public void paintComponent(Graphics g){
				Graphics2D g2 = (Graphics2D) g;
				g2.drawImage(background,centerPanel.getX(),centerPanel.getY()-topPanel.getHeight(),centerPanel.getWidth(),centerPanel.getHeight(),null);

			}
		};
		centerPanel.setPreferredSize(new Dimension(framePanel.getWidth(), framePanel.getHeight()-framePanel.getHeight()/10));
		centerPanel.setBackground(Color.gray);
		centerPanel.setLayout(new GridBagLayout());

		boardPanel = new boardPanel();
		boardPanel.clear();
		boardPanel.setPreferredSize(new Dimension((blocks.get(0).getWidth()*boardPanel.getNumRows())+(boardPanel.getNumRows())*boardPanel.getGap()-boardPanel.getNumRows(), (blocks.get(0).getHeight()*boardPanel.getNumRows())+(boardPanel.getNumCols())*boardPanel.getGap()-boardPanel.getNumRows()));
		boardPanel.setOpaque(false);
		centerPanel.add(boardPanel);

		framePanel.setLayout(new BorderLayout());
		framePanel.add(topPanel, BorderLayout.NORTH);
		framePanel.add(bottomPanel, BorderLayout.SOUTH);
		framePanel.add(centerPanel, BorderLayout.CENTER);
		framePanel.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {//resizes the font, images, and some components
				topPanel.setPreferredSize(new Dimension(framePanel.getWidth(), framePanel.getHeight()/10));
				boardPanel.setPreferredSize(new Dimension((blocks.get(0).getWidth()*boardPanel.getNumRows())+(boardPanel.getNumRows())*boardPanel.getGap(), (blocks.get(0).getHeight()*boardPanel.getNumRows())+(boardPanel.getNumCols())*boardPanel.getGap()));

				for(Block b:blocks){
					b.setWidth(b.getWidth()*framePanel.getWidth()/1000);
					b.setHeight(b.getHeight()*framePanel.getHeight()/800);

					b.setCors((int)(b.getX()*framePanel.getWidth()/oldW),(int) (b.getY()*framePanel.getHeight()/oldH));
					if(b.getPlaced())boardPanel.snap(b,topPanel.getHeight());
				}

				for(int i=0; i<blocks.size(); i++){
					blocks.get(i).setWidth(50*framePanel.getWidth()/1000);
					blocks.get(i).setHeight(50*framePanel.getHeight()/800);

					blocks.get(i).setCors((int)(blocks.get(i).getX()*framePanel.getWidth()/oldW),(int) (blocks.get(i).getY()*framePanel.getHeight()/oldH));
					if(blocks.get(i).getPlaced())boardPanel.snap(blocks.get(i),topPanel.getHeight());

				}
				oldW=framePanel.getWidth();
				oldH=framePanel.getHeight();
			}
			public void componentHidden(ComponentEvent arg0) {
			}
			public void componentMoved(ComponentEvent arg0) {
			}
			public void componentShown(ComponentEvent arg0) {
			}
		});

		for(int i=0; i<blocks.size(); i++)
			blocks.get(i).setCors(i*topPanel.getWidth()/blocks.size()+(topPanel.getWidth()/blocks.size())/2-blocks.get(i).getWidth(),20);

		addBlockButton = new JButton("Add Block");
		addBlockButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				addBlock(blockToAdd);
				gPane.updateUI();
			}
		});
		addBlockButton.setBackground(new Color(55,55,55));
		addBlockButton.setForeground(Color.black);
		addBlockButton.setFocusPainted(false);
		addBlockButton.setFont(new Font("DialogInput", 20, 13));
	        
		buttonPanel.add(addBlockButton);


		String[] blockTypes = {"    -   - ","Regular","Glass","Un-Reflecting"};
		box = new JComboBox<String>(blockTypes);
		box.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				box.requestFocus();
				box.getUI().setPopupVisible(box,true);
				blockToAdd = box.getSelectedItem().toString();
			}
		});
		buttonPanel.add(box);




		gPane.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {//if a Block is currently selected, the selected Block will follow the mouse
				if(selectedBlock!=null){
					for(Lazer l:lazers){
						l.clear();
					}
					selectedBlock.moveTo(e.getX()-selectedBlock.getWidth()/2,e.getY()+2*selectedBlock.getHeight()/2-topPanel.getHeight());

					tempGhost.setCors(selectedBlock.getX(),selectedBlock.getY());
					boardPanel.snap(tempGhost,topPanel.getHeight());
					gPane.repaint();
				}
			}
			public void mouseMoved(MouseEvent e) {
				mouseLocation = new Point(e.getX(),e.getY());
			}

		});
		gPane.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					for(int i=blocks.size()-1; i>=0; i--)
						if(blocks.get(i).contains(e.getPoint())){
							selectedBlock=blocks.get(i);
							tempGhost = new Block(selectedBlock);
							tempGhost.setType(tempGhost.getType()+"Ghost");
							tempGhost.setPrintedImage(tempGhost.getGhost());
							boardPanel.snap(tempGhost,topPanel.getHeight());
							blocks.add(tempGhost);
							selectedBlock.setOldX(selectedBlock.getX());
							selectedBlock.setOldY(selectedBlock.getY());
							selectedBlock.setPrintedImage(new EmptySpace().getImage());
							tempSelected = new Block(selectedBlock);
							tempSelected.setType(tempSelected.getType()+"Selected");
							tempSelected.setCors(selectedBlock.getX(), selectedBlock.getY());
							tempSelected.setPrintedImage(tempSelected.getSelected());
							blocks.add(tempSelected);
							bringToFront(selectedBlock);
							frame.repaint();
							break;
						}
				}
			}
			public void mouseReleased(MouseEvent e) {
				if(selectedBlock!=null){
					boardPanel.snap(selectedBlock,topPanel.getHeight());
					selectedBlock.setPrintedImage(selectedBlock.getImage());
				}
				if(tempSelected != null){
					tempSelected.setPrintedImage(tempSelected.getImage());
					blocks.remove(tempSelected);
					tempSelected=null;
				}
				if(tempGhost!=null){
					tempGhost.setPrintedImage(tempGhost.getImage());
					blocks.remove(tempGhost);
					tempGhost=null; 
				}
				System.out.println(boardPanel);
				for(Lazer l:lazers){
					l.clear();
				}
				frame.repaint();
				selectedBlock=null;
				frame.setVisible(true);

			}
		});
		frame.pack();
		frame.setVisible(true);
	}
	
	public void setAddBlockImages(){
		
		
		
	}
	
	public void paintAddBlockImages(Graphics2D g){
		int space = centerPanel.getHeight()/4;
		int start = topPanel.getHeight()+space;
		int y = frame.getWidth()-50;
		addBlockImages[0].setCors(start,y);
		addBlockImages[1].setCors(start+space,y);
		addBlockImages[2].setCors(start+space*2,y);
		int i=0;
		while (i<3){
			g.drawImage(addBlockImages[i].getPrintedImage(), 100,100, addBlockImages[i].getWidth(), addBlockImages[i].getHeight(), null);
			i++;
		}
	}

	public void bringToFront(Block block){
		for(int i = 0;i<blocks.size();i++){
			if(blocks.get(i).equals(block))bringToFront(i);
		}
	}

	public void bringToFront(int index){
		for(int i=index; i<blocks.size()-1; i++){
			Block temp=blocks.get(i);
			blocks.set(i,blocks.get(i+1));
			blocks.set(i+1,temp);
		}
	}

	public void prepareLazers() {
		lazers = new ArrayList<Lazer>();
		createLazer(2,3,"west",Lazer.SOUTHEAST);
		//createLazer(1,1,"west",Lazer.SOUTHEAST);
		//createLazer(3,0,"east",Lazer.SOUTHWEST);
		createLazer(1,4,"south",Lazer.NORTHEAST);
		
		endPoints = new ArrayList<EndPoint>();
		int width = 20;
		Point posEP1=boardPanel.north(2,2,width,width,topPanel.getHeight());
		EndPoint ep1 = new EndPoint(posEP1,width,Color.red);
		endPoints.add(ep1);
	}

	public void createLazer(int x, int y, String orientation, double direction){
		createLazer(x,y,orientation,direction,Color.red);
	}

	public void createLazer(int x, int y, String orientation, double direction, Color color){
		Point pos=null;
		int width=4;
		if(orientation.equals("north"))pos = boardPanel.north(y,x,width,width,topPanel.getHeight());
		if(orientation.equals("east"))pos = boardPanel.east(y,x,width,width,topPanel.getHeight());
		if(orientation.equals("south"))pos = boardPanel.south(y,x,width,width,topPanel.getHeight());
		if(orientation.equals("west"))pos = boardPanel.west(y,x,width,width,topPanel.getHeight());

		int widthLeg = boardPanel.getBoardBlock(0,0).getWidth()/2 + boardPanel.getHGap()/2;
		int heightLeg = boardPanel.getBoardBlock(0,0).getHeight()/2 + boardPanel.getVGap()/2;

		double diagonal = Math.sqrt((widthLeg*widthLeg) + (heightLeg*heightLeg));

		System.out.println(diagonal);

		lazers.add(new Lazer(new Point(pos.x,pos.y),direction,color,diagonal));

	}

	public void updateLazers(Graphics2D g){
		g.setColor(Color.red);
		for(Lazer l:lazers){
			l.paint(g, blocks, endPoints);
		}
		for(EndPoint ep:endPoints){
			ep.paint(g,mouseLocation);
		}
	}

	public void prepareBlocks() {
		Block b1 = new GlassBlock();
		Block b2 = new GlassBlock();

		blocks = new ArrayList<Block>();
		blocks.add(b1);
		blocks.add(b2);
		
		Block regular = new UnMovableBlock();
		Block glass = new UnMovableBlock();
		Block unreflecting = new UnMovableBlock();
		addBlockImages = new Block[]{regular,glass,unreflecting};
		for(Block b:addBlockImages){
			blocks.add(b);
		}
		
		
		
		URL rImage = this.getClass().getResource("RegularBlock.png");
		URL gImage = this.getClass().getResource("GlassBlock.png");
		URL uImage = this.getClass().getResource("UnReflectingBlock.png");
		
		try {
			regular.setImage(ImageIO.read(rImage));
			glass.setImage(ImageIO.read(gImage));
			unreflecting.setImage(ImageIO.read(uImage));
		} catch (IOException e) {}
	}

	public void addBlock(String type){
		if(type!=null && blocks.size()+1<boardPanel.getNumRows()*boardPanel.getNumCols()){
			Block b = (type.equals("Glass"))?new GlassBlock():(type.equals("Un-Reflecting"))?new UnReflectingBlock():new RegularBlock();
			blocks.add(b);
		}
		putAwayUnplacedBlocks();
	}
	
	public void putAwayUnplacedBlocks(){
		ArrayList<Block> unplaced = new ArrayList<Block>();
		for(Block b:blocks){
			if(!b.getPlaced()){
				unplaced.add(b);
			}
		}
		for(int i =0;i<unplaced.size();i++){
			boardPanel.setToRandomSpot(unplaced, topPanel.getHeight());
		}
		frame.repaint();
	}

	public static void main(String[] args){
		Game display = new Game();
	}
}
