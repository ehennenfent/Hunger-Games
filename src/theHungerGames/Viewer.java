package theHungerGames;

import hungerScrimmage.ScrimmageDriver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * The basic class for viewing the ecology simulation
 * 
 * @author Peter Dong
 *
 */
public class Viewer extends JPanel implements MouseListener, ActionListener {

	private Arena map;
	private LeaderBoard leaderBoard;
	private TimelineBoard timelines;
	private static int frameNum = 0;
	private static JFrame frame = new JFrame();
	public static final boolean SAVE_IMAGES = ScrimmageDriver.getImageState();
	/**
	 * This determines the size of the bar along the bottom of the view.
	 */
	private static final int SIZE_OF_BAR = 20;

	/**
	 * If I don't have this, Eclipse will yell at me.  It is not used.
	 */
	private static final long serialVersionUID = -4924678870492657424L;

	/**
	 * @param mymap - the Arena to be used
	 * @param printout - If this is true, the readout each turn will be printed to the system console.
	 */
	public Viewer(Arena mymap, double timeIncrement) {
		
		addMouseListener(this);
		map = mymap;
		setSize(map.getXSize(), map.getYSize() + SIZE_OF_BAR);
		setBackground(Color.white);
		
		leaderBoard = new LeaderBoard(mymap);
		timelines = new TimelineBoard(mymap, timeIncrement);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(leaderBoard);
		frame.setName("Leaderboard");
		frame.setSize(leaderBoard.getWidth(), leaderBoard.getHeight());
		frame.setVisible(true);
		saveImage(frame);
		
		JFrame frame2 = new JFrame();
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.add(timelines);
		frame2.setName("Timeline_Board");
		frame2.setSize(timelines.getWidth() + 100, timelines.getHeight());
		frame2.setVisible(true);
		saveImage(frame2);
	}

	/**
	 * This allows the user to set a timer so that the Arena takes a turn automatically at regular intervals.
	 * @param increment - the amount of time, in milliseconds, between each turn
	 */
	public void setTimer(int increment) { // in milliseconds
		Timer time = new Timer(increment, this);
		time.addActionListener(leaderBoard);
		time.addActionListener(timelines);
		time.start();
	}
	
	/**
	 * This is the actual heart of the function, called whenever an action is received.
	 */
	private void doStuff() {
		frameNum++;
		if (map.doTurn()) {
			repaint();
		} else {
			Map<String,Integer> scores = LeaderBoard.scores;
			for(Map.Entry<String,Integer> entry : scores.entrySet()){
				System.out.println(entry.getKey() + ": " + entry.getValue());
			}
			close();
		}
		if(SAVE_IMAGES)
		saveImage(frame);
	}

	private void close() {
		map.close();
		java.awt.Window w = SwingUtilities.getWindowAncestor(this);
		WindowEvent wev = new WindowEvent(w, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		myPaint(g);
	}
	
	/**
	 * This determines how far from the bottom the text should print.
	 */
	private static final int DISTANCE_FROM_EDGE = 25;
	/**
	 * This is how far the text starts from the edge of the bar.
	 */
	private static final int DISTANCE_FROM_LEFT_EDGE = 10;
	
	/**
	 * This draws the Arena and adds the status bar at the bottom with the Animal count
	 * @param g - a Graphics object to draw with
	 */
	public void myPaint(Graphics g) {
		map.Draw(g, getLocation().x, getLocation().y);
		
		g.setColor(Color.black);
//		String status = map.countAnimals();
//		g.drawString(status, DISTANCE_FROM_LEFT_EDGE, map.getYSize() - DISTANCE_FROM_EDGE);
	}

	// Only the mouseClick event is caught
	@Override
	public void mouseClicked(MouseEvent arg0) {
		doStuff();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	// This is raised by the Timer object
	@Override
	public void actionPerformed(ActionEvent arg0) {
		doStuff();
	}
	
	static public void runViewer(Arena mymap, double increment, int timerSpeed) {
		Viewer view = new Viewer(mymap, increment);
		view.setTimer(timerSpeed);
		
//		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(view);
		frame.setName("Viewer");
		frame.setSize(view.getWidth() - 7, view.getHeight() - 15);

		frame.setVisible(true);	
		
		saveImage(frame);
		
	}
	
	public static void saveImage(JFrame frame){
		File folder = new File("Images/" + frame.getName());
		if (!folder.exists()) {
		    System.out.println("creating directory for " + frame.getName());
		    boolean result = false;

		    try{
		        folder.mkdir();
		        result = true;
		    } 
		    catch(SecurityException se){
		        //handle it
		    }        
		    if(result) {    
		        System.out.println("Images/" + frame.getName() + " created");  
		    }
		}
		try
        {
            BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = image.createGraphics();
            frame.paint(graphics2D);
            ImageIO.write(image,"png", new File("Images/" + frame.getName() + "/" + frameNum+".png"));
        }
        catch(Exception exception)
        {
            System.out.println("Something went wrong with the image generation");
        }
	}
	
}
