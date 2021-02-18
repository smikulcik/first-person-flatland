package simonGraphics;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;

/**
 * Interface for all drawable objects
 * 
 * @author Simon Mikulcik
 * date: 4/27/2014
 *
 */

public interface Drawable{
	public void draw(Graphics2D g, PointD worldCenter);
}
