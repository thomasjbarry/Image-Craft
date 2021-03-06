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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import tzk.image.ui.ImageCraft;

/**
 * Shapes allows users to click and drag to draw rectangles and ovals.
 * 
 * Contributers:    Thomas James Barry/ thomasbarry92@gmail.com   /5076942
 *                  Zachary Gateley/    zach.cykic@gmail.com      /5415772
 *                  K Drew Gonzales/    drewgonzales360@gmail.com /5470602
 */
public class Shapes extends SimpleTool {

    public Shapes(ImageCraft iC, String shape) {
        super(iC);

        imageCraft = iC;
        shapeType = shape;
        penWidth = 2;
        penIndex = 1;
        
        switch (shapeType) {
            case "Rectangle":
                super.setButton(imageCraft.jRectangle);
                break;
            case "Oval":
                super.setButton(imageCraft.jOval);
                break;
            default:
                System.out.println("Error: no button for this shape");
        }

        //Create a new Swing Timer that delays drawing a shape by delay (in milliseconds)
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                dragProcedure();
            }
        });
        //This timer does not repeat (otherwise it will repaint every delay milliseconds
        //until we release the mouse.
        timer.setRepeats(false);

        //Set the initial delay to the delay value
        timer.setInitialDelay(delay);
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
        startX = (short) evt.getX();
        startY = (short) evt.getY();

        // Create a new temporary workspace and save it to this object
        imageCraft.drawingArea.instantiateWorkSpace();
        workSpace = imageCraft.drawingArea.getWorkSpace();
        workSpaceGraphics = (Graphics2D) imageCraft.drawingArea.getWorkSpace().getGraphics();

        //Determine which color to paint with and set the imageGraphics to that color
        Color toColor = imageCraft.getPaintColor(!rightButton);
        workSpaceGraphics.setColor(toColor);
        workSpaceGraphics.setStroke(new BasicStroke(penWidth));

        // Set the backgroundColor so that we can use clearRect
        workSpaceGraphics.setBackground(new Color(0, 0, 0, 0));

        drawingGraphics = imageCraft.drawingArea.getGraphics();
    }

    /**
     * Paint shape based on dragged coordinates and initial coordinates
     *
     * @param evt The MouseEvent that has fired.
     */
    @Override
    public void mouseDragged(MouseEvent evt) {
        //Update the drag event to this event
        dragEvt = evt;

        //If the timer isn't running then restart it (this will trigger a call
        //to dragProcedure())
        //Else the timer is running so we shouldn't repaint with the new evt
        if (!timer.isRunning()) {
            timer.restart();
        }
    }

    /**
     * Add the most recent shape as a SimpleHistory object to the currentLayer
     *
     * @param evt The MouseEvent that has fired.
     */
    @Override
    public void mouseReleased(MouseEvent evt) {
        timer.stop();
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
        imageCraft.currentLayer.addHistory(workSpace, shapeType);

        // Clean up resources
        drawingGraphics.dispose();
        workSpaceGraphics.dispose();
        imageCraft.drawingArea.setWorkSpace(null);
    }

    private void dragProcedure() {
        //Repaint the drawing area before we draw to forget any previous dragged shapes
        imageCraft.drawingArea.repaint();

        // Clear the workSpace image
        // This may be deprecated. Can we update?
        workSpaceGraphics.clearRect(0, 0, workSpace.getWidth(), workSpace.getHeight());

        // Get the top left and bottom right coordinates
        short[] coordinates = points((short) dragEvt.getX(), (short) dragEvt.getY());
        short x1, y1, x2, y2;
        // Left
        x1 = coordinates[0];
        // Top
        y1 = coordinates[1];
        // Right
        x2 = coordinates[2];
        // Bottom
        y2 = coordinates[3];

        // Draw the shape
        switch (shapeType) {
            case "Rectangle":
                // drawRect param: x, y, width, height
                workSpaceGraphics.drawRect(x1, y1, x2 - x1, y2 - y1);
                break;
            case "Oval":
                // drawOval param: x, y, width, height
                workSpaceGraphics.drawOval(x1, y1, x2 - x1, y2 - y1);
                break;
            default:
                System.out.println("No Shape of type" + shapeType);
        }
    }

    private short[] points(short x2, short y2) {
        short x1 = startX;
        short y1 = startY;

        //If we dragged the mouse outside of the JPanel
        //Set the x/y value to the border value
        if (x2 < 0) {
            x2 = 0;
        } else {
            // Set x to the lesser of x or the right edge of the drawingArea
            x2 = (short) Math.min(x2, imageCraft.drawingArea.getWidth() - penWidth);
        }
        if (y2 < 0) {
            y2 = 0;
        } else {
            // Set y to the lesser of y or the bottom edge of the drawingArea
            y2 = (short) Math.min(y2, imageCraft.drawingArea.getHeight() - penWidth);
        }

        // Swap left and right
        // if new point is left of original point
        if (x2 < x1) {
            short friend = x2;
            x2 = x1;
            x1 = friend;
        }

        // Swap top and bottom
        // if new point is above original point
        if (y2 < y1) {
            short friend = y2;
            y2 = y1;
            y1 = friend;
        }

        // Return array of coordinates
        short[] r = {x1, y1, x2, y2};
        return r;
    }

    @Override
    public void select() {
        super.select();
        //select the tool and then enable the ability to select a size
        imageCraft.jSize.setEnabled(true);
        imageCraft.jSize.setSelectedIndex(penIndex);
    }

    /**
     *
     * @param width
     */
    @Override
    public void setPenWidth(int width) {
        penWidth = width;
        if (penWidth == 1) {
            penIndex = 0;
        } else if (penWidth == 2) {
            penIndex = 1;
        } else {
            penIndex = 2;
        }
    }

    // Variables declaration
    private final ImageCraft imageCraft;
    private boolean dragging, rightButton;
    private short startX, startY;
    private final String shapeType;
    private BufferedImage workSpace;
    private Graphics2D workSpaceGraphics;
    private Graphics drawingGraphics;
    private int penWidth, penIndex;
    private final Timer timer;
    private final int delay = 15;
    private MouseEvent dragEvt;
    // End of variables declaration
}
