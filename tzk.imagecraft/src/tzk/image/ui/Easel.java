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

/**
 * The Easel is a jPanel under the drawing area that allows users to resize the
 * drawing area.
 *
 * It will hopefully be used more later, but we aren't sure what for.
 *
 * @author Drew
 */
public class Easel extends javax.swing.JPanel {

    /**
     * Creates new form Easel
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     *
     * @param args
     */
    public static void main(String[] args) {
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                formMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 585, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 345, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * This checks if the mouse is hovering above one of the 3 resizers.
     * Resizers are small squares at the edge of the easel that users drag to
     * resize the easel and the drawing area.
     *
     * @param evt
     */
    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // finds out if the user clicked the right box, then sets
        // the vertical, horizontal, or corner bools to true.
        if (evt.getX() > width - offset
                && evt.getX() < width + (2 * offset)
                && evt.getY() > (height / 2) - offset
                && evt.getY() < (height / 2) + offset) {
            //right/horizontal
            horizontal = true;
        } else if (evt.getY() > height - offset
                && evt.getY() < height + (2 * offset)
                && evt.getX() > (width / 2) - offset
                && evt.getX() < (width / 2) + offset) {
            //bottom/vertical
            vertical = true;
        } else if (evt.getY() > height - offset
                && evt.getY() < height + (2 * offset)
                && evt.getX() > width - offset
                && evt.getX() < width + (2 * offset)) {
            //corner
            corner = true;
        }        
    }//GEN-LAST:event_formMousePressed

    /**
     * This is where the resize functions get called. When the mouse is
     * released, the easel and drawing area are resized.
     *
     * @param evt
     */
    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased

        // Unset all flags
        horizontal = false;
        vertical = false;
        corner = false;

        System.out.println("Drawing area resized: " + width + ", " + height);
    }//GEN-LAST:event_formMouseReleased

    
    /**
     * This sets the cursor to a drag cursor if it is above a resizer, it changes
     * the cursor. Otherwise it just uses the default cursor.
     * 
     * @param evt 
     */
    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        Cursor newCursor = null;
        if (evt.getX() > width - offset
                && evt.getX() < width + (2 * offset)
                && evt.getY() > (height / 2) - offset
                && evt.getY() < (height / 2) + offset) {
            //right/horizontal
            newCursor = cursorW;
        } else if (evt.getY() > height - offset
                && evt.getY() < height + (2 * offset)
                && evt.getX() > (width / 2) - offset
                && evt.getX() < (width / 2) + offset) {
            //bottom/vertical
            newCursor = cursorN;
        } else if (evt.getY() > height - offset
                && evt.getY() < height + (2 * offset)
                && evt.getX() > width - offset
                && evt.getX() < width + (2 * offset)) {
            //corner
            newCursor = cursorNW;
        } else if (cursor != cursorDefault) {
            // Only change to default if it is not already
            newCursor = cursorDefault;
        }
        // Set newCursor only if it hasn't changed
        if (newCursor != cursor) {
            cursor = newCursor;
            this.setCursor(cursor);
        }
    }//GEN-LAST:event_formMouseMoved

    /**
     * This resizes the drawing area and the easel when the resizers
     * are dragged.
     * 
     * @param evt 
     */
    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
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
    }//GEN-LAST:event_formMouseDragged

    private void formMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseExited
        setCursor();
    }//GEN-LAST:event_formMouseExited

    /**
     * I overrode the paintComponent method so that I could draw in the squares
     * that represent the resizers.
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
        resizedWidth = resizedWidth < imageCraft.drawingArea.getMinWidth() ?
                imageCraft.drawingArea.getMinWidth() : resizedWidth;
        resizedHeight = resizedHeight < imageCraft.drawingArea.getMinHeight() ?
                imageCraft.drawingArea.getMinHeight() : resizedHeight;
        
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

    private ImageCraft imageCraft;
    private int height, width;
    private final int offset = 10;
    private boolean corner, vertical, horizontal;
    private Cursor cursor;
    private final Cursor cursorW = new Cursor(Cursor.W_RESIZE_CURSOR);
    private final Cursor cursorN = new Cursor(Cursor.N_RESIZE_CURSOR);
    private final Cursor cursorNW = new Cursor(Cursor.NW_RESIZE_CURSOR);
    private final Cursor cursorDefault = new Cursor(Cursor.DEFAULT_CURSOR);
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
