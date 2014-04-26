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
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author Thomas
 */
public class LayerTree extends JTree {

    //Constructors
    /**
     * Required for NetBeans Design tab.
     */
    public LayerTree() {

    }

    /**
     * Create a new LayerTree object.
     * 
     * @param iC the ImageCraft object this belongs to
     */
    public LayerTree(ImageCraft iC) {
        //Sets the TreeModel to a new DefaultTreeModel with an empty root node,
        //the boolean means that a node is a leaf only if it isn't allowed to have
        //children (which means it doesn't consider an empty layer node a leaf)
        this.setModel(new DefaultTreeModel(new DefaultMutableTreeNode(), true));

        //Hides the root node so we only see the Layers and History Objects
        this.setRootVisible(false);

        //This shows the lines from layer nodes to history nodes
        this.setShowsRootHandles(true);

        imageCraft = iC;

        //Get the TreeModel and root node as fields
        model = (DefaultTreeModel) this.getModel();
        root = (DefaultMutableTreeNode) model.getRoot();

        //creates right click menu and listeners
        initComponents();

        //Sets the CellRenderer to an object of the private class layerTreeCellRenderer
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) new LayerTree.layerTreeCellRenderer();
        setCellRenderer(renderer);

        //Creates ImageIcon for Layers and History objects from file name
        ImageIcon historyIcon = new ImageIcon("src/tzk/image/img/historyIcon.png");
        ImageIcon layerIcon = new ImageIcon("src/tzk/image/img/layerIcon.png");
        renderer.setLeafIcon(historyIcon);
        renderer.setOpenIcon(layerIcon);
        renderer.setClosedIcon(layerIcon);

