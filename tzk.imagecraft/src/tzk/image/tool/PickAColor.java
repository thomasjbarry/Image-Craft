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
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;
import tzk.image.ui.ImageCraft;

/**
 *
 * @author Thomas
 */
public class PickAColor extends SimpleTool {

    public PickAColor(ImageCraft iC) {
        super(iC);

        imageCraft = iC;

        super.setButton(imageCraft.jPick);
    }

    /**
     * Pressed the mouse on the Drawing Area
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
        
        // You are about to drag the mouse        
        dragging = true;
        
        /*
         * set the boolean rightButton to remember the button that we first
         * started pressing so that we can ignore another mouse click 
         * with the other button while drawing
         */        
        rightButton = SwingUtilities.isRightMouseButton(evt);
    }

    /**
     * Dragging the mouse, just ignore until we release the mouse
     *
     * @param evt The MouseEvent that has fired.
     */
    @Override
    public void mouseDragged(MouseEvent evt) {
    }

    /**
     * Get the color from the current X and Y coordinates and set the
     * primary/secondary color in imageCraft
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
        
        //Set pickedColor to white if we selected a purely transparent background
        //otherwise set it to the selected color
        Color pickedColor;
        if (imageCraft.drawingArea1.currentDrawing.getRGB(evt.getX(), evt.getY()) == 0) {
            pickedColor = Color.white;
        } else {
            pickedColor = new Color(imageCraft.drawingArea1.currentDrawing.getRGB(evt.getX(), evt.getY()));
        }
        
        //Set the primary/secondary color to pickedColor
        if (rightButton) {
            imageCraft.secondaryColor = pickedColor;
        } else {
            imageCraft.primaryColor = pickedColor;
        }

        //Repaint the jColorSwatch after changing one of the colors
        imageCraft.jColorSwatch.paintComponent(imageCraft.jColorSwatch.getGraphics());
    }
    
    // Variables declaration
    private final ImageCraft imageCraft;
    private boolean dragging, rightButton;
    // End of variables declaration
}
