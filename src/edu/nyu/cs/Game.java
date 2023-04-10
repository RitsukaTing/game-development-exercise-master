package edu.nyu.cs;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.lang3.SystemUtils;

import processing.core.*; // import the base Processing library
import processing.sound.*; // import the processing sound library

/**
 * Describe your game succinctly here, and update the author info below.
 * Some starter code has been included for your reference - feel free to delete or modify it.
 * 
 * @author Foo Barstein
 * @version 0.1
 */
public class Game extends PApplet {

  private SoundFile soundStartup; // will refer to a sound file to play when the program first starts
  private SoundFile soundClick; // will refer to a sound file to play when the user clicks the mouse
  private PImage imgMe; // will hold a photo of me
  private ArrayList<Target> targets; // will hold an ArrayList of Star objects
  private ArrayList<Flash> flashes;
  private final int POINTS_PER_TARGET = 1; // the number of points to award the user for each star they destroy
  private int score = 0; // the user's score
  private int lives = 10; //how many lives the user has
  private int frames = 60;
  private String level = "normal";
 


	/**
	 * This method will be automatically called by Processing when the program runs.
   * - Use it to set up the initial state of any instance properties you may use in the draw method.
	 */
	public void setup() {
    // set the cursor to crosshairs
    this.cursor(PApplet.CROSS);

    // load up a sound file and play it once when program starts up
		String cwd = Paths.get("").toAbsolutePath().toString(); // the current working directory as an absolute path
		String path = Paths.get(cwd, "sounds", "vibraphon.mp3").toString(); // e.g "sounds/vibraphon.mp3" on Mac/Unix vs. "sounds\vibraphon.mp3" on Windows
    this.soundStartup = new SoundFile(this, path);
    this.soundStartup.play();

    // load up a sound file and play it once when the user clicks
		path = Paths.get(cwd, "sounds", "certainrifle.mp3").toString(); // e.g "sounds/thump.aiff" on Mac/Unix vs. "sounds\thump.aiff" on Windows
    this.soundClick = new SoundFile(this, path); // if you're on Windows, you may have to change this to "sounds\\thump.aiff"
 
    // load up an image of the background
		path = Paths.get(cwd, "images", "certaingame.png").toString(); 
    this.imgMe = loadImage(path);

    // some basic settings for when we draw shapes
    this.ellipseMode(PApplet.CENTER); // setting so ellipses radiate away from the x and y coordinates we specify.
    this.imageMode(PApplet.CENTER); // setting so the ellipse radiates away from the x and y coordinates we specify.

    // makes the array for objects
    targets = new ArrayList<Target>();
    flashes = new ArrayList<Flash>();

	} 

	/**
	 * This method is called automatically by Processing every 1/60th of a second by default.
   * - Use it to modify what is drawn to the screen.
   * - There are methods for drawing various shapes, including `ellipse()`, `circle()`, `rect()`, `square()`, `triangle()`, `line()`, `point()`, etc.
	 */
	public void draw() {
    if (score > 15) {
      background(0);
      textSize(32);
      textAlign(CENTER);
      fill(255);
      text("YOU WIN", width/2, (height/4) + 45);
      text("Press 'r' to restart", width/2, (height*2/4) - 5);
      text("Press 1 for Easy, 2 for Normal, and 3 for Hard", width/2, (height*3/4) - 50);

    } else if (lives <= 0) {
      background(0);
      textSize(32);
      textAlign(CENTER);
      fill(255);
      text("YOU LOSE", width/2, (height/5) - 10);
      text("Press 'r' to restart", width/2, (height*2/5) - 10);
      text("Press 1 for Easy, 2 for Normal, and 3 for Hard", width/2, (height*3/5) - 10);
      String scoreString = String.format("Your score was: %d", score);
      text(scoreString, width/2, (height*4/5) - 10);

    } else {
      // fill the window with solid color
      this.background(0, 0, 0); // fill the background with the specified r, g, b color.

      // show an image of me that wanders around the window
      image(this.imgMe, this.width / 2, this.height/2); // draw image to center of window

      // draw an ellipse at the current position of the mouse
      this.fill(255, 255, 255); // set the r, g, b color to use for filling in any shapes we draw later.
      
      if (this.level.equals("easy")) {
        this.ellipse(this.mouseX, this.mouseY, 90, 90); // draw an ellipse wherever the mouse is
      } else if (this.level.equals("normal")) {
        this.ellipse(this.mouseX, this.mouseY, 45, 45); // draw an ellipse wherever the mouse is
      } else {
        this.ellipse(this.mouseX, this.mouseY, 10, 10); // draw an ellipse wherever the mouse is
      }
    

      if (this.level.equals("easy")) {
        updateTargets(frames);
        updateTargets(this.targets);
      } else {
        updateTargets(frames);
        updateTargets(this.targets);
        updateTargets(this.flashes);
      } 

      // show the score at the bottom of the window
      String scoreString = String.format("SCORE: %d LIVES: %d", score, lives);
      textSize(14);
      text(scoreString, this.width/2, this.height-50);
    }
  }

