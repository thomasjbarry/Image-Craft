/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tzk.image.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * The drawing area allows users to create their own
 * ImageCrafts where they can draw on, import other images ect.
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
     * Resizes this entire drawing.
     * Updates all layers, history items, and javax.swing with new size
     *
     * @param x the additional width of the document
     * @param y the additional height of the document
     */
    public void resizeDrawing(int x, int y) {
        // Update BufferedImage objects in layers
        for (Layer layer : imageCraft.layerList) {
            for (Object obj : layer.getHistoryArray()) {
                SimpleHistory simpleHistoryObj = (SimpleHistory) obj;
                // finalImage is what this layer looks like after this action is performed
                simpleHistoryObj.setFinalImage(imageCraft.resize(simpleHistoryObj.getFinalImage(),
                        simpleHistoryObj.getFinalImage().getWidth() + x,
                        simpleHistoryObj.getFinalImage().getHeight() + y));

                // actionImage contains a snapshot of the action performed only
                // One could potentially add this actionImage to the finalImage 
                // of the last simpleHistory element to get this simpleHistory
                // object's finalImage
                simpleHistoryObj.setActionImage(imageCraft.resize(simpleHistoryObj.getActionImage(),
                        simpleHistoryObj.getActionImage().getWidth() + x,
                        simpleHistoryObj.getActionImage().getHeight() + y));
            }
        }
        // Update swing
        this.setPreferredSize(new Dimension(
                (int) this.getPreferredSize().getWidth() + x,
                (int) this.getPreferredSize().getHeight() + y));
        this.revalidate();
        this.repaint();
    }
    
    /**
     * Create a new temporary workSpace.
     * To remove the workspace, just set it to null
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
        int[] selectedIndices = imageCraft.layerTree1.getSelectionRows();
        currentDrawing = new BufferedImage(this.getWidth(),
                this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        // With LayerTree, layers are in reversed order.
        for (int i = selectedIndices.length - 1; i > -1; i--) {
            int selectedIndex = selectedIndices[i];
            if (imageCraft.layerTree1.getClickedLayer(selectedIndex) != null) {
                imageCraft.layerTree1.getClickedLayer(selectedIndex).drawLayer();
            } else {
                imageCraft.layerTree1.getClickedHistory(selectedIndex).draw(currentDrawing);
            }
        }
        g.drawImage(currentDrawing, 0, 0, this);
        
        // Paint the temporary workspace if it exists
        // Paint this over the currentDrawing so that it is visible
        if (workSpace != null) {
            g.drawImage(workSpace, 0, 0, this);
        }
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

    // Variables declaration
    // The current drawing, set by what is selected in LayerList
    private BufferedImage currentDrawing;
    // A temporary workspace, used with tools such as Shape
    private BufferedImage workSpace;
    private ImageCraft imageCraft;
    // End of variables declaration
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
