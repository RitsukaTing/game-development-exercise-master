package edu.nyu.cs;

import processing.core.PApplet;
import processing.core.PImage;
import java.lang.Math;

public class Target {
    // instance properties
    Game app; // will hold a reference to the main Game object
    PImage img; // will hold a reference to an image of a star
    int x; // will hold the x coordinate of this object on the screen
    int y; // will hold the y coordinate of this object on the screen
    int timeToLive;
    boolean alive;


    /**
     * Constructor to create a Star object at a specific position on the screen
     * @param app a reference to the Game object that created this object
     * @param x the x coordinate of this object on the screen
     * @param y the y coordinate of this object on the screen
     */
    public Target(Game app, String imgFilePath, int x, int y) {

        this.app = app; // store a reference to the main game object

        // load the specified image
        this.img = app.loadImage(imgFilePath);

        // store the x and y coordinates of this object on the screen
        this.x = x;
        this.y = y;
        this.timeToLive = 60;
        this.alive = true;
    }

    /**
     * Draw this star's image to the screen at the appropriate coordinates
     */
    public void draw() {
        // draw this object's image at its x and y coordinates
        
        this.app.imageMode(PApplet.CENTER); // setting so the image is drawn centered on the specified x and y coordinates
        this.app.image(this.img, this.x, this.y);
    }

    public void undoDraw() {
        // get the dimensions of the image
        int imgWidth = this.img.width;
        int imgHeight = this.img.height;
        
        // get a region of the background image that covers the area of the drawn image
        PImage bgRegion = this.app.get(this.x - imgWidth/2, this.y - imgHeight/2, imgWidth, imgHeight);
        
        // draw the background region back onto the canvas to "undo" the image drawing
        this.app.image(bgRegion, this.x - imgWidth/2, this.y - imgHeight/2);
        
    }


    /**
     * Determines whehter a given x, y coordinate overlaps with this Star.
     * @param x The x coordinate of interest.
     * @param y The y coordinate of interest.
     * @param fudgeFactor An amount by which to expand the area we consider overlap
     * @return Boolean true if the x,y coordinate overlaps with this star, false otherwise.
     */
    public boolean overlaps(int x, int y, int fudgeFactor) {
        // get the coordinates of all edges of this Star's image
        int l = this.x - this.img.width/2 - fudgeFactor; // the left edge's x coord
        int r = this.x + this.img.width/2 + fudgeFactor; // the right edge's x coord
        int t = this.y - this.img.height/2 - fudgeFactor; // the top edge's y coord
        int b = this.y + this.img.height/2 + fudgeFactor; // the bottom edge's y coord
        // return whether the x,y coords are within the bounds of this Star's image
        return (x > l && x < r && y > t && y < b);
    }

}