  private void updateTargets(int reactionTime) {
    String cwd = Paths.get("").toAbsolutePath().toString();
    if (frameCount % reactionTime == 0) {
      String path = Paths.get(cwd, "images", "target.png").toString();
      Target target = new Target(this, path, getRandomNumber(0, this.width), getRandomNumber(0, this.height));
      this.targets.add(target);
    }

    if (frameCount % reactionTime == 0) {
      String path = Paths.get(cwd, "images", "certainflash.png").toString();
      Flash flash = new Flash(this, path, getRandomNumber(0, this.width), getRandomNumber(0, this.height));
      this.flashes.add(flash);
    }
  }
  
  private void updateTargets(List<? extends Target> targets) {
    for (int i = 0; i < targets.size(); i++) {
        Target target = targets.get(i);
        target.timeToLive--;
        target.draw();

        // remove the object from the list if its timeToLive is 0
        if (target.timeToLive == 0) {
            if (target instanceof Flash) {
                this.flashes.remove(i);
            } else {
                lives -= 1;
                this.targets.remove(i);
            }
        }
    }
  }

  private void updateDifficulty(int speed) {
    this.frames = speed;
  }

  private void updateDifficulty(String difficulty) {
    this.level = difficulty;
  }
  
  // randomly generates a number within a specified range
  public int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }

	/**
	 * This method is automatically called by Processing every time the user presses a key while viewing the map.
	 * - The `key` variable (type char) is automatically is assigned the value of the key that was pressed.
	 * - The `keyCode` variable (type int) is automatically is assigned the numeric ASCII/Unicode code of the key that was pressed.
	 */
	public void keyPressed() {
    // the `key` variable holds the char of the key that was pressed, the `keyCode` variable holds the ASCII/Unicode numeric code for that key.
		String scoreString = String.format("SCORE: %d LIVES: %d", score, lives);
    text(scoreString, this.width/2, this.height-50);
    if (key == 114) {
      textSize(12);
    } else if (key == 49) {
      updateDifficulty(100);
      updateDifficulty("easy");
    } else if (key == 50) {
      updateDifficulty(75);
      updateDifficulty("normal");
    } else if (key == 51) {
      updateDifficulty(45);
      updateDifficulty("hard");
    }
    this.lives = 10;
    this.score = 0;
	}  

	/**
	 * This method is automatically called by Processing every time the user clicks a mouse button.
	 * - The `mouseX` and `mouseY` variables are automatically is assigned the coordinates on the screen when the mouse was clicked.
   * - The `mouseButton` variable is automatically assigned the value of either the PApplet.LEFT or PApplet.RIGHT constants, depending upon which button was pressed.
   */
	public void mouseClicked() {
		System.out.println(String.format("Mouse clicked at: %d:%d.", this.mouseX, this.mouseY));

    // check whether we have clicked on a star
    for (int i=0; i<this.targets.size(); i++) {
      Target target = this.targets.get(i); // get the current Star object from the ArrayList
      Flash flash = this.flashes.get(i);
      // check whether the position where the user clicked was within this star's boundaries
      if (target.overlaps(this.mouseX, this.mouseY, 20)) {
        // if so, award the user some points
        target.alive = false;
        score += POINTS_PER_TARGET;        
        // play a thump sound
        this.soundClick.play();
        // delete the star from the ArrayList
        this.targets.remove(target);
      } else if (flash.overlaps(this.mouseX, this.mouseY, 20)) {
        lives -= lives;
      }
    }
	}

	/**
	 * This method is automatically called by Processing every time the user presses down and drags the mouse.
	 * The `mouseX` and `mouseY` variables are automatically is assigned the coordinates on the screen when the mouse was clicked.
   */
	public void mouseDragged() {
		System.out.println(String.format("Mouse dragging at: %d:%d.", mouseX, mouseY));
	}

  /**
   * A method that can be used to modify settings of the window, such as set its size.
   * This method shouldn't really be used for anything else.  
   * Use the setup() method for most other tasks to perform when the program first runs.
   */
  public void settings() {
		size(1200, 800); // set the map window size, using the OpenGL 2D rendering engine
		System.out.println(String.format("Set up the window size: %d, %d.", width, height));    
  }

  /**
   * The main function is automatically called first in a Java program.
   * When using the Processing library, this method must call PApplet's main method and pass it the full class name, including package.
   * You shouldn't need to modify this method.
   * 
   * @param args An array of any command-line arguments.
   */
  public static void main(String[] args) {
    // make sure we're using Java 1.8
		System.out.printf("\n###  JDK IN USE ###\n- Version: %s\n- Location: %s\n### ^JDK IN USE ###\n\n", SystemUtils.JAVA_VERSION, SystemUtils.getJavaHome());
		boolean isGoodJDK = SystemUtils.IS_JAVA_1_8;
		if (!isGoodJDK) {
			System.out.printf("Fatal Error: YOU MUST USE JAVA 1.8, not %s!!!\n", SystemUtils.JAVA_VERSION);
		}
		else {
			PApplet.main("edu.nyu.cs.Game"); // do not modify this!
		}
  }
}