        // Whether to fire events
        inhibitEvents = false;
    }

    //Classes
    private class layerTreeCellRenderer extends DefaultTreeCellRenderer {

        //Makes a Custom CellRenderer that sets the Font Color to Red for the
        //Current Layer and to Black otherwise.
        @Override
        public Component getTreeCellRendererComponent(JTree jtree, Object o, boolean bln, boolean bln1, boolean bln2, int i, boolean bln3) {
            //Call to DefaultTreeCellRenderer's getTreeCellRendererComponent method
            super.getTreeCellRendererComponent(jtree, o, bln, bln1, bln2, i, bln3);
            //If row i contains a node (this is only null before we've added the first layer)
            if (getPathForRow(i) != null) {

                //Create new childNode for the corresponding row i and it's parentNode
                DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) getPathForRow(i).getLastPathComponent();
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) childNode.getParent();

                //if the childNode is a layer, and the childNode represents the
                //current layer then set the node's text color to Red. Repaint
                //the tree to ensure only the current layer is red.
                if (root.isNodeChild(childNode) && imageCraft.layerList.indexOf(imageCraft.currentLayer) == root.getIndex(childNode)) {
                    setForeground(Color.RED);
                    jtree.repaint();
                } //Else if the childNode is not a layer node, and the parentNode
                //exists (which means this is a History object), and the
                //childNode represents a History object that was not UNDOne
                //then set the node's text color to Blue. Repaint the tree to 
                //ensure only the proper History objects are blue.
                else if (!root.isNodeChild(childNode) && parentNode != null && parentNode.getIndex(childNode) <= imageCraft.layerList.get(root.getIndex(parentNode)).getUndoIndex()) {
                    setForeground(Color.BLUE);
                    jtree.repaint();
                } //Else the node is a layer that is not the current layer, or it 
                //is a History object that was UNDOne. Set the node's text
                //color to Black. Repaint the tree to ensure these are all black.
                else {
                    setForeground(Color.BLACK);
                    jtree.repaint();
                }
            }
            //return the component that renders the tree nodes
            return this;
        }
    }

    //Methods
    private void initComponents() {
        //Initialize RightClick Menu components
        jPopupMenu = new javax.swing.JPopupMenu();
        jMenuItemSetCL = new javax.swing.JMenuItem();
        jMenuItemRename = new javax.swing.JMenuItem();
        jMenuItemMoveUp = new javax.swing.JMenuItem();
        jMenuItemMoveDown = new javax.swing.JMenuItem();
        jMenuItemClearLayer = new javax.swing.JMenuItem();
        jMenuItemDelete = new javax.swing.JMenuItem();

        //Add jMenuItemSetCL to jPopupMenu, set its text, and add an action listener
        //to the menu item. This will set the Current Layer.
        jPopupMenu.add(jMenuItemSetCL);
        jMenuItemSetCL.setText("Set as Current Layer");
        jMenuItemSetCL.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSetCLActionPerformed(evt);
            }
        });

        //Add jMenuItemRename to jPopupMenu, set its text, and add an action listener
        //to the menu item. This will rename the Layer or History object.      
        jPopupMenu.add(jMenuItemRename);
        jMenuItemRename.setText("Rename");
        jMenuItemRename.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRenameActionPerformed(evt);
            }
        });

        //Add jMenuItemMoveUp to jPopupMenu, set its text, and add an action listener
        //to the menu item. This will move the Layer or History object up
        //in the tree.
        jPopupMenu.add(jMenuItemMoveUp);
        jMenuItemMoveUp.setText("Move up");
        jMenuItemMoveUp.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMoveUpActionPerformed(evt);
            }
        });

        //Add jMenuItemMoveDown to jPopupMenu, set its text, and add an action listener
        //to the menu item. This will move the Layer or History object down
        //in the tree.        
        jPopupMenu.add(jMenuItemMoveDown);
        jMenuItemMoveDown.setText("Move down");
        jMenuItemMoveDown.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemMoveDownActionPerformed(evt);
            }
        });

        //Add jMenuItemClearLayer to jPopupMenu, set its text, and add an action listener
        //to the menu item. This will clear the Layer object of all of its SH objects.
        jPopupMenu.add(jMenuItemClearLayer);
        jMenuItemClearLayer.setText("Clear");
        jMenuItemClearLayer.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemClearLayerActionPerformed(evt);
            }
        });

        //Add jMenuItemDelete to jPopupMenu, set its text, and add an action listener
        //to the menu item. This will delete the Layer or History object
        //from the tree.
        jPopupMenu.add(jMenuItemDelete);
        jMenuItemDelete.setText("Delete");
        jMenuItemDelete.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemDeleteActionPerformed(evt);
            }
        });

        //Add a mouse listener (mouseClicked) to this LayerTree that calls
        //the jLayerTreeMouseClicked method
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLayerTreeMouseClicked(evt);
            }
        });

        //Add a tree selection listener (valueChanged) to this LayerTree that
        //calls the jLayerTreeValueChanged method
        addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jLayerTreeValueChanged(evt);
            }
        });
    }

    /**
     * Handles MouseClicked events for this LayerTree.
     * You can either click on a Layer or a History item. 
     * You can either left click or right click.
     * On right click, customize and show menu.
     * 
     * This fires after ValueChanged.
     *
     * @param evt
     */
    private void jLayerTreeMouseClicked(java.awt.event.MouseEvent evt) {
        //Set the boolean isLayer to true if we clicked a Layer and false if we
        //clicked a History
        Layer thisLayer = getClickedLayer(evt.getX(), evt.getY());
        isLayer = thisLayer != null;

        //If we clicked a layer then set the clickedLayer, else set the clickedHistory
        if (isLayer) {
            clickedLayer = thisLayer;
        } else {
            clickedHistory = getClickedHistory(evt.getX(), evt.getY());
        }

        //If you right clicked the LayerList then show the jPopupMenu from
        //where you clicked
        if (SwingUtilities.isRightMouseButton(evt)) {
            // Customize menu depending on what item was clicked
            // Layer
            if (isLayer) {
                //First enable all of the menu items for jPopupMenu, we will
                //disable the ones we don't need.
                enableAllMenuOptions();

                //If the layer you clicked was the last one in this LayerTree
                //then it is the BackgroundLayer which we never rename, move, or
                //delete.
                if (clickedLayer == imageCraft.layerList.get(imageCraft.layerList.size() - 1)) {
                    jMenuItemRename.setEnabled(false);
                    jMenuItemMoveUp.setEnabled(false);
                    jMenuItemMoveDown.setEnabled(false);
                    jMenuItemDelete.setEnabled(false);
                } else {
                    //If the layer you clicked was the first one in this 
                    //LayerTree then we can't move it up.
                    if (clickedLayer == imageCraft.layerList.get(0)) {
                        jMenuItemMoveUp.setEnabled(false);
                    }
                    //If the layer you clicked was the 2nd to last in the 
                    //list then it is directly above the Background Layer, which
                    //means it can never be moved down.
                    // If there is only one layer besides the background layer,
                    // it needs both the MoveUp and MoveDown options disabled.
                    if (clickedLayer == imageCraft.layerList.get(imageCraft.layerList.size() - 2)) {
                        jMenuItemMoveDown.setEnabled(false);
                    }
                }

                System.out.println("You right clicked " + clickedLayer.getLayerName());
            } 
            // History
            else {
                //First enable all of the menu items for jPopupMenu, we will
                //disable the ones we don't need.                
                enableAllMenuOptions();
                
                // ClearLayer and SetCL cannot be used on History items
                jMenuItemClearLayer.setEnabled(false);
                jMenuItemSetCL.setEnabled(false);
                
                // Store the history array locally so that we do not need to 
                // keep calling get methods.
                ArrayList<History> historyList = clickedHistory.getLayer().getHistoryArray();

                System.out.println("History array size: "+historyList.size());
                for (int i = 0; i < historyList.size(); i++) {
                    System.out.println("History item "+i+": "+historyList.get(i).getHistoryName());
                }
                System.out.println("This item: "+clickedHistory.getHistoryName());
                
                //History items output in reverse order
                //If the History you clicked was the first in its layer,
                //then it can't be moved up
                if (clickedHistory == historyList.get(0)) {
                    jMenuItemMoveUp.setEnabled(false);
                } 
                //If the History you clicked was the last in its layer,
                //then it can't be moved down.      
                // If there is only one item, it is the bottom and the top,
                // this must be an if, not an else if
                if (clickedHistory == historyList.get(historyList.size() - 1)) {
                    jMenuItemMoveDown.setEnabled(false);
                } 

                System.out.println("You right clicked " + clickedHistory.getHistoryName() + " in " + clickedHistory.getLayer().getLayerName());
            }
            
            //Show the jPopupMenu at the location you clicked in this LayerTree.
            jPopupMenu.show(this, evt.getX(), evt.getY());
        }
    }

    /**
     * Handles TreeSelection events for this LayerTree. This fires any time a
     * selection changes in the LayerTree, even if the change occurred within
     * this method.
     * 
     * This fires before MouseClicked.
     *
     * @param evt
     */
    private void jLayerTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        // Do not fire layer events - something is already working on it
        if (inhibitEvents) {
            return;
        }
        // This is a flag to say that we are changing items
        // Prevents valueChanged from getting out of hand and into infinite loops
        inhibitEvents = true;
        
        // No selected index - nothing to do
        // Such as before background layer is added to LayerTree
        if (getMinSelectionRow() == -1) {
            inhibitEvents = false;
            return;
        }

        // If value changed, and all were selected, then less than all must now
        // be selected
        // Flag and check appropriately
        boolean allSelected = getSelected().get(0).length == imageCraft.layerList.size();
        imageCraft.selectedAll = allSelected;
        imageCraft.jSelectAllLayers.setSelected(allSelected);
        
        //Get the SelectedNode
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.getSelectionPath().getLastPathComponent();

        //If the selectedNode is a child of the root Node (it is a Layer),
        //then set the currentLayer to the selectedLayer
        if (root.isNodeChild(selectedNode)) {
            imageCraft.currentLayer = imageCraft.layerList.get(this.root.getIndex(selectedNode));
        }
        
        // Turn off layerTree "working" flag
        inhibitEvents = false;

        //Paint the Drawing Area to reflect the change in selection
        imageCraft.drawingArea.paintComponent(imageCraft.drawingArea.getGraphics());
    }

    /**
     * Sets the clickedLayer to the current layer.
     * Fired by clicking "Set as Current Layer" from the menu.
     *
     * @param evt
     */
    private void jMenuItemSetCLActionPerformed(java.awt.event.ActionEvent evt) {
        imageCraft.currentLayer = clickedLayer;

        //Repaint this LayerTree so that the CellRenderer can repaint this node red
        this.repaint();

        System.out.println("The Current Layer is now " + imageCraft.currentLayer.getLayerName());
    }

    /**
     * Renames the clicked Layer or History object.
     * Does not allow layer or history object to have an empty name.
     *
     * @param evt
     */
    private void jMenuItemRenameActionPerformed(java.awt.event.ActionEvent evt) {
        String renameText;
        DefaultMutableTreeNode node = null;
        
        // Layer
        if (isLayer) {
            //Set the renameText to the input given from the InputDialog (by
            //default this is the clickedLayer's current layerName.
            renameText = JOptionPane.showInputDialog("Rename layer", clickedLayer.getLayerName());

            //if the user didn't exit the InputDialog without entering a String
            if (renameText != null) {
                // Update the Layer object itself
                clickedLayer.setLayerName(renameText);

                // Update the LayerTree with the new name
                node = ((DefaultMutableTreeNode) this.root.getChildAt(
                        imageCraft.layerList.indexOf(clickedLayer)));
                node.setUserObject(renameText);
            }
        }
        // History item
        else {
            //Set the renameText to the input given from the InputDialog (by
            //default this is the clickedLayer's current layerName.            
            renameText = JOptionPane.showInputDialog("Rename History", clickedHistory.getHistoryName());

            //if the user didn't exit the InputDialog without entering a String            
            if (renameText != null) {
                // Update the history item itself              
                clickedHistory.setHistoryName(renameText);

                // Update the LayerTree with the new name
                node = ((DefaultMutableTreeNode) this.root.getChildAt(imageCraft.layerList.indexOf(clickedHistory.getLayer()))
                        .getChildAt(clickedHistory.getLayer().getHistoryArray().indexOf(clickedHistory)));
                node.setUserObject(renameText);
            }
        }
        
        // Moving this out of the previous if-then conditions makes this more
        // readable and changes only have to happen in one place, but now
        // the condition must be executed again. Trade offs...
        //notify this LayerTree's TreeModel that the clickedHistory's node
        //has changed to update its UserObject  
        if (renameText != null && node != null) {
            model.nodeChanged(node);
        }
    }

    /**
     * Moves the clicked Layer or History object's node up in this LayerTree.
     *
     * @param evt
     */
    private void jMenuItemMoveUpActionPerformed(java.awt.event.ActionEvent evt) {
        // Store selected rows into selected
        int[] selected = this.getSelectionRows();
        int index;

        //If the clicked node is a layer node then move that layer up
        if (isLayer) {
            //Set the index to the index of the clickedLayer in imageCraft's layerList
            index = imageCraft.layerList.indexOf(clickedLayer);

            //Create a friend Layer to swap the clickedLayer with the one before it
            //in imageCraft's layerList.
            Layer friend = imageCraft.layerList.get(index - 1);
            imageCraft.layerList.set(index - 1, clickedLayer);
            imageCraft.layerList.set(index, friend);

            //Insert the clickedHistory into the previous index in the root node,
            //and notify this LayerTree's TreeModel that the root node's
            //structure has changed. The TreeModel will automatically push the
            //rest of the Layer nodes down in this LayerTree.
            root.insert((DefaultMutableTreeNode) root.getChildAt(index), index - 1);
            model.nodeStructureChanged(root);

            System.out.println("You just moved " + clickedLayer.getLayerName() + " up.");
        } //Else the clicked node is a History node, move it up.
        else {
            //Set the index to the index of the clickedHistory in its layer's historyArray
            index = clickedHistory.getLayer().getHistoryArray().indexOf(clickedHistory);

            //Create a friend History to swap the clickedHistory with the
            //one before it in its layer's historyArray
            History friend = (History) clickedHistory.getLayer().getHistoryArray().get(index - 1);
            clickedHistory.getLayer().getHistoryArray().set(index - 1, clickedHistory);
            clickedHistory.getLayer().getHistoryArray().set(index, friend);

            //Get the clickedHistory's parentNode (its Layer) and its childNode
            //(the clickedHistory node).
            DefaultMutableTreeNode parentNode = ((DefaultMutableTreeNode) root.getChildAt(imageCraft.layerList.indexOf(clickedHistory.getLayer())));
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(index);

            //Insert the clickedHistory into the previous index in its parentNode,
            //and notify this LayerTree's TreeModel that the parentNode's
            //structure has changed. The TreeModel will automatically push the
            //rest of the History nodes down in this LayerTree.
            parentNode.insert(childNode, index - 1);
            model.nodeStructureChanged(parentNode);

            System.out.println("You just moved " + clickedHistory.getHistoryName() + " up.");
        }

        //Edit the selected int[]. If it contained an index of the clicked node
        //or the one that the node swapped with then adjust its index appropriately.
        for (int i = 0; i < selected.length; i++) {
            if (selected[i] == index || selected[i] == index - 1) {
                selected[i] = (selected[i] == index) ? index - 1 : index;
            }
        }
        //Reselect the correct rows in this LayerTree
        this.setSelectionRows(selected);
    }

    /**
     * Moves the clicked Layer or History object's node down in this LayerTree.
     *
     * @param evt
     */
    private void jMenuItemMoveDownActionPerformed(java.awt.event.ActionEvent evt) {
        // Store selected rows into selected
        int[] selected = this.getSelectionRows();
        int index;

        //If the clicked node is a layer node then move that layer down
        if (isLayer) {
            //Set the index to the index of the clickedLayer in imageCraft's layerList
            index = imageCraft.layerList.indexOf(clickedLayer);

            //Create a friend Layer to swap the clickedLayer with the one after it
            //in imageCraft's layerList.
            Layer friend = imageCraft.layerList.get(index + 1);
            imageCraft.layerList.set(index + 1, clickedLayer);
            imageCraft.layerList.set(index, friend);

            //Insert the clickedHistory into the next index in the root node,
            //and notify this LayerTree's TreeModel that the root node's
            //structure has changed. The TreeModel will automatically push the
            //rest of the Layer nodes up in this LayerTree.
            root.insert((DefaultMutableTreeNode) root.getChildAt(index), index + 1);
            model.nodeStructureChanged(root);

            System.out.println("You just moved " + clickedLayer.getLayerName() + " up.");
        } //Else the clicked node is a History node, move it down.
        else {
            //Set the index to the index of the clickedHistory in its layer's historyArray
            index = clickedHistory.getLayer().getHistoryArray().indexOf(clickedHistory);

            //Create a friend History to swap the clickedHistory with the
            //one after it in its layer's historyArray
            History friend = (History) clickedHistory.getLayer().getHistoryArray().get(index + 1);
            clickedHistory.getLayer().getHistoryArray().set(index + 1, clickedHistory);
            clickedHistory.getLayer().getHistoryArray().set(index, friend);

            //Get the clickedHistory's parentNode (its Layer) and its childNode
            //(the clickedHistory node).
            DefaultMutableTreeNode parentNode = ((DefaultMutableTreeNode) root.getChildAt(imageCraft.layerList.indexOf(clickedHistory.getLayer())));
            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(index);

            //Insert the clickedHistory into the next index in its parentNode,
            //and notify this LayerTree's TreeModel that the parentNode's
            //structure has changed. The TreeModel will automatically push the
            //rest of the History nodes up in this LayerTree.
            parentNode.insert(childNode, index + 1);
            model.nodeStructureChanged(parentNode);

            System.out.println("You just moved " + clickedHistory.getHistoryName() + " down.");
        }

        //Edit the selected int[]. If it contained an index of the clicked node
        //or the one that the node swapped with then adjust its index appropriately.
        for (int i = 0; i < selected.length; i++) {
            if (selected[i] == index || selected[i] == index - 1) {
                selected[i] = (selected[i] == index) ? index - 1 : index;
            }
        }
        //Reselect the correct rows in this LayerTree
        this.setSelectionRows(selected);
    }

    /**
     * Clears the clickedLayer, deleting all History objects in its historyArray
     *
     * @param evt
     */
    private void jMenuItemClearLayerActionPerformed(java.awt.event.ActionEvent evt) {
        //Gets the node selected and removes all of the HistoryNodes
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(imageCraft.layerList.indexOf(clickedLayer));
        node.removeAllChildren();
        model.nodeStructureChanged(node);

        //Clear the layer's historyArray to delete the History objects.
        clickedLayer.getHistoryArray().clear();

        //Reset the undoIndex to -1 for this layer because it is now empty
        clickedLayer.setUndoIndex((short) -1);

        //Repaint this LayerTree and the DrawingArea
        this.repaint();
        imageCraft.drawingArea.repaint();

        System.out.println("You just cleared " + clickedLayer.getLayerName());
    }

    /**
     * Deletes the clicked Layer or History object and its node.
     *
     * @param evt
     */
    private void jMenuItemDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        //If the clicked node is a layer node
        if (isLayer) {
            //Call removeLayer method on the clickedLayer to remove it
            this.removeLayer(clickedLayer);

            System.out.println("You just deleted " + clickedLayer.getLayerName());
        } //Else the clicked node is a history node
        else {
            //Call removeHistory method on the clickedHistory to remove it from
            //this LayerTree
            Layer layer = clickedHistory.getLayer();
            this.removeHistory(clickedHistory, clickedHistory.getLayer());

            System.out.println("You just deleted " + clickedHistory.getHistoryName());
            System.out.println("Removed: " + !layer.getHistoryArray().contains(clickedHistory));
        }
    }

    /**
     * Add a Layer to the tree.
     *
     * @param layer Layer object
     */
    protected void addLayer(Layer layer) {
        //Set the childNode (a Layer Object) and parentNode (the root node)         
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(layer.getLayerName());
        DefaultMutableTreeNode parentNode = root;

        //Add the childNode to the parentNode
        parentNode.add(childNode);

        //Insert the childNode into the tree
        model.insertNodeInto(childNode, parentNode, 0);

        //Make the childNode visible
        this.scrollPathToVisible(new TreePath(childNode.getPath()));

        this.setSelectionRow(this.getVisibleRowCount() - 1);
        this.repaint();
    }

    /**
     * Remove a Layer from the tree.
     *
     * @param layer Layer object
     */
    private void removeLayer(Layer layer) {
        //Set the childNode (a Layer Object) and parentNode (the root node)         
        DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) root.getChildAt(imageCraft.layerList.indexOf(layer));
        DefaultMutableTreeNode parentNode = root;

        //Remove the childNode from the tree
        model.removeNodeFromParent(childNode);

        //Remove the Layer from the layerList
        imageCraft.layerList.remove(clickedLayer);

        //Make the childNode visible
        this.scrollPathToVisible(new TreePath(parentNode.getPath()));

        this.setSelectionRow(0);
    }

    /**
     * Add a History object to the tree as a child of the History object's Layer
     *
     * @param history History Object
     * @param layer Layer Object corresponding to the location of the History
     * object
     */
    protected void addHistory(History history, Layer layer) {
        //Set the childNode (a History Object) and parentNode (a Layer Object) 
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(history.getHistoryName());
        DefaultMutableTreeNode parentNode
                = (DefaultMutableTreeNode) treeModel.getChild(root, imageCraft.layerList.indexOf(layer));

        //Set the childNode to not allow children
        childNode.setAllowsChildren(false);
        //Insert the childNode into the tree
        model.insertNodeInto(childNode, parentNode, 0);

        //Add the childNode to the parentNode
        parentNode.add(childNode);

        //Leaves the Layer collapsed
        if (this.isCollapsed(new TreePath(parentNode.getPath()))) {
            this.scrollPathToVisible(new TreePath(childNode.getPath()));
            this.collapsePath(new TreePath(parentNode.getPath()));
        } else {
            this.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        this.repaint();
    }

    /**
     * Remove a History object from the tree as a child of the History object's
     * Layer.
     *
     * @param history History Object
     * @param layer Layer Object corresponding to the location of the History
     * object
     */
    protected void removeHistory(History history, Layer layer) {
        //Set the childNode (a History Object) and parentNode (a Layer Object) 
        DefaultMutableTreeNode parentNode
                = (DefaultMutableTreeNode) root.getChildAt(imageCraft.layerList.indexOf(layer));
        DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) parentNode.getChildAt(layer.getHistoryArray().indexOf(history));

        //Insert the childNode into the tree
        model.removeNodeFromParent(childNode);

        //Notifies this LayerTree's TreeModel that the parentNode's structure changed
        model.nodeStructureChanged(parentNode);

        //Make the parentNode visible
        this.scrollPathToVisible(new TreePath(parentNode.getPath()));

        //Remove the clickedHistory from its layer's historyArray
        history.getLayer().getHistoryArray().remove(history);

        //Reset the clickedHistory's layer's undoIndex so that none of the 
        //remaining History objects in the layer are UNDOne
        history.getLayer().setUndoIndex((short) (history.getLayer().getHistoryArray().size() - 1));

        //Repaint the Drawing Area
        imageCraft.drawingArea.revalidate();
        imageCraft.drawingArea.repaint();
    }

    /**
     * Gets the Clicked Layer based on the click location.
     *
     * @param x
     * @param y
     * @return Layer returns the clicked layer or null if not a layer
     */
    private Layer getClickedLayer(int x, int y) {
        //Get the clicked node
        DefaultMutableTreeNode layerNode = ((DefaultMutableTreeNode) (this.getClosestPathForLocation(x, y)).getLastPathComponent());

        Layer layer;
        try {
            //set layer to the clicked node's corresponding layer
            layer = imageCraft.layerList.get(root.getIndex(layerNode));
        } catch (Exception e) {
            //If the node isn't a Layer then set layer to null
            layer = null;
        }

        return layer;
    }

    /**
     * Gets the clicked Layer based on the row clicked.
     *
     * @param row
     * @return Layer returns the clicked layer or null if not a layer
     */
    protected Layer getClickedLayer(int row) {
        //Gets the node clicked
        DefaultMutableTreeNode layerNode = ((DefaultMutableTreeNode) this.getPathForRow(row).getLastPathComponent());

        Layer layer;
        try {
            //set layer to the clicked node's corresponding layer
            layer = imageCraft.layerList.get(root.getIndex(layerNode));
        } catch (Exception e) {
            //If the node isn't a Layer then set layer to null
            layer = null;
        }

        return layer;
    }

    /**
     * Gets the clicked History based on the location clicked.
     *
     * @param x
     * @param y
     * @return History returns the clicked history item or null if not a history
     */
    private History getClickedHistory(int x, int y) {
        //Gets the historyNode and the layerNode
        DefaultMutableTreeNode historyNode = ((DefaultMutableTreeNode) (this.getClosestPathForLocation(x, y)).getLastPathComponent());
        DefaultMutableTreeNode layerNode = ((DefaultMutableTreeNode) (historyNode.getParent()));

        History history;
        try {
            //set history to the clicked node's corresponding History
            history = (History) imageCraft.layerList.get(root.getIndex(layerNode)).getHistoryArray().get(layerNode.getIndex(historyNode));
        } catch (Exception e) {
            //if the clicked node isn't a History then set history to null
            history = null;
        }

        return history;
    }

    /**
     * Gets the clicked History based on the row clicked.
     *
     * @param row
     * @return History returns the clicked history item or null if not a history
     */
    protected History getClickedHistory(int row) {
        //Gets the historyNode and the layerNode
        DefaultMutableTreeNode historyNode = ((DefaultMutableTreeNode) this.getPathForRow(row).getLastPathComponent());
        DefaultMutableTreeNode layerNode = ((DefaultMutableTreeNode) (historyNode.getParent()));

        History history;
        try {
            //set history to the clicked node's corresponding History
            history = (History) imageCraft.layerList.get(root.getIndex(layerNode)).getHistoryArray().get(layerNode.getIndex(historyNode));
        } catch (Exception e) {
            //if the clicked node isn't a History then set history to null
            history = null;
        }

        return history;
    }

    /**
     * Gets the selected rows and separates them into two int[], one for Layer
     * objects and one for History objects, and returns them in an ArrayList.
     *
     * @return ArrayList<int[]>
     */
    protected ArrayList<int[]> getSelected() {
        //Initialize ArrayList<int[]> to return
        ArrayList<int[]> selected = new ArrayList<>();

        //Initialize int[]s that hold the selected rows and those that will hold
        //the selected layers, and the selected histories
        int[] selectedRows = this.getSelectionRows();
        int[] selectedLayers, selectedHistory;

        //Initialize counters for layers and histories
        int layerNum = 0;
        int historyNum = 0;

        //Accumulate the counters
        for (int i : selectedRows) {
            //If the clicked row is a layer then count layerNum
            if (getClickedLayer(i) != null) {
                layerNum++;
            } //If the clicked row is a History then count historyNum
            else if (getClickedHistory(i) != null) {
                historyNum++;
            } //Else send error message
            else {
                System.out.println("Error: unknown type selected in getSelected()");
            }
        }

        //Create proper size for the int[]s for the selectedLayers and selectedHistory
        if (layerNum > 0) {
            selectedLayers = new int[layerNum];
        } else {
            selectedLayers = new int[1];
        }
        if (historyNum > 0) {
            selectedHistory = new int[historyNum];
        } else {
            selectedHistory = new int[1];
        }

        //Add the rows to the correct int[]
        for (int layer = 0, history = 0, i = 0; i < selectedRows.length && layer < selectedLayers.length && history < selectedHistory.length; i++) {
            if (getClickedLayer(i) != null) {
                selectedLayers[layer++] = i;
            } else if (getClickedHistory(i) != null) {
                selectedHistory[history++] = i;
            } else {
                System.out.println("Error: unknown type selected in getSelectedLayers()");
            }
        }
        //Add the int[]s to the ArrayList<int[]>
        selected.add(selectedLayers);
        selected.add(selectedHistory);

        //return the ArrayList<int[]>
        return selected;
    }

    /**
     * Returns this LayerTree's root node.
     *
     * @return DefaultMutableTreeNode
     */
    protected DefaultMutableTreeNode getRoot() {
        return this.root;
    }

    /**
     * Selects the node corresponding to layer.
     *
     * @param layer Layer whose node is to be selected
     */
    protected void setSelected(Layer layer) {
        //Get layer's node
        DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) imageCraft.layerTree.root.getChildAt(
                imageCraft.layerList.indexOf(layer));

        //Get node's TreePath
        TreePath path = new TreePath(childNode.getPath());

        //Select the TreePath
        this.setSelectionPath(path);
    }
    
    /**
     * Selects the node corresponding to the passed History object.
     * 
     * @param history the History object to select
     */
    private void setSelected(History history) {
        // TODO
    }

    /**
     * Enables all of the MenuItems in jPopupMenu.
     * There is only one JPopupMenu, so there is no need to pass a parameter.
     */
    private void enableAllMenuOptions() {
        //enable each item in the menu's components
        for (Component item : jPopupMenu.getComponents()) {
            item.setEnabled(true);
        }
    }

    //Initialize Variables
    private ImageCraft imageCraft;
    private DefaultTreeModel model;
    private DefaultMutableTreeNode root;
    private Layer clickedLayer;
    private History clickedHistory;
    private JPopupMenu jPopupMenu;
    private JMenuItem jMenuItemSetCL, jMenuItemRename, jMenuItemMoveUp,
            jMenuItemMoveDown, jMenuItemClearLayer, jMenuItemDelete;
    private boolean isLayer;
    protected boolean inhibitEvents;
    //End of Initialize Variables
}
