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
package tzk.image.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.JPanel;

/**
 * The Easel is a jPanel under the drawing area that allows users to resize the
 * drawing area.
 *
 * It will hopefully be used more later, but we aren't sure what for.
 *
 * @author Drew
 */
public class Easel extends JPanel {

    /**
     * Creates new form Easel
     *
     * @deprecated
     */
    public Easel() {
        cursor = cursorDefault;
        initComponents();
    }

    public Easel(ImageCraft iC) {
        cursor = cursorDefault;
        imageCraft = iC;
        initComponents();
    }

    private void initComponents() {
        // Add events
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent evt) {
                easelMouseExited(evt);
            }
            @Override
            public void mousePressed(MouseEvent evt) {
                easelMousePressed(evt);
            }
            @Override
            public void mouseReleased(MouseEvent evt) {
                easelMouseReleased(evt);
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent evt) {
                easelMouseDragged(evt);
            }
            @Override
            public void mouseMoved(MouseEvent evt) {
                easelMouseMoved(evt);
            }
        });
    }

    /**
     * This checks if the mouse is hovering above one of the 3 resizers.
     * Resizers are small squares at the edge of the easel that users drag to
     * resize the easel and the drawing area.
     *
     * @param evt
     */
    private void easelMousePressed(MouseEvent evt) {
        // finds out if the user clicked one of the resize boxes, then sets
        // the vertical, horizontal, or corner bools to true.
        int x = evt.getX();
        int y = evt.getY();
        if (isRight(x, y)) {
            horizontal = true;
        }
        else if (isBottom(x, y)) {
            vertical = true;
        }
        else if (isCorner(x, y)) {
            corner = true;
        }

        //If a resize component is selected then draw the current drawing to the easel
        if (horizontal || vertical || corner) {
            // Store graphics into their own variables so that they can be
            // properly disposed of later on
            Graphics thisGraphics = this.getGraphics();
            Graphics deskGraphics = imageCraft.jDesk.getGraphics();
            
            thisGraphics.drawImage(imageCraft.drawingArea.getCurrentDrawing(), 0, 0, null);
            deskGraphics.drawImage(imageCraft.drawingArea.getCurrentDrawing(), 0, 0, null);
            
            thisGraphics.dispose();
            deskGraphics.dispose();
            
            revalidate();
            repaint();
            imageCraft.jDesk.revalidate();
            imageCraft.jDesk.repaint();
        }
    }

    /**
     * This is where the resize functions get called. When the mouse is
     * released, the easel and drawing area are resized.
     *
     * @param evt
     */
    private void easelMouseReleased(MouseEvent evt) {

        // Unset all flags
        horizontal = false;
        vertical = false;
        corner = false;

        System.out.println("Drawing area resized: " + width + ", " + height);
    }

    /**
     * This sets the cursor to a drag cursor if it is above a resizer, it
     * changes the cursor. Otherwise it just uses the default cursor.
     *
     * @param evt
     */
    private void easelMouseMoved(MouseEvent evt) {
        Cursor newCursor = null;
        int x = evt.getX();
        int y = evt.getY();
        if (isRight(x, y)) {
            newCursor = cursorW;
        }
        else if (isBottom(x, y)) {
            newCursor = cursorN;
        }
        else if (isCorner(x, y)) {
            newCursor = cursorNW;
        }
        else if (cursor != cursorDefault) {
            // Only change to default if it is not already
            newCursor = cursorDefault;
        }
        
        // Set newCursor only if it hasn't changed
        if (newCursor != cursor) {
            cursor = newCursor;
            this.setCursor(cursor);
        }
    }

    /**
     * This resizes the drawing area and the easel when the resizers are
     * dragged.
     *
     * @param evt
     */
    private void easelMouseDragged(MouseEvent evt) {
        if (horizontal) {
            this.resizeEasel(evt.getX() - width, 0);
            imageCraft.drawingArea.resizeDrawing(evt.getX() - width, 0);
        } else if (vertical) {
            this.resizeEasel(0, evt.getY() - height);
            imageCraft.drawingArea.resizeDrawing(0, evt.getY() - height);
        } else if (corner) {
            this.resizeEasel(evt.getX() - width, evt.getY() - height);
            imageCraft.drawingArea.resizeDrawing(evt.getX() - width, evt.getY() - height);
        }
    }

    private void easelMouseExited(MouseEvent evt) {
        setCursor();
    }

    /**
     * I overrode the paintComponent method so that I could draw in the squares
     * that represent the resizers.
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // For NetBeans design tab
        if (imageCraft == null) {
            return;
        }

        width = imageCraft.drawingArea.getWidth() + offset;
        height = imageCraft.drawingArea.getHeight() + offset;

        g.setColor(Color.BLUE);
        g.fillRect(width - 10, (height / 2) - 5, 6, 6);//horizontal resizer
        g.fillRect(width - 10, height - 10, 6, 6);//corner rezier
        g.fillRect((width / 2) - 5, height - 10, 6, 6); //vertical resizer
    }

    /**
     * Resize the easel. Updates javax.swing
     *
     * @param x additional width to add (negative: remove)
     * @param y additional height to add (negative: remove)
     */
    public void resizeEasel(int x, int y) {
        //Initialize the resized width and height
        int resizedWidth = (int) this.getPreferredSize().getWidth() + x;
        int resizedHeight = (int) this.getPreferredSize().getHeight() + y;

        //Check that the resized width and height aren't smaller than the minimum values
        resizedWidth = resizedWidth < imageCraft.drawingArea.getMinWidth()
                ? imageCraft.drawingArea.getMinWidth() : resizedWidth;
        resizedHeight = resizedHeight < imageCraft.drawingArea.getMinHeight()
                ? imageCraft.drawingArea.getMinHeight() : resizedHeight;

        //Update GUI
        this.setPreferredSize(new Dimension(resizedWidth, resizedHeight));
        this.revalidate();
        this.repaint();
    }

    /**
     * Set default cursor.
     */
    protected void setCursor() {
        if (cursor != cursorDefault) {
            cursor = cursorDefault;
            setCursor(cursor);
        }
    }
    
    /**
     * Are the coordinates within the right-side resizer?
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the coordinate is within the right-side resizer
     */
    private boolean isRight(int x, int y) {
        return x > width - offset
                && x < width + (2 * offset)
                && y > (height / 2) - offset
                && y < (height / 2) + offset;
    }
    
    /**
     * Are the coordinates within the bottom-side resizer?
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the coordinate is within the bottom-side resizer
     */
    private boolean isBottom(int x, int y) {
        return y > height - offset
                && y < height + (2 * offset)
                && x > (width / 2) - offset
                && x < (width / 2) + offset;
    }
    
    /**
     * Are the coordinates within the corner resizer?
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the coordinate is within the corner resizer
     */
    private boolean isCorner(int x, int y) {
        return y > height - offset
                && y < height + (2 * offset)
                && x > width - offset
                && x < width + (2 * offset);
    }

    private ImageCraft imageCraft;
    private int height, width;
    private final int offset = 10;
    private boolean corner, vertical, horizontal;
    private Cursor cursor;
    private final Cursor cursorW = new Cursor(Cursor.W_RESIZE_CURSOR);
    private final Cursor cursorN = new Cursor(Cursor.N_RESIZE_CURSOR);
    private final Cursor cursorNW = new Cursor(Cursor.NW_RESIZE_CURSOR);
    private final Cursor cursorDefault = new Cursor(Cursor.DEFAULT_CURSOR);
}
