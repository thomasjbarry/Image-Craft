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

/**
 * This class is no longer used! It has been replaced with LayerTree.java!
 * 
 * Contributers:    Thomas James Barry/ thomasbarry92@gmail.com   /5076942
 *                  Zachary Gateley/    zach.cykic@gmail.com      /5415772
 *                  K Drew Gonzales/    drewgonzales360@gmail.com /5470602
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@Deprecated
public class LayerList extends JList {

    //Constructors
    public LayerList() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    public LayerList(ImageCraft iC) {
        Vector<String> layers = new Vector<>();
        imageCraft = iC;
        for (Layer layer : imageCraft.layerList) {
            layers.add(layer.getLayerName());
        }
        setListData(layers);
        setSelectedIndex(imageCraft.layerList.size() - 1);
        initComponents();
        setCellRenderer(new currentLayerCellRenderer());
    }

    //Classes
    private class currentLayerCellRenderer extends DefaultListCellRenderer {

        //Makes a Custom CellRenderer that sets the Font Color to Red for the
        //Current Layer and to Black otherwise.
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (imageCraft.layerList.indexOf(imageCraft.currentLayer) == index) {
                setForeground(Color.RED);
            } else {
                setForeground(Color.BLACK);
            }
            return this;
        }
    }

    //Methods
    private void initComponents() {
        jPopupMenu = new javax.swing.JPopupMenu();
        jMenuItemSetCL = new javax.swing.JMenuItem();
        jMenuItemRename = new javax.swing.JMenuItem();
        jMenuItemMoveUp = new javax.swing.JMenuItem();
        jMenuItemMoveDown = new javax.swing.JMenuItem();
        jMenuItemClearLayer = new javax.swing.JMenuItem();
        jMenuItemDeleteLayer = new javax.swing.JMenuItem();
        

        jPopupMenu.add(jMenuItemSetCL);
        jMenuItemSetCL.setText("Set as Current Layer");
        jMenuItemSetCL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                jMenuItemSetCLActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemRename);
        jMenuItemRename.setText("Rename");
        jMenuItemRename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                jMenuItemRenameActionPerformed(evt);
            }
        });        
        jPopupMenu.add(jMenuItemMoveUp);
        jMenuItemMoveUp.setText("Move up");
        jMenuItemMoveUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                jMenuItemMoveUpActionPerformed(evt);
            }
        });
        jPopupMenu.add(jMenuItemMoveDown);
        jMenuItemMoveDown.setText("Move down");
        jMenuItemMoveDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                jMenuItemMoveDownActionPerformed(evt);
            }
        });
        
        jPopupMenu.add(jMenuItemClearLayer);
        jMenuItemClearLayer.setText("Clear");
        jMenuItemClearLayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
               jMenuItemClearLayerActionPerformed(evt);
            }
        });
         
        jPopupMenu.add(jMenuItemDeleteLayer);
        jMenuItemDeleteLayer.setText("Delete");

        jMenuItemDeleteLayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                jMenuItemDeleteLayerActionPerformed(evt);
            }
        });

        setSelectedIndex(0);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                jLayerListMouseClicked(evt);
            }
        });
        addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                jLayerListValueChanged(evt);
            }
        });
    }

    private void jLayerListMouseClicked(MouseEvent evt) {
        //If you right clicked the LayerList then show the PopupMenu from
        //where you clicked and set the clickedLayer to the layer you clicked on
        if (SwingUtilities.isRightMouseButton(evt)) {
            clickedLayer = imageCraft.layerList.get(
                    this.locationToIndex(new Point(evt.getX(), evt.getY())));
            enableAllMenuOptions(jPopupMenu);

            if (clickedLayer == imageCraft.layerList.get(0)) {
                jMenuItemRename.setEnabled(false);
                jMenuItemMoveUp.setEnabled(false);
                jMenuItemMoveDown.setEnabled(false);
                jMenuItemDeleteLayer.setEnabled(false);
            } else {
                if (clickedLayer == imageCraft.layerList.get(1)) {
                    jMenuItemMoveUp.setEnabled(false);
                }
                if (clickedLayer == imageCraft.layerList.get(imageCraft.layerList.size() - 1)) {
                    jMenuItemMoveDown.setEnabled(false);
                }
            }

            jPopupMenu.show(this, evt.getX(), evt.getY());

            System.out.println("You right clicked " + clickedLayer.getLayerName());
        }
    }

    private void jLayerListValueChanged(ListSelectionEvent evt) {
        //If there isn't a selected index don't do anything
        if (getSelectedIndex() != -1) {
            if (imageCraft.allLayersSelected) {
                imageCraft.allLayersSelected = false;
            } else if (imageCraft.jSelectAllLayers.isSelected()) {
                int index = getSelectedIndex();
                imageCraft.jSelectAllLayers.setSelected(false);
                setSelectedIndex(index);
            } else if (getSelectedIndices().length == imageCraft.layerList.size()) {
                imageCraft.allLayersSelected = true;
                imageCraft.jSelectAllLayers.setSelected(true);
            }

            imageCraft.currentLayer = imageCraft.layerList.get(
                    getSelectedIndex());
            this.repaint();
//            Graphics drawingGraphics = imageCraft.drawingArea.getGraphics();
//            imageCraft.drawingArea.paintComponent(drawingGraphics);
//            drawingGraphics.dispose();
            imageCraft.drawingArea.repaint();
        }
    }

    private void jMenuItemSetCLActionPerformed(ActionEvent evt) {
        //If you select the "Set as Current Layer" menu item set the Current
        //Layer to the layer we right clicked on.
        imageCraft.currentLayer = clickedLayer;
        this.repaint();
        System.out.println("The Current Layer is now " +
                imageCraft.currentLayer.getLayerName());
    }
    
    private void jMenuItemRenameActionPerformed(ActionEvent evt) {
        int[] selected = this.getSelectedIndices();
        String renameText = JOptionPane.showInputDialog("Rename layer", clickedLayer.getLayerName());
        if (renameText != null) {
        clickedLayer.setLayerName(renameText);
        this.updateLayerList();
        this.setSelectedIndices(selected);
        this.repaint();
        }
    }    

    private void jMenuItemMoveUpActionPerformed(ActionEvent evt) {
        int[] selected = this.getSelectedIndices();
        int index = imageCraft.layerList.indexOf(clickedLayer);
        Layer friend = imageCraft.layerList.get(index - 1);
        imageCraft.layerList.set(index - 1, clickedLayer);
        imageCraft.layerList.set(index, friend);

        this.updateLayerList();
        this.repaint();

        for (int i = 0; i < selected.length; i++) {
            if (selected[i] == index || selected[i] == index - 1) {
                selected[i] = selected[i] == index ? index - 1 : index;
            }
        }
        this.setSelectedIndices(selected);
        System.out.println("You just moved " + clickedLayer.getLayerName() + " up.");
    }

    private void jMenuItemMoveDownActionPerformed(ActionEvent evt) {
        int[] selected = this.getSelectedIndices();
        int index = imageCraft.layerList.indexOf(clickedLayer);

        Layer friend = imageCraft.layerList.get(index + 1);
        imageCraft.layerList.set(index + 1, clickedLayer);
        imageCraft.layerList.set(index, friend);

        this.updateLayerList();
        this.repaint();

        for (int i = 0; i < selected.length; i++) {
            if (selected[i] == index || selected[i] == index + 1) {
                selected[i] = selected[i] == index ? index + 1 : index;
            }
        }
        this.setSelectedIndices(selected);
        System.out.println("You just moved " + clickedLayer.getLayerName() + " down.");
    }

    private void jMenuItemClearLayerActionPerformed(ActionEvent evt) {
        int[] selected = this.getSelectedIndices();
        clickedLayer.getHistoryArray().clear();
        clickedLayer.setUndoIndex((short) -1);
        this.updateLayerList();
        this.setSelectedIndices(selected);
        this.repaint();
        System.out.println("You just cleared " + clickedLayer.getLayerName());
    }
    
    private void jMenuItemDeleteLayerActionPerformed(ActionEvent evt) {
        imageCraft.layerList.remove(clickedLayer);
        this.updateLayerList();
        this.repaint();
        System.out.println("You just deleted " + clickedLayer.getLayerName());
    }

    @SuppressWarnings("unchecked")
    protected void updateLayerList() {
        // Get layers from Layer class
        Vector<String> layers = new Vector<>();
        for (Layer layer : imageCraft.layerList) {
            layers.add(layer.getLayerName());
        }

        // Update list component, set selected layer
        setListData(layers);
        setSelectedIndex(imageCraft.layerList.size() - 1);
    }

    private void enableAllMenuOptions(JPopupMenu menu) {
        for (Component item : menu.getComponents()) {
            item.setEnabled(true);
        }
    }
    
    protected int[] getSelected() {
        return getSelectedIndices();
    }

    public static void main() {
        //Do Nothing
    }

    //Initialize Variables
    private ImageCraft imageCraft;
    private Layer clickedLayer;
    private JPopupMenu jPopupMenu;
    private JMenuItem jMenuItemSetCL, jMenuItemRename, jMenuItemMoveUp,
            jMenuItemMoveDown, jMenuItemClearLayer, jMenuItemDeleteLayer;
}
