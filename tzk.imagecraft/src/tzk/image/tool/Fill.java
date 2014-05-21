/* The MIT License (MIT)

 Copyright (c) 2014 Thomas James Barry, Zachary Y. Gateley, Kenneth Drew Gonzales

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package tzk.image.tool;

import java.awt.event.MouseEvent;
import tzk.image.ui.*;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;

/**
 * Fill allows users to paint a whole area of the same color, with a
 * single color. 
 *
 * Contributers:    Thomas James Barry/ thomasbarry92@gmail.com   /5076942
 *                  Zachary Gateley/    zach.cykic@gmail.com      /5415772
 *                  K Drew Gonzales/    drewgonzales360@gmail.com /5470602
 */
public class Fill extends SimpleTool {

    public Fill(ImageCraft iC) {
        super(iC);

        imageCraft = iC;
        super.setButton(imageCraft.jFill);
    }

    /**
     * Bucket fill.
     * If the pixel clicked on is a different color than the current color 
     * from ImageCraft (primaryColor for left mouse button, secondaryColor
     * for right mouse button), change color of this pixel and all adjacent
     * pixels of the same color to the current color.
     * @param evt 
     */
    @Override
    public void mousePressed(MouseEvent evt) {
        // Get the color that we're working with
        // Left button clicked: use primary color
        // Right button clicked: use secondary color
        Color toColor = imageCraft.getPaintColor(!SwingUtilities.isRightMouseButton(evt));
        
        // The point clicked on
        // These integers variables will be reused in our loop
        int x = evt.getX();
        int y = evt.getY();
        
        // The current, visible BufferedImage canvas area
        BufferedImage image = imageCraft.drawingArea.getCurrentDrawing();
        
        // This is the color that we will paint over.
        // Second parameter of Color constructor keeps alpha channel
        Color fromColor = new Color(image.getRGB(x, y), true);
        
        // Make sure that we clicked on a color that is not the color to paint with
        if (toColor.equals(fromColor)) {
            // If it is the same color, do nothing.
            System.out.println("Cannot fill. Clicked same color.");
            System.out.println("toColor: "+toColor);
            System.out.println("fromColor: "+fromColor);
            return;
        }
        
        // FILL
        // Create stack of coordinate points to fill
        // Each coordinate takes up two elements, x on even number, y on odd number
        // For each coordinate in the stack, pull the pen up. Go upwards until 
        // the color is no longer threshold distance from fromColor. 
        // Then put the pen down and start drawing downwards until the color 
        // is no longer threshold distance from fromColor.
        // On the way down, check left and right. If the side color is
        // threshold distance from fromColor, add that point to the stack, 
        // if a point on that unbroken line segment has not already been 
        // added to the stack.
        ArrayList<Integer> stack = new ArrayList<>();
        stack.add(x);
        stack.add(y);
        
        // Create a new BufferedImage object
        // This will be sent to SimpleHistory constructor
        BufferedImage fillImage = imageCraft.newBlankImage();
        Graphics fillGraphics = fillImage.getGraphics();
        
        // The graphics object of the BufferedImage itself
        Graphics imageGraphics = image.getGraphics();
        
        // Set the color of the graphics object to our paint color
        fillGraphics.setColor(toColor);
        imageGraphics.setColor(toColor);
        
        // This will represent the pixel being checked
        Color color;
        
        // colorDistance from fromColor that passes as a pixel to be painted
        // Maybe we can let the user select this?
        int threshold = 50;
        
        // Whether we need to check the left side or the right side
        boolean checkLeft, checkRight;
        
        // Draw the line segment once, not once per pixel
        // This remembers where to start drawing the line
        int yTop;
        
        // Remember image width and height since they are used within the loop
        int width = image.getWidth();
        int height = image.getHeight();
        
        // Loop through each coordinate in the stack
        while (stack.size() > 0) {
            // Get x, y coordinates, the first two elements in the stack
            x = stack.remove(0);
            y = stack.remove(0);
            yTop = y;
            
            // Go to top of line, pen up (not painting)
            while (y > 0) {
                color = new Color(image.getRGB(x, y - 1), true);
                if (colorDistance(color, fromColor) > threshold) {
                    break;
                }
                y--;
            }
            
            // Reset booleans to true. We want to start checking the sides
            // of our line immediately
            checkLeft = true;
            checkRight = true;
            
            // Start painting downwards, checking left and right as we go
            while (y < height) {
                color = new Color(image.getRGB(x, y), true);
                
                // Hit a barrier. Stop.
                if (colorDistance(color, fromColor) > threshold) {
                    break;
                }
                
                // Check left
                // Only check up until the left side of the image
                if (x > 0) {
                    color = new Color(image.getRGB(x - 1, y), true);
                    // If color.equals(toColor), then we have already painted
                    // this pixel
                    if (!color.equals(toColor) &&
                            colorDistance(color, fromColor) <= threshold) {
                        // If we haven't seen a pixel on the left side that we
                        // want to fill yet, be vigilant
                        if (checkLeft) {
                            // We have a match!
                            
                            // Add coordinates to stack
                            stack.add(x - 1);
                            stack.add(y);
                            
                            // Stop looking on the left side
                            checkLeft = false;
                        }
                    }
                    // Otherwise, when we see something that we do *not* want 
                    // to fill, start being vigilant again for a pixel to fill
                    else {
                        checkLeft = true;
                    }
                }
                
                // Check right
                // Only check up to the right side of the image
                if (x < width - 1) {
                    color = new Color(image.getRGB(x + 1, y), true);
                    // If color.equals(toColor), then we have already painted
                    // this pixel
                    if (!color.equals(toColor) &&
                            colorDistance(color, fromColor) <= threshold) {
                        // If we haven't seen a pixel on the right side that we
                        // want to fill yet, be vigilant
                        if (checkRight) {
                            // We have a match!
                            
                            // Add coordinates to stack
                            stack.add(x + 1);
                            stack.add(y);
                            
                            // Stop looking on the right side
                            checkRight = false;
                        }
                    }
                    // Otherwise, when we see something that we do *not* want 
                    // to fill, start being vigilant again for a pixel to fill
                    else {
                        checkRight = true;
                    }
                }
                
                
                // Increment row, get new color for condition and loop
                y++;
            }
            
            // Draw the column's line in both the action image
            // and the currentImage. We need it
            fillGraphics.drawLine(x, yTop, x, y - 1);
            imageGraphics.drawLine(x, yTop, x, y - 1);
        }
        
        // Draw our final image
        imageCraft.drawingArea.getGraphics().drawImage(image, 0, 0, null);
        imageCraft.drawingArea.getGraphics().drawImage(fillImage, 0, 0, null);
        
        // Create new history object in layer
        imageCraft.currentLayer.addHistory(fillImage, "Fill");
    }

    /**
     * Do nothing. Fill only works on mousePressed.
     * @param evt 
     */
    @Override
    public void mouseDragged(MouseEvent evt) {
    }

    /**
     * Do nothing. Fill only works on mousePressed.
     * 
     * @param evt 
     */
    @Override
    public void mouseReleased(MouseEvent evt) {
    }
    
    /**
     * Checks the length of the distance between two colors in RGBA four space.
     * This method plots out the coordinates of the channels of two colors
     * passed as parameters into four space and calculates the length
     * of the line segment connecting the two points.
     * 
     * @param color1
     * @param color2
     * @return 
     */
    private int colorDistance(Color color1, Color color2) {
        int r1 = color1.getRed();
        int g1 = color1.getGreen();
        int b1 = color1.getBlue();
        int a1 = color1.getAlpha();
        int r2 = color2.getRed();
        int g2 = color2.getGreen();
        int b2 = color2.getBlue();
        int a2 = color2.getAlpha();
        
        return (int) Math.sqrt(Math.pow(r2 - r1, 2) + Math.pow(g2 - g1, 2) + Math.pow(b2 - b1, 2) + Math.pow(a2 - a1, 2));
    }

    // Variables declaration
    private final ImageCraft imageCraft;
    // End of variables declaration
}
