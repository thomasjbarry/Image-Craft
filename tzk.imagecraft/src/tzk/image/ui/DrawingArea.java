/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tzk.image.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * The drawing area allows users to create their own ImageCrafts where they can
 * draw on, import other images ect.
 *
 * @author Thomas
 */
public class DrawingArea extends javax.swing.JPanel {

    /**
     * Constructor with no arguments. This is needed in order for NetBeans to be
     * able to add DrawingArea as a component. Removal of this makes the Design
     * tab break.
     */
    public DrawingArea() {
        initComponents();
    }

    /**
     * Creates new form DrawingArea.
     *
     * @param iC ImageCraft object that this DrawingArea belongs to
     */
    public DrawingArea(ImageCraft iC) {
        initComponents();

        imageCraft = iC;
        this.updateBackground();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(100, 100));
        setPreferredSize(new java.awt.Dimension(575, 335));
        addMouseListener(new java.awt.event.MouseAdapter() {
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
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    /**
     * These three methods tell the ImageCraft that the mouse is drawing on the
     * drawing area.
     *
     * @param evt
     */
    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        if (imageCraft.currentTool != null) {
            imageCraft.currentTool.mousePressed(evt);
        }
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        if (imageCraft.currentTool != null) {
            imageCraft.currentTool.mouseDragged(evt);
        }
    }//GEN-LAST:event_formMouseDragged

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        if (imageCraft.currentTool != null) {
            imageCraft.currentTool.mouseReleased(evt);
        }
    }//GEN-LAST:event_formMouseReleased

    /**
     * Resizes this entire drawing. Updates all layers, history items, and
     * javax.swing with new size
     *
     * @param x the additional width of the document
     * @param y the additional height of the document
     */
    public void resizeDrawing(int x, int y) {
        //Initialize the resized width and height
        int resizedWidth = (int) this.getPreferredSize().getWidth() + x;
        int resizedHeight = (int) this.getPreferredSize().getHeight() + y;

        //Check that the resized width and height aren't smaller than the minimum values
        resizedWidth = resizedWidth < minWidth ? minWidth : resizedWidth;
        resizedHeight = resizedHeight < minHeight ? minHeight : resizedHeight;

        // Update BufferedImage objects in layers
        for (Layer layer : imageCraft.layerList) {
            for (History historyObj : layer.getHistoryArray()) {
                // finalImage is what this layer looks like after this action is performed
                historyObj.setFinalImage(imageCraft.resize(historyObj.getFinalImage(), resizedWidth, resizedHeight));

                // actionImage contains a snapshot of the action performed only
                // One could potentially add this actionImage to the finalImage 
                // of the last simpleHistory element to get this simpleHistory
                // object's finalImage
                if (historyObj.getActionImage() != null) {
                    historyObj.setActionImage(imageCraft.resize(historyObj.getActionImage(), resizedWidth, resizedHeight));
                }
            }
        }

        // Update this Drawing Area's size, background, and display it properly
        this.setPreferredSize(new Dimension(resizedWidth, resizedHeight));
        this.updateBackground();
        this.revalidate();
        this.repaint();
    }

    /**
     * Create a new temporary workSpace. To remove the workspace, just set it to
     * null
     */
    public void instantiateWorkSpace() {
        workSpace = new BufferedImage(
                this.getWidth(),
                this.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Must have this so that the NetBeans Design tab works.
        if (imageCraft == null) {
            return;
        }

        // Get selected layers from ImageCraft object
        // Paint those layers to a new bufferedImage
        // And draw the buffered image onto the panel
        int[] selectedIndices = imageCraft.layerTree.getSelectionRows();
        currentDrawing = new BufferedImage(this.getWidth(),
                this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        // With LayerTree, layers are in reversed order.
        for (int i = selectedIndices.length - 1; i > -1; i--) {
            int selectedIndex = selectedIndices[i];
            if (imageCraft.layerTree.getClickedLayer(selectedIndex) != null) {
                imageCraft.layerTree.getClickedLayer(selectedIndex).drawLayer();
            } else {
                imageCraft.layerTree.getClickedHistory(selectedIndex).draw(currentDrawing);
            }
        }

        //Draws the backgroundImage onto this DrawingArea
        g.drawImage(backgroundImage, 0, 0, null);

        //Draws the current drawing on top of the background
        g.drawImage(currentDrawing, 0, 0, this);

        // Paint the temporary workspace if it exists
        // Paint this over the currentDrawing so that it is visible
        if (workSpace != null) {
            g.drawImage(workSpace, 0, 0, this);
        }
    }

    /**
     * Updates the background image to the size of this DrawingArea.
     */
    private void updateBackground() {
        //Create a new blank image the size of this DrawingArea
        backgroundImage = new BufferedImage(
                (int) this.getPreferredSize().getWidth(),
                (int) this.getPreferredSize().getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        //Background Image's graphics
        Graphics bgGraphics = backgroundImage.getGraphics();

        //Sets the background of the drawing area to a grid of light/dark gray
        //squares to indicate that it is transparent        
        for (int x = 0; x < this.getPreferredSize().getWidth(); x += 10) {
            for (int y = 0; y < this.getPreferredSize().getHeight(); y += 10) {
                Color shapeColor = (x + y) % 20 == 0
                        ? new Color(85, 85, 85, 175) : new Color(170, 170, 170, 175);
                bgGraphics.setColor(shapeColor);
                bgGraphics.fillRect(x, y, 10, 10);
            }
        }
        //Free resources for background's graphics
        bgGraphics.dispose();
    }

    public BufferedImage getCurrentDrawing() {
        return this.currentDrawing;
    }

    public BufferedImage getWorkSpace() {
        return this.workSpace;
    }

    public void setWorkSpace(BufferedImage image) {
        this.workSpace = image;
    }

    protected int getMinWidth() {
        return minWidth;
    }

    protected int getMinHeight() {
        return minHeight;
    }

    // Variables declaration
    // The current drawing, set by what is selected in LayerList
    private BufferedImage currentDrawing;
    private BufferedImage backgroundImage;
    // A temporary workspace, used with tools such as Shape
    private BufferedImage workSpace;
    private ImageCraft imageCraft;
    private final int minWidth = 15;
    private final int minHeight = 15;
    // End of variables declaration
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
