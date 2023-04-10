package edu.nyu.cs;

import processing.core.PApplet;
import processing.core.PImage;
import java.lang.Math;

public class Flash extends Target {
    
    public Flash(Game app, String imgFilePath, int x, int y) {
        super(app, imgFilePath, x, y);
    }

    public boolean overlaps(int x, int y, int fudgeFactor) {
        return super.overlaps(x, y, fudgeFactor);
    }

}
