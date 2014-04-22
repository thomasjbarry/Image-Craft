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

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import tzk.image.tool.Draw;
import tzk.image.tool.Fill;
import tzk.image.tool.PickAColor;
import tzk.image.tool.Shapes;
import tzk.image.tool.SimpleTool;

/**
 * ImageCraft is an application where users can draw, and edit images. The images
 * that users craft are saveable and can be re- opened in ImageCraft
 * 
 *
 * Contributers: Thomas James Barry
 *               Zachary Gately
 *               K Drew Gonzales
 */
public class ImageCraft extends javax.swing.JFrame {

    /**
     * Creates new form ImageCraft
     */
    @SuppressWarnings("unchecked")
    public ImageCraft() {
        numLayer = 0;
        layerList = new ArrayList<>();
    
        //Create the GUI components
        initComponents();

        // Create a default background layer
        Layer layer = new Layer(this);
        System.out.println("New Current Layer: "
                + layer.getLayerName() + " ; " + layer);

        selectedAll = false;

        // Update the title
        setTitle();

        // Initiate ImageCraft tools
        drawTool = new Draw(this);
        fillTool = new Fill(this);
        shapesTool = new Shapes(this, "Rectangle");
        pickAColor = new PickAColor(this);

        // Select default tool
        drawTool.select();

        // Init new IO object
        inputOutput = new IO(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane = new javax.swing.JSplitPane();
        jLayersPane = new javax.swing.JPanel();
        newLayer = new javax.swing.JButton();
        jSelectAllLayers = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        layerTree = new tzk.image.ui.LayerTree(this);
        jRightPane = new javax.swing.JPanel();
        jToolBar = new javax.swing.JPanel();
        jDraw = new javax.swing.JToggleButton();
        jFill = new javax.swing.JToggleButton();
        jPick = new javax.swing.JToggleButton();
        jShape = new javax.swing.JToggleButton();
        jSize = new javax.swing.JComboBox();
        jColorSwatch = new ColorSwatch(this);
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jEasel = new tzk.image.ui.Easel(this);
        drawingArea = new DrawingArea(this);
        jMenuBar1 = new javax.swing.JMenuBar();
        jFile = new javax.swing.JMenu();
        jNew = new javax.swing.JMenuItem();
        jOpen = new javax.swing.JMenuItem();
        jSave = new javax.swing.JMenuItem();
        jSaveAs = new javax.swing.JMenuItem();
        jExport = new javax.swing.JMenuItem();
        jExportAs = new javax.swing.JMenuItem();
        jClose = new javax.swing.JMenuItem();
        jQuit = new javax.swing.JMenuItem();
        jEdit = new javax.swing.JMenu();
        jImport = new javax.swing.JMenuItem();
        jUndo = new javax.swing.JMenuItem();
        jRedo = new javax.swing.JMenuItem();
        jFilter = new javax.swing.JMenu();
        jHelp = new javax.swing.JMenu();
        jAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jLayersPane.setMinimumSize(new java.awt.Dimension(180, 100));
        jLayersPane.setPreferredSize(new java.awt.Dimension(180, 418));

        newLayer.setText("New");
        newLayer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newLayerMouseClicked(evt);
            }
        });

        jSelectAllLayers.setText("Select All");
        jSelectAllLayers.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jSelectAllLayersItemStateChanged(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Layers");

        jScrollPane1.setViewportView(layerTree);

        javax.swing.GroupLayout jLayersPaneLayout = new javax.swing.GroupLayout(jLayersPane);
        jLayersPane.setLayout(jLayersPaneLayout);
        jLayersPaneLayout.setHorizontalGroup(
            jLayersPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayersPaneLayout.createSequentialGroup()
                .addComponent(jSelectAllLayers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(newLayer)
                .addGap(49, 49, 49))
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        jLayersPaneLayout.setVerticalGroup(
            jLayersPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayersPaneLayout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jLayersPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSelectAllLayers)
                    .addComponent(newLayer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );

        jSplitPane.setLeftComponent(jLayersPane);

        jRightPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jRightPaneComponentResized(evt);
            }
        });

        jDraw.setText("Draw");
        jDraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDrawActionPerformed(evt);
            }
        });

        jFill.setText("Fill");
        jFill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFillActionPerformed(evt);
            }
        });

        jPick.setText("Picker");
        jPick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPickActionPerformed(evt);
            }
        });

        jShape.setText("Shapes");
        jShape.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jShapeActionPerformed(evt);
            }
        });

        jSize.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Pen Width: 1", "Pen Width: 2", "Pen Width: 5" }));
        jSize.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jSizeItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jColorSwatchLayout = new javax.swing.GroupLayout(jColorSwatch);
        jColorSwatch.setLayout(jColorSwatchLayout);
        jColorSwatchLayout.setHorizontalGroup(
            jColorSwatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );
        jColorSwatchLayout.setVerticalGroup(
            jColorSwatchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jToolBarLayout = new javax.swing.GroupLayout(jToolBar);
        jToolBar.setLayout(jToolBarLayout);
        jToolBarLayout.setHorizontalGroup(
            jToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jToolBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDraw)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFill, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPick, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jShape)
                .addGap(18, 18, 18)
                .addComponent(jSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jColorSwatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jToolBarLayout.setVerticalGroup(
            jToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jToolBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDraw, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPick, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jShape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jToolBarLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jToolBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jToolBarLayout.createSequentialGroup()
                                .addComponent(jColorSwatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSize))))
                .addContainerGap())
        );

        jEasel.setDoubleBuffered(false);
        jEasel.setPreferredSize(new java.awt.Dimension(500, 300));

        drawingArea.setPreferredSize(new java.awt.Dimension(500, 300));
        drawingArea.setLayout(new javax.swing.BoxLayout(drawingArea, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout jEaselLayout = new javax.swing.GroupLayout(jEasel);
        jEasel.setLayout(jEaselLayout);
        jEaselLayout.setHorizontalGroup(
            jEaselLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jEaselLayout.createSequentialGroup()
                .addComponent(drawingArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(215, Short.MAX_VALUE))
        );
        jEaselLayout.setVerticalGroup(
            jEaselLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jEaselLayout.createSequentialGroup()
                .addComponent(drawingArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 715, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jEasel, javax.swing.GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 405, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jEasel, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel1);

        javax.swing.GroupLayout jRightPaneLayout = new javax.swing.GroupLayout(jRightPane);
        jRightPane.setLayout(jRightPaneLayout);
        jRightPaneLayout.setHorizontalGroup(
            jRightPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jRightPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jRightPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jRightPaneLayout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())))
        );
        jRightPaneLayout.setVerticalGroup(
            jRightPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jRightPaneLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        jSplitPane.setRightComponent(jRightPane);

        jFile.setText("File");

        jNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jNew.setText("New");
        jNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNewActionPerformed(evt);
            }
        });
        jFile.add(jNew);

        jOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jOpen.setText("Open");
        jOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOpenActionPerformed(evt);
            }
        });
        jFile.add(jOpen);

        jSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jSave.setText("Save");
        jSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveActionPerformed(evt);
            }
        });
        jFile.add(jSave);

        jSaveAs.setText("Save As");
        jSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSaveAsActionPerformed(evt);
            }
        });
        jFile.add(jSaveAs);

        jExport.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jExport.setText("Export");
        jExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jExportActionPerformed(evt);
            }
        });
        jFile.add(jExport);

        jExportAs.setText("Export As");
        jExportAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jExportAsActionPerformed(evt);
            }
        });
        jFile.add(jExportAs);

        jClose.setText("Close");
        jClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCloseActionPerformed(evt);
            }
        });
        jFile.add(jClose);

        jQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jQuit.setText("Quit");
        jQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jQuitActionPerformed(evt);
            }
        });
        jFile.add(jQuit);

        jMenuBar1.add(jFile);

        jEdit.setText("Edit");

        jImport.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jImport.setText("Import");
        jImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jImportActionPerformed(evt);
            }
        });
        jEdit.add(jImport);

        jUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jUndo.setText("Undo");
        jUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUndoActionPerformed(evt);
            }
        });
        jEdit.add(jUndo);

        jRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jRedo.setText("Redo");
        jRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRedoActionPerformed(evt);
            }
        });
        jEdit.add(jRedo);

        jMenuBar1.add(jEdit);

        jFilter.setText("Filter");
        jMenuBar1.add(jFilter);

        jHelp.setText("Help");

        jAbout.setText("About");
        jAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAboutActionPerformed(evt);
            }
        });
        jHelp.add(jAbout);

        jMenuBar1.add(jHelp);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jRightPaneComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jRightPaneComponentResized
        //Draw the BufferedImage to the JPanel
        drawingArea.paintComponent(drawingArea.getGraphics());
    }//GEN-LAST:event_jRightPaneComponentResized

    @SuppressWarnings("unchecked")
    private void newLayerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newLayerMouseClicked
        //Uncheck the SelectAllLayers checkbox, reset the Layer List with the
        //most updated layerNameList, and select the new Layer
        jSelectAllLayers.setSelected(false);

        //Add the layer name to the layerNameList
        //and the layer object to the layerObjectList
        Layer layer = new Layer(this);

        drawingArea.paintComponent(drawingArea.getGraphics());

        System.out.println("New Layer: " + layer.getLayerName()
                + " ; " + layer);
    }//GEN-LAST:event_newLayerMouseClicked

    private void jSelectAllLayersItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jSelectAllLayersItemStateChanged
        //If the checkbox is deselected then select the first layer. 
        //Otherwise if all layers were selected manually do nothing.
        //Otherwise if the checkbox is selected and there is more than one layer
        //in the layerNameList then select all of the layers.
        if (evt.getStateChange() == java.awt.event.ItemEvent.DESELECTED) {
            layerTree.setSelectionRow(0);
        } else if (layerTree.getSelected().get(0).length == this.layerList.size()) {
        } else if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED
                && this.layerList.size() > 1) {
            int[] indices = new int[this.layerList.size()];

            for (int i = 0; i < layerTree.getRoot().getChildCount(); i++) {
                indices[i] = layerTree.getRowForPath(new TreePath(((DefaultMutableTreeNode) layerTree.getRoot().getChildAt(i)).getPath()));
            }
//            layerList1.setSelectedIndices(indexes);
            layerTree.setSelectionRows(indices);
            selectedAll = true;
            jSelectAllLayers.setSelected(true);

        } else {
            System.out.println(
                    "Error: jSelectAllLayers didn't select or deselect");
        }
    }//GEN-LAST:event_jSelectAllLayersItemStateChanged

    private void jFillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFillActionPerformed
        // TODO add your handling code here:
        fillTool.select();
    }//GEN-LAST:event_jFillActionPerformed

    private void jDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDrawActionPerformed
        // TODO add your handling code here:
        drawTool.select();
    }//GEN-LAST:event_jDrawActionPerformed

    private void jAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAboutActionPerformed
        // Dummy Print Statement--functionality to be added later
        System.out.println("Show \"About\" window");
    }//GEN-LAST:event_jAboutActionPerformed

    private void jRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRedoActionPerformed
        this.currentLayer.redo();
    }//GEN-LAST:event_jRedoActionPerformed

    private void jUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUndoActionPerformed
        this.currentLayer.undo();
    }//GEN-LAST:event_jUndoActionPerformed

    private void jSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveAsActionPerformed
        // Save this file as new ImageCraft file
        // Makes sure this save is unique if already performed
        inputOutput.save(true);
    }//GEN-LAST:event_jSaveAsActionPerformed

    private void jSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSaveActionPerformed
        // Save this file in ImageCraft format
        // Overwrites last save file if already performed
        inputOutput.save(false);
    }//GEN-LAST:event_jSaveActionPerformed

    private void jOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOpenActionPerformed
        // Open a new file
        // Can be ImageCraft format or JPG format

        inputOutput.open();
    }//GEN-LAST:event_jOpenActionPerformed

    private void jExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jExportActionPerformed
        // Export as JPG
        // Will overwrite last export if already performed
        inputOutput.export(false);
    }//GEN-LAST:event_jExportActionPerformed

    private void jExportAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jExportAsActionPerformed
        // Export as new JPG
        // Parameter true makes sure this export is unique
        inputOutput.export(true);
    }//GEN-LAST:event_jExportAsActionPerformed

    private void jQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jQuitActionPerformed
        // Close program. If unsaved, it should check to see if we want to save.
        System.exit(0);
    }//GEN-LAST:event_jQuitActionPerformed

    private void jNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNewActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                // Create new ImageCraft object
                ImageCraft program = new ImageCraft();

                // Show ImageCraft object
                program.setVisible(true);
            }
        });
    }//GEN-LAST:event_jNewActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        //If we are closing the only openFrame then exit the application
        if (openFrames == 1) {
            System.exit(0);
        }
        ImageCraft.openFrames--;
    }//GEN-LAST:event_formWindowClosing

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        ImageCraft.openFrames++;
    }//GEN-LAST:event_formWindowOpened

    private void jCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCloseActionPerformed
        //Dispose of this ImageCraft, if unsaved should check if you want to save
        dispose();

        //If we are closing the only openFrame then exit the application
        if (openFrames == 1) {
            System.exit(0);
        }
        ImageCraft.openFrames--;
    }//GEN-LAST:event_jCloseActionPerformed

    private void jShapeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jShapeActionPerformed
        shapesTool.select();
    }//GEN-LAST:event_jShapeActionPerformed

    private void jPickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPickActionPerformed
        pickAColor.select();
    }//GEN-LAST:event_jPickActionPerformed

    private void jImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jImportActionPerformed
        inputOutput.fileImport();
    }//GEN-LAST:event_jImportActionPerformed

    private void jSizeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jSizeItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (jSize.getSelectedIndex() == 0) {
                currentTool.setPenStroke("src/tzk/image/img/standardPen_1.png");
                currentTool.setPenWidth(1);
            } else if (jSize.getSelectedIndex() == 1) {
                currentTool.setPenStroke("src/tzk/image/img/standardPen_2.png");
                currentTool.setPenWidth(2);                
            }else {
                currentTool.setPenStroke("src/tzk/image/img/standardPen_5.png");
                currentTool.setPenWidth(5);               
            }           
        }
    }//GEN-LAST:event_jSizeItemStateChanged

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        this.repaint();
    }//GEN-LAST:event_formComponentResized

     public BufferedImage newBlankImage() {
        return new BufferedImage(
                (int) drawingArea.getPreferredSize().getWidth(),
                (int) drawingArea.getPreferredSize().getHeight(),
                BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage resize(BufferedImage bufferedImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        resizedImage.getGraphics().drawImage(bufferedImage, 0, 0, null);
        return resizedImage;
    }

    /**
     * Set the title of ImageCraft with new passed title.
     *
     * @param title This is the new title of this file. If it has a path, the
     * path will be removed.
     */
    @Override
    public void setTitle(String title) {
        // Clean up title by stripping the path, and update object field
        // Check for forward or backward slash in name, store which is found to delim
        int lastSlash = -1;
        // Using a single "=" on one side of a comparison operator is a way
        // to tell the next line what condition passed, if the condition amounts to true
        if ((lastSlash = title.lastIndexOf('/')) > -1 || (lastSlash = title.lastIndexOf('\'')) > -1) {
            title = title.substring(lastSlash + 1);
        }
        currentTitle = title;

        // Add ImageCraft tag before setting jFrame title
        title = title.concat(" (ImageCraft)");

        super.setTitle(title);
    }

    /**
     * Set the title of this ImageCraft. Does not set a new currentTitle.
     */
    public void setTitle() {
        setTitle(currentTitle);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ImageCraft.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ImageCraft.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ImageCraft.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ImageCraft.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                // Create new ImageCraft object
                ImageCraft program = new ImageCraft();

                // Show ImageCraft object
                program.setVisible(true);
            }
        });
    }

    // Variables declaration
    // Make colors accessible to other packages
    public Color primaryColor = Color.black;
    public Color secondaryColor = Color.green;

    // Whether all layers are selected
    protected boolean selectedAll;

    // FileChooser is inside of this input-output object
    private final IO inputOutput;

    //Number of New Layers, List of all layers, currently selected layer
    protected int numLayer;
    protected ArrayList<Layer> layerList;
    public Layer currentLayer;

    // Current tool selected
    public SimpleTool currentTool = null;
    // All available tools
    public SimpleTool drawTool, fillTool, shapesTool, pickAColor;

    // Name of the current file
    public String currentTitle = "Untitled";

    //Number of current open ImageCraft frames
    private static int openFrames = 0;

    public static ColorPicker colorPicker = new ColorPicker();

    // End variables declaration

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public tzk.image.ui.DrawingArea drawingArea;
    private javax.swing.JMenuItem jAbout;
    private javax.swing.JMenuItem jClose;
    public tzk.image.ui.ColorSwatch jColorSwatch;
    public javax.swing.JToggleButton jDraw;
    private tzk.image.ui.Easel jEasel;
    private javax.swing.JMenu jEdit;
    private javax.swing.JMenuItem jExport;
    private javax.swing.JMenuItem jExportAs;
    private javax.swing.JMenu jFile;
    public javax.swing.JToggleButton jFill;
    private javax.swing.JMenu jFilter;
    private javax.swing.JMenu jHelp;
    private javax.swing.JMenuItem jImport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jLayersPane;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jNew;
    private javax.swing.JMenuItem jOpen;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JToggleButton jPick;
    private javax.swing.JMenuItem jQuit;
    private javax.swing.JMenuItem jRedo;
    private javax.swing.JPanel jRightPane;
    private javax.swing.JMenuItem jSave;
    private javax.swing.JMenuItem jSaveAs;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    protected javax.swing.JCheckBox jSelectAllLayers;
    public javax.swing.JToggleButton jShape;
    public javax.swing.JComboBox jSize;
    private javax.swing.JSplitPane jSplitPane;
    private javax.swing.JPanel jToolBar;
    private javax.swing.JMenuItem jUndo;
    protected tzk.image.ui.LayerTree layerTree;
    private javax.swing.JButton newLayer;
    // End of variables declaration//GEN-END:variables
}
