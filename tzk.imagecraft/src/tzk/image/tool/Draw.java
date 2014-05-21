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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import tzk.image.ui.ImageCraft;

/**
 * Draw pen tool. Draws images as lines and allows for different pen strokes
 * 
 *
 * Contributers:    Thomas James Barry/ thomasbarry92@gmail.com   /5076942
 *                  Zachary Gateley/    zach.cykic@gmail.com      /5415772
 *                  K Drew Gonzales/    drewgonzales360@gmail.com /5470602
 */
public class Draw extends SimpleTool {

    public Draw(ImageCraft iC) {
        super(iC);

        imageCraft = iC;
        penStroke = "src/tzk/image/img/standardPen_1.png";
        penIndex = 1;

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

        //Adjust this point if outside the borders of the drawing area
        Point point = adjustBorderPoints(x, y);
        x = (short) point.getX();
        y = (short) point.getY();

        // You are about to drag the mouse
        dragging = true;

        //Get the graphics objects for the BufferedImage and the JPanel
        drawingGraphics = imageCraft.drawingArea.getGraphics();
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
        Color toColor = imageCraft.getPaintColor(!rightButton);

        //Try to open the penStroke file and set the penColor to the toColor
        try {
  
            

            BufferedImage readFile = ImageIO.read(new File(penStroke));
            BufferedImage bufferedFile = new BufferedImage(readFile.getWidth(),
                    readFile.getHeight(), BufferedImage.TYPE_INT_ARGB);
            bufferedFile.getGraphics().drawImage(readFile, 0, 0, null);
            pen = setPenColor(bufferedFile, toColor);
        } catch (IOException err) {
            System.out.println("Not a real image..."); // You shmuck
            pen = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB, null);
        }

        imageGraphics.drawImage(pen, x, y, null);

        //Store these mouse coordinates as the previous point
        prevX = x;
        prevY = y;
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

        //Adjust this point if outside the borders of the drawing area
        Point point = adjustBorderPoints(x, y);
        x = (short) point.getX();
        y = (short) point.getY();

        //Use the Bresenham Line Algorithm to find any point in between this and
        //the previous point to also draw
        bresenhamAlgorithm(prevX, prevY, x, y);

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

        //If we begin dragging with one button and press the other mouse button and then
        //release the other mouse button that it ignores it and continues dragging
        //with the first button that we pressed.
        if (rightButton != SwingUtilities.isRightMouseButton(evt)) {
            return;
        }

        //No longer dragging
        dragging = false;

        // Create new history object in layer
        imageCraft.currentLayer.addHistory(currentDrawing, "Draw");

        // Clean up resources
        drawingGraphics.dispose();
        imageGraphics.dispose();
    }

    /**
     * Returns a point within the drawing area so that all points drawn are
     * visible.
     * 
     * @param x coordinate
     * @param y coordinate
     * @return new point
     */
    private Point adjustBorderPoints(short x, short y) {
        //If we dragged the mouse outside of the JPanel
        //Set the x/y value to the border value
        if (x < 0) {
            x = 0;
        } else {
            // Set x to the lesser of x or the right edge of the drawingArea
            x = (short) Math.min(x, imageCraft.drawingArea.getWidth() - (pen != null ? pen.getWidth() : 1));
        }
        if (y < 0) {
            y = 0;
        } else {
            // Set y to the lesser of y or the bottom edge of the drawingArea
            y = (short) Math.min(y, imageCraft.drawingArea.getHeight() - (pen != null ? pen.getHeight() : 1));
        }

        return new Point(x, y);
    }

    /**
     * SetPenColor takes an image and if the background is white enough, it will
     * use the non-white part of the picture as the brush stroke. This will allow
     * users to use any picture with a white background as a pen. 
     * 
     * 
     * @param image
     * @param color
     * @return 
     */
    private BufferedImage setPenColor(BufferedImage image, Color color) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (Color.WHITE.getRGB() - image.getRGB(x, y) < 50) {
                    image.setRGB(x, y, blackTransparent);
                } else {
                    image.setRGB(x, y, color.getRGB());
                }
            }
        }
        return image;
    }

    /**
     * Select this tool.
     */
    @Override
    public void select() {
        super.select();
        //select the tool and then enable the ability to select a size
        imageCraft.jSize.setEnabled(true);
        imageCraft.jSize.setSelectedIndex(penIndex);
    }

    /** 
     * Set new pen stroke width for this tool.
     * 
     * @param filePath location of image in system
     */
    @Override
    public void setPenStroke(String filePath) {
        penStroke = filePath;
        switch (penStroke) {
            case "src/tzk/image/img/standardPen_1.png":
                penIndex = 0;
                break;
            case "src/tzk/image/img/standardPen_2.png":
                penIndex = 1;
                break;
            case "src/tzk/image/img/standardPen_5.png":
                penIndex = 2;
                break;
            case "src/tzk/image/img/caligraphy_1.png":
                penIndex = 3;
                break;
        }
    }

    /**
     * Takes two sets of xy coordinates and properly draws all points in between
     * to give the illusion of a line.
     * @param x1
     * @param y1
     * @param x2
     * @param y2 
     */
    public void bresenhamAlgorithm(int x1, int y1, int x2, int y2) {
        //The range of X and Y
        int deltaX = x2 - x1;
        int deltaY = y2 - y1;

        //Assign the numerator and denomerator of the slope of the line
        //so that it is in the range [-1,1]
        int slopeDenom = Math.max(Math.abs(deltaX), Math.abs(deltaY));
        int slopeNumer = Math.min(Math.abs(deltaX), Math.abs(deltaY));

        //The signs of the slopes depending on the octet that the line is in
        int dX1, dY1, dX2, dY2;

        //Assigns the signs of the slopes according to the octet the line is in
        dX1 = (int) Math.signum(deltaX);
        dX2 = Math.abs(deltaX) > Math.abs(deltaY) ? (int) Math.signum(deltaX) : 0;
        dY1 = (int) Math.signum(deltaY);
        dY2 = Math.abs(deltaX) > Math.abs(deltaY) ? 0 : (int) Math.signum(deltaY);

        //Our error variable that is used to increment the slope's numerator
        //until the slope leaves the range [-1,1]
        int error = (int) .5 * slopeDenom;

        for (int i = 0; i <= slopeDenom; i++) {
            //Draw current pixel with current pen image
            imageGraphics.drawImage(pen, x1, y1, null);

            //update the error value with the slope numerator            
            error += slopeNumer;

            //If the slope is not in the range [-1,1] then subtract 1 from the
            // absolute value of the slope and then add appropriate dX&dY values
            //Else the slope is in range [-1,1] then add appropiate dX&dY values
            if (!(error < slopeDenom)) {
                error -= slopeDenom;
                x1 += dX1;
                y1 += dY1;
            } else {
                x1 += dX2;
                y1 += dY2;
            }
        }
    }

    // Variables declaration
    private final ImageCraft imageCraft;

    private static int alpha = 0, red = 0, green = 0, blue = 0;
    private static int blackTransparent = (alpha << 24) | (red << 16) | (green << 8) | blue;
    private Graphics drawingGraphics, imageGraphics;
    private boolean dragging, rightButton;
    private BufferedImage currentDrawing;
    private short prevX, prevY;
    private BufferedImage pen;
    private String penStroke;
    private int penIndex;
    // End of variables declaration
}
