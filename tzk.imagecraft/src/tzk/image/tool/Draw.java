/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tzk.image.tool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import tzk.image.ui.ImageCraft;

/**
 *
 * @author zach
 */
public class Draw extends SimpleTool {

    public Draw(ImageCraft iC) {
        super(iC);
        
        imageCraft = iC;
        
        super.setButton(imageCraft.jDraw);
    }

    /**
     * Start drawing.
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
        
        // Create a new BufferedImage object
        // This will be sent to SimpleHistory constructor
        currentDrawing = imageCraft.newBlankImage();
        
        //Get the coordinates clicked
        short x = (short) evt.getX();
        short y = (short) evt.getY();

        // You are about to drag the mouse
        dragging = true;

        //Get the graphics objects for the BufferedImage and the JPanel
        drawingGraphics = imageCraft.drawingArea1.getGraphics();
        imageGraphics = currentDrawing.getGraphics();

        /*
         * set the boolean rightButton to remember the button that we first
         * started pressing so that we can ignore another mouse click 
         * with the other button while drawing
         */
        rightButton = SwingUtilities.isRightMouseButton(evt);

        // Get the color that we're working with
        // Left button clicked: use primary color
        // Right button clicked: use secondary color
        Color toColor = (!rightButton ? imageCraft.primaryColor : imageCraft.secondaryColor);

        //Set the BufferedImage color and add the current coordinates &&
        //color to simpleHistoryObject
        imageGraphics.setColor(toColor);

        //Store these mouse coordinates as the previous point
        prevX = x;
        prevY = y;

        //draw the point pressed to the BufferedImage and then to the JPanel
        imageGraphics.drawLine(x, y, x, y);
        drawingGraphics.drawImage(currentDrawing, 0, 0, null);
    }

    /**
     * Move brush.
     *
     * @param evt The MouseEvent that has fired.
     */
    @Override
    public void mouseDragged(MouseEvent evt) {
        // This should not be called if we are not dragging
        if (!dragging) {
            return;
        }

        //Get the coordinates clicked
        short x = (short) evt.getX();
        short y = (short) evt.getY();

        //If we dragged the mouse outside of the JPanel
        //Set the x/y value to the border value
        if (x < 0) {
            x = 0;
        } else {
            // Set x to the lesser of x or the right edge of the drawingArea
            x = (short) Math.min(x, imageCraft.drawingArea1.getWidth() - 1);
        }
        if (y < 0) {
            y = 0;
        } else {
            // Set y to the lesser of y or the bottom edge of the drawingArea
            y = (short) Math.min(y, imageCraft.drawingArea1.getHeight() - 1);
        }

        //Draw a line from the previous point to the current point
        //on the BufferedImage
        imageGraphics.drawLine(prevX, prevY, x, y);

        //Draw the BufferedImage onto the JPanel
        drawingGraphics.drawImage(currentDrawing, 0, 0, null);

        //Current point is now the previous point
        prevX = x;
        prevY = y;
    }

    /**
     * Stop brush and save SimpleHistory.
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
    
    // These fields  hide SimpleTool fields. This is OK.
//    public JToggleButton toolButton;
//    protected String toolImage = "";
//    protected String toolName = "Brush";
    
    private final ImageCraft imageCraft;
    
    private Graphics drawingGraphics, imageGraphics;
    private boolean dragging, rightButton;
    private BufferedImage currentDrawing;
    private short prevX, prevY;
    // End of variables declaration
}
