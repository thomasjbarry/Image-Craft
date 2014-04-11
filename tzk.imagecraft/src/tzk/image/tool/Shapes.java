/*
 * The MIT License
 *
 * Copyright 2014 cisc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package tzk.image.tool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import tzk.image.ui.ImageCraft;

/**
 *
 * @author cisc
 */
public class Shapes extends SimpleTool {

    public Shapes(ImageCraft iC, String shape) {
        super(iC);

        imageCraft = iC;
        shapeType = shape;

        super.setButton(imageCraft.jShape);
    }

    /**
     * Start drawing the shape of shapeType
     *
     * @param evt The MouseEvent that has fired.
     */
    @Override
    public void mousePressed(MouseEvent evt) {
        // Currently dragging the mouse
        // User must have clicked the other mouse button
        // Ignore        
        if (dragging) {
            return;
        }
        //You are about to drag the mouse
        dragging = true;
        
        /*
         * set the boolean rightButton to remember the button that we first
         * started pressing so that we can ignore another mouse click 
         * with the other button while drawing
         */        
        rightButton = SwingUtilities.isRightMouseButton(evt);
        
        //Store initial point that was clicked
        startX = evt.getX();
        startY = evt.getY();
        
        //Get the graphics for the drawing area
        drawingGraphics = imageCraft.drawingArea1.getGraphics();
    }

    /**
     * Paint shape based on dragged coordinates and initial coordinates
     *
     * @param evt The MouseEvent that has fired.
     */
    @Override
    public void mouseDragged(MouseEvent evt) {
        //Repaint the drawing area before we draw to forget any previous dragged shapes
        imageCraft.drawingArea1.paintComponent(imageCraft.drawingArea1.getGraphics());
        
        //Get the current coordinates endX, endY and make a local copy of startX and startY
        int endX = evt.getX();
        int endY = evt.getY();
        int x = startX;
        int y = startY;
        
        //Set the currentDrawing to the drawingArea's currentDrawing
        currentDrawing = imageCraft.drawingArea1.currentDrawing; 
        
        //Get the graphics for this currentDrawing
        imageGraphics = currentDrawing.getGraphics(); 
        
        //Determine which color to paint with and set the imageGraphics to that color
        Color toColor = (!rightButton ? imageCraft.primaryColor : imageCraft.secondaryColor);        
        imageGraphics.setColor(toColor);        
        
        //If we dragged the mouse outside of the JPanel
        //Set the x/y value to the border value
        if (endX < 0) {
            endX = 0;
        } else {
            // Set x to the lesser of x or the right edge of the drawingArea
            endX = Math.min(endX, imageCraft.drawingArea1.getWidth() - 1);
        }
        if (endY < 0) {
            endY = 0;
        } else {
            // Set y to the lesser of y or the bottom edge of the drawingArea
            endY = Math.min(endY, imageCraft.drawingArea1.getHeight() - 1);
        }
        
        //If we went to the left of our initial point then swap the values
        //of our endX and the local copy of the startX
        if (endX < x) {
            int friend = endX;
            endX = x;
            x = friend;
        }
        
        //If we went above our initial point then swap the values
        //of our endY and the local copy of the startY        
        if (endY < y) {
            int friend = endY;
            endY = y;
            y = friend;
        }
        
        //Check the shapeType and draw the correct shape to this currentDrawing
        //using the adjusted local copy of startX, startY and endX, endY
        if (shapeType.equals("rectangle")) {
            imageGraphics.drawRect(x, y, endX - x, endY - y);
        } else {
            System.out.println("No Shape of type" + shapeType);
            return;
        }
        
        //Draw this currentDrawing to the drawingArea
        drawingGraphics.drawImage(currentDrawing, 0, 0, null);
    }

    /**
     * Add the most recent shape as a SimpleHistory object to the currentLayer
     *
     * @param evt The MouseEvent that has fired.
     */
    @Override
    public void mouseReleased(MouseEvent evt) {
        // If we aren't dragging then we clicked a second mouse button
        // Ignore
        if (!dragging) {
            return;
        }

        // The below code ensures that if we begin
        //dragging with one button and press the other mouse button and then
        //release the other mouse button that it ignores it and continues dragging
        //with the first button that we pressed. You can test this by drawing
        //with the left/right button and then try clicking with the other button
        //while you continue dragging. Without this code it breaks the drawing.
        //With this code we continue drawing
        if (rightButton != SwingUtilities.isRightMouseButton(evt)) {
            return;
        }

        //No longer dragging
        dragging = false;
        
        // Create new history object in layer
        imageCraft.currentLayer.addHistory(currentDrawing);

        // Clean up resources
        drawingGraphics.dispose();
        imageGraphics.dispose();
    }

    // Variables declaration
    private final ImageCraft imageCraft;
    private boolean dragging, rightButton;
    private int startX, startY;
    private final String shapeType;
    private BufferedImage currentDrawing;
    private Graphics imageGraphics, drawingGraphics;
    // End of variables declaration
}
