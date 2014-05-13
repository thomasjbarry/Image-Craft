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
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import tzk.image.tool.*;

/**
 * ImageCraft is an application where users can draw, and edit images. The
 * images that users craft are saveable and can be re- opened in ImageCraft
 *
 *
 * Contributers: Thomas James Barry Zachary Gateley K Drew Gonzales
 */
public class ImageCraft extends JFrame {

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

        allLayersSelected = false;

        // Update the title
        setTitle();

        // Initiatalize ImageCraft tools
        drawTool = new Draw(this);
        fillTool = new Fill(this);
        rectangleShape = new Shapes(this, "Rectangle");
        ovalShape = new Shapes(this, "Oval");
        pickAColor = new PickAColor(this);
        cropTool = new Crop(this);

        // Select default tool
        drawTool.select();

        // Init new IO object
        inputOutput = new IO(this);
    }

    /**
     * Initialize GUI
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        //These listeners ensure the application closes when all JFrames are disposed
        //and not when the first is closed.
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt) {
                formWindowClosing(evt);
            }

            @Override
            public void windowOpened(WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
//        addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent evt) {
//                formComponentResized(evt);
//            }
//        });

        // Default pane for the JFrame
        // Defaults to BorderLayout
        Container pane = getContentPane();

        /////////////////////////////////
        // Build the menu bar
        JMenuBar menuBar = new JMenuBar();
        initMenuBar(menuBar);
        pane.add(menuBar, BorderLayout.NORTH);
        setJMenuBar(menuBar);

        ////////////////////////////////////
        // Build the Split Pane using Border Layout
        JSplitPane splitPane = new JSplitPane();
        pane.add(splitPane, BorderLayout.CENTER);

        //Build the left and right pane with Flow Layout by default
        JPanel leftPane = new JPanel();
        leftPane.setLayout(new BorderLayout());
        leftPane.setMinimumSize(new Dimension(200, 100));
        leftPane.setPreferredSize(new Dimension(200, 418));
        splitPane.add(leftPane, JSplitPane.LEFT);
        JPanel rightPane = new JPanel();
        rightPane.setLayout(new BorderLayout());
//        rightPane.addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent evt) {
//                jRightPaneComponentResized(evt);
//            }
//        });
        splitPane.add(rightPane, JSplitPane.RIGHT);

        //////////////////////////
        // Build the left pane
        initLeftPane(leftPane);

        ///////////////////////
        // Build up the right pane
        JPanel upperCont = new JPanel(new BorderLayout());

        // Build up the list of tools
        JPanel toolCont = new JPanel();
        toolCont.setLayout(new GridLayout());
        toolCont.setMaximumSize(new Dimension(9000, 100));
        initToolBar(toolCont);
        upperCont.add(toolCont, BorderLayout.CENTER);

        // Color swatch
        jColorSwatch = new ColorSwatch(this);
        jColorSwatch.setPreferredSize(new Dimension(56, 56));
        jColorSwatch.setMinimumSize(new Dimension(56, 56));
        upperCont.add(jColorSwatch, BorderLayout.EAST);

        // Drawing area, and its containers
        FlowLayout leftLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        JScrollPane rightScrollPane = new JScrollPane();
        jDesk = new JPanel();
        jDesk.setLayout(leftLayout);
        jDesk.setBackground(new Color(210, 210, 230));
        jEasel = new Easel(this);
        jEasel.setLayout(leftLayout);
//        jEasel.setDoubleBuffered(false);
        jEasel.setBackground(new Color(210, 210, 230));
        jEasel.setPreferredSize(new Dimension(510, 310));
        drawingArea = new DrawingArea(this);
        drawingArea.setPreferredSize(new Dimension(500, 300));

        // Add components together
        jEasel.add(drawingArea);
        jDesk.add(jEasel);
        rightScrollPane.add(jDesk);

        // Build the Right Pane's Scroll Pane
        rightScrollPane.setViewportView(jDesk);
        rightPane.add(rightScrollPane, BorderLayout.SOUTH);

        // Add components. 
        rightPane.add(upperCont, BorderLayout.NORTH);
        rightPane.add(rightScrollPane, BorderLayout.CENTER);

        pack();
    }

    /**
     * Build tool bar. Called from initComponents()
     */
    private void initMenuBar(JMenuBar menuBar) {
        // New ActionListener
        MenuBarHandler handler = new MenuBarHandler();

        // "File" menu
        jFile = new JMenu();
        jFile.setText("File");
        jNew = new JMenuItem();
        jNew.setText("New");
        jNew.addActionListener(handler);
        jNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        jOpen = new JMenuItem();
        jOpen.setText("Open");
        jOpen.addActionListener(handler);
        jOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        jSave = new JMenuItem();
        jSave.setText("Save");
        jSave.addActionListener(handler);
        jSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        jSaveAs = new JMenuItem();
        jSaveAs.setText("Save As");
        jSaveAs.addActionListener(handler);
        jSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        jExport = new JMenuItem();
        jExport.setText("Export");
        jExport.addActionListener(handler);
        jExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
        jExportAs = new JMenuItem();
        jExportAs.setText("Export As");
        jExportAs.addActionListener(handler);
        jExportAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
        jClose = new JMenuItem();
        jClose.setText("Close");
        jClose.addActionListener(handler);
        jQuit = new JMenuItem();
        jQuit.setText("Quit");
        jQuit.addActionListener(handler);
        jQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        jFile.add(jNew);
        jFile.add(jOpen);
        jFile.add(jSave);
        jFile.add(jSaveAs);
        jFile.add(jExport);
        jFile.add(jExportAs);
        jFile.add(jClose);
        jFile.add(jQuit);

        // "Edit" menu
        jEdit = new JMenu();
        jEdit.setText("Edit");
        jImport = new JMenuItem();
        jImport.setText("Import");
        jImport.addActionListener(handler);
        jImport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
        jUndo = new JMenuItem();
        jUndo.setText("Undo");
        jUndo.addActionListener(handler);
        jUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        jRedo = new JMenuItem();
        jRedo.setText("Redo");
        jRedo.addActionListener(handler);
        jRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK));
        jEdit.add(jImport);
        jEdit.add(jUndo);
        jEdit.add(jRedo);

        // "Filter" menu
        jFilter = new JMenu();
        jFilter.setText("Filter");
        jGrayscale = new JMenuItem();
        jGrayscale.setText("Grayscale");
        jGrayscale.addActionListener(handler);
        jNegative = new JMenuItem();
        jNegative.setText("Negative");
        jNegative.addActionListener(handler);
        jSharpen = new JMenuItem();
        jSharpen.setText("Sharpen");
        jSharpen.addActionListener(handler);
        jBlur = new JMenuItem();
        jBlur.setText("Blur");
        jBlur.addActionListener(handler);
        jFilter.add(jGrayscale);
        jFilter.add(jNegative);
        jFilter.add(jSharpen);
        jFilter.add(jBlur);

        // "Help" menu
        jHelp = new JMenu();
        jHelp.setText("Help");
        jAbout = new JMenuItem();
        jAbout.setText("About");
        jAbout.addActionListener(handler);
        jHelp.add(jAbout);

        // Add it all together into jMenuBar
        menuBar.add(jFile);
        menuBar.add(jEdit);
        menuBar.add(jFilter);
        menuBar.add(jHelp);
    }

    /**
     * Listener that handles MenuBar action events.
     */
    private class MenuBarHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem clicked = (JMenuItem) e.getSource();
            if (clicked.equals(jNew)) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // Create new ImageCraft object
                        ImageCraft program = new ImageCraft();

                        // Show ImageCraft object
                        program.setVisible(true);
                    }
                });

            } else if (clicked.equals(jOpen)) {
                // Open a new file
                // Can be ImageCraft format or JPG format
                inputOutput.open();

            } else if (clicked.equals(jSave)) {
                // Save this file in ImageCraft format
                // Overwrites last save file if already performed
                inputOutput.save(false);

            } else if (clicked.equals(jSaveAs)) {
                // Save this file as new ImageCraft file
                // Makes sure this save is unique if already performed
                inputOutput.save(true);

            } else if (clicked.equals(jExport)) {
                // Export as JPG
                // Will overwrite last export if already performed
                inputOutput.export(false);

            } else if (clicked.equals(jExportAs)) {
                // Export as new JPG
                // Parameter true makes sure this export is unique
                inputOutput.export(true);

            } else if (clicked.equals(jClose)) {
                //Dispose of this ImageCraft, if unsaved should check if you want to save
                dispose();
                //If we are closing the only openFrame then exit the application
                if (openFrames == 1) {
                    System.exit(0);
                }
                ImageCraft.openFrames--;

            } else if (clicked.equals(jQuit)) {
                // Close program. If unsaved, it should check to see if we want to save.
                System.exit(0);

            } else if (clicked.equals(jImport)) {
                inputOutput.fileImport();

            } else if (clicked.equals(jUndo)) {
                currentLayer.undo();

            } else if (clicked.equals(jRedo)) {
                currentLayer.redo();

            } else if (clicked.equals(jGrayscale)) {
                currentLayer.addHistory("Grayscale");

            } else if (clicked.equals(jNegative)) {
                currentLayer.addHistory("Negative");

            } else if (clicked.equals(jSharpen)) {
                currentLayer.addHistory("Sharpen");

            } else if (clicked.equals(jBlur)) {
                currentLayer.addHistory("Blur");

            } else if (clicked.equals(jAbout)) {
//                JOptionPane.showMessageDialog(menuBar, 
//                        "Welcome to the best damn ImageCrafting program "
//                                + "you ever did saw.");
            }
        }

    }

    /**
     * Build tool buttons and place into passed JPanel.
     */
    private void initToolBar(JPanel toolCont) {
        ToolBarHandler handler = new ToolBarHandler();
        
        jDraw = new JToggleButton();
        jDraw.setText("Draw");
        jDraw.addActionListener(handler);
        
        
        jFill = new JToggleButton();
        jFill.setText("Fill");
        jFill.addActionListener(handler);
        
        jPick = new JToggleButton();
        jPick.setText("Picker");
        jPick.addActionListener(handler);
        
        jRectangle = new JToggleButton();
        jRectangle.setText("Rectangle");
        jRectangle.addActionListener(handler);
        
        jSize = new JComboBox();
        jSize.setModel(new DefaultComboBoxModel(new String[]
        {"Pen Width: 1", "Pen Width: 2", "Pen Width: 5", "Caligraphy"}));
        jSize.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent evt) {
                jPenChange(evt);
            }
        });       
        
        jOval = new JToggleButton();
        jOval.setText("Oval");
        jOval.addActionListener(handler);
        
        jCrop = new JButton();
        jCrop.setText("Crop");
        jCrop.addActionListener(handler);

        // Build the ToolBar
        toolCont.add(jDraw);
        toolCont.add(jFill);
        toolCont.add(jPick);
        toolCont.add(jRectangle);
        toolCont.add(jOval);
        toolCont.add(jSize);
        toolCont.add(jCrop);
    }

    /**
     * Responds to the selected items in the toolbar.
     */
    private class ToolBarHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AbstractButton clicked = (AbstractButton) e.getSource();

            if (clicked.equals( (JToggleButton) jDraw)) {
                drawTool.select();
            } else if (clicked.equals( (JToggleButton) jFill)){
                fillTool.select();
            } else if (clicked.equals( (JToggleButton) jPick)){
                pickAColor.select();
            } else if (clicked.equals( (JToggleButton) jRectangle)){
                rectangleShape.select();
            } else if (clicked.equals( (JToggleButton) jOval)){
                ovalShape.select();
            } else if (clicked.equals( (JButton) jCrop)){
                cropTool.select();
            }
        }

    }

    /**
     * Initialize components in left side of GUI.
     * @param leftPane JPanel to contain components
     */
    private void initLeftPane(JPanel leftPane) {
        ////////////////
        // Holds "Layers" title and layer tools
        JPanel cont = new JPanel();
        cont.setLayout(new BorderLayout());

        // Holds the title "Layers"
        JPanel northCont = new JPanel();
        // Holds the layer tools
        JPanel southCont = new JPanel();

        //Add a Title to the header panel
        JLabel layerHeader = new JLabel();
        layerHeader.setFont(new Font("Georgia", 1, 24));
        layerHeader.setText("Layers");
        northCont.add(layerHeader);

        // "Select All" checkbox
        jSelectAllLayers = new JCheckBox();
        jSelectAllLayers.setText("Select All");
        jSelectAllLayers.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent evt) {
                jSelectAllLayersItemStateChanged(evt);
            }
        });
        southCont.add(jSelectAllLayers);

        // "New Layer" button
        JButton newLayer = new JButton();
        newLayer.setText("New");
        newLayer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                newLayerMouseClicked(evt);
            }
        });
        southCont.add(newLayer);

        ////////////////////////
        // Border layout 
        cont.add(northCont, BorderLayout.NORTH);
        cont.add(southCont, BorderLayout.SOUTH);
        leftPane.add(cont, BorderLayout.NORTH);

        /////////////////
        // Add a scroll pane for the layer tree
        // Place in CENTER so it automatically sizes
        JScrollPane leftScrollPane = new JScrollPane();
        layerTree = new LayerTree(this);
        leftScrollPane.setViewportView(layerTree);
        leftPane.add(leftScrollPane, BorderLayout.CENTER);
    }

    /**
     * Repaint the current drawing when the split pane has been resized.
     * @param evt handled by Java
     */
    private void jRightPaneComponentResized(ComponentEvent evt) {
        //Draw the BufferedImage to the JPanel
        drawingArea.repaint();
    }

    /**
     * Create and select a new layer.
     * @param evt handled by system
     */
    @SuppressWarnings("unchecked")
    private void newLayerMouseClicked(MouseEvent evt) {
        //Uncheck the SelectAllLayers checkbox, reset the Layer List with the
        //most updated layerNameList, and select the new Layer
        jSelectAllLayers.setSelected(false);

        //Add the layer name to the layerNameList
        //and the layer object to the layerObjectList
        Layer layer = new Layer(this);

        drawingArea.repaint();

        System.out.println("New Layer: " + layer.getLayerName()
                + " ; " + layer);
    }

    /**
     * Update LayerTree when clicked on selectAll button.
     * Ignores if was un/checked by a process.
     * @param evt 
     */
    private void jSelectAllLayersItemStateChanged(ItemEvent evt) {
        // Do not do anything flag
        // LayerTree uses this when setting selectAll checkbox on the fly
        if (layerTree.inhibitEvents) {
            return;
        }

        //If the checkbox is deselected then select the first layer. 
        //Otherwise if the checkbox is selected and there is more than one layer
        //in the layerNameList then select all of the layers.
        if (evt.getStateChange() == ItemEvent.DESELECTED) {
            layerTree.setSelectionRow(0);
        } else if (evt.getStateChange() == ItemEvent.SELECTED
                && this.layerList.size() > 1) {
            int[] indices = new int[this.layerList.size()];

            for (int i = 0; i < layerTree.getRoot().getChildCount(); i++) {
                indices[i] = layerTree.getRowForPath(new TreePath(((DefaultMutableTreeNode) layerTree.getRoot().getChildAt(i)).getPath()));
            }
//            layerList1.setSelectedIndices(indexes);
            layerTree.setSelectionRows(indices);
            allLayersSelected = true;
            jSelectAllLayers.setSelected(true);

        } else {
            System.out.println(
                    "Error: jSelectAllLayers didn't select or deselect");
        }
    }

    /**
     * Check whether to close whole application on window close.
     * @param evt 
     */
    private void formWindowClosing(WindowEvent evt) {
        //If we are closing the only openFrame then exit the application
        if (openFrames == 1) {
            System.exit(0);
        }
        ImageCraft.openFrames--;
    }

    /**
     * Update total number of imageCraft objects on new object.
     * @param evt 
     */
    private void formWindowOpened(WindowEvent evt) {
        ImageCraft.openFrames++;
    }

    /**
     * Update current tool with newly selected pen.
     * @param evt 
     */
    private void jPenChange(ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            if (jSize.getSelectedIndex() == 0) {
                currentTool.setPenStroke("src/tzk/image/img/standardPen_1.png");
                currentTool.setPenWidth(1);
            } else if (jSize.getSelectedIndex() == 1) {
                currentTool.setPenStroke("src/tzk/image/img/standardPen_2.png");
                currentTool.setPenWidth(2);
            } else if (jSize.getSelectedIndex() == 2) {
                currentTool.setPenStroke("src/tzk/image/img/standardPen_5.png");
                currentTool.setPenWidth(5);
            } else {
                currentTool.setPenStroke("src/tzk/image/img/caligraphy_1.png");
                currentTool.setPenWidth(5);
            }
        }
    }

//    /** 
//     * Repaint current drawing on window resize.
//     * @param evt 
//     */
//    private void formComponentResized(ComponentEvent evt) {
//        this.repaint();
//    }

    /**
     * Set the paint color of this ImageCraft object. Does not update
     * ColorSwatch.
     *
     * @param primary whether this is the primary color or the secondary color
     * @param color the new color to select
     */
    public void setPaintColor(boolean primary, Color color) {
        if (primary) {
            primaryColor = color;
        } else {
            secondaryColor = color;
        }
    }

    /**
     * Get the current paint color.
     *
     * @param primary whether to return the primaryColor or secondaryColor
     * @return the proper paint color
     */
    public Color getPaintColor(boolean primary) {
        return (primary ? primaryColor : secondaryColor);
    }

    /**
     * Create and return a new empty BufferedImage with same dimensions
     * as current drawing.
     * @return the new empty image
     */
    public BufferedImage newBlankImage() {
        return new BufferedImage(
                (int) drawingArea.getPreferredSize().getWidth(),
                (int) drawingArea.getPreferredSize().getHeight(),
                BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Grow passed BufferedImage if not as big as current drawing.
     * @param bufferedImage buffered image to resize
     * @param width its width
     * @param height its height
     * @return the correctly sized BufferedImage
     */
    public BufferedImage maxOutImage(BufferedImage bufferedImage, int width, int height) {
        //Only change the dimensions of the BufferedImage if it has increased in
        //size. This ensures data isn't lost by resizing.
        BufferedImage resizedImage = new BufferedImage(
                Math.max(width, bufferedImage.getWidth()),
                Math.max(height, bufferedImage.getHeight()),
                BufferedImage.TYPE_INT_ARGB);
        Graphics resizedGraphics = resizedImage.getGraphics();
        
        // Draw old BufferedImage to new image to be returned
        resizedGraphics.drawImage(bufferedImage, 0, 0, null);
        
        // Free up graphics resources
        resizedGraphics.dispose();
        
        // Return new image
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
     * Access easel.
     * 
     * @return easel
     */
    public Easel getEasel(){
        return jEasel;
    }

    /**
     * Main runnable.
     * 
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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

    ////// Fields //////
    // Class fields
    //Number of current open ImageCraft frames
    private static int openFrames = 0;
    // Use the same colorPicker for every open ImageCraft object
    public static ColorPicker colorPicker = new ColorPicker();
    
    // Object fields
    // Each imageCraft object may have its own colors
    private Color primaryColor = Color.black;
    private Color secondaryColor = Color.white;

    // FileChooser is inside of this object
    private final IO inputOutput;

    // Total number of layers
    protected int numLayer;
    // Contains all layers in this current drawing
    public ArrayList<Layer> layerList;
    // Which layer is selected - see LayerTree
    public Layer currentLayer;
    // Whether all layers are selected within LayerTree, flag
    protected boolean allLayersSelected;

    // Current tool selected
    public SimpleTool currentTool = null;
    // All available tools
    public SimpleTool drawTool, fillTool, rectangleShape, ovalShape, pickAColor, cropTool;

    // Name of the current file
    public String currentTitle = "Untitled";

    // Reused components
    public DrawingArea drawingArea;
    private JMenuItem jAbout;
    private JMenuItem jClose;
    public ColorSwatch jColorSwatch;
    public JButton jCrop;
    protected JPanel jDesk;
    public JToggleButton jDraw;
    private Easel jEasel;
    private JMenu jEdit;
    private JMenuItem jExport;
    private JMenuItem jExportAs;
    private JMenu jFile;
    public JToggleButton jFill;
    private JMenu jFilter;
    private JMenuItem jGrayscale;
    private JMenu jHelp;
    private JMenuItem jImport;
    private JMenuItem jBlur;
    private JMenuItem jNegative;
    private JMenuItem jNew;
    private JMenuItem jOpen;
    public JToggleButton jOval;
    public JToggleButton jPick;
    private JMenuItem jQuit;
    public JToggleButton jRectangle;
    private JMenuItem jRedo;
    private JMenuItem jSave;
    private JMenuItem jSaveAs;
    protected JCheckBox jSelectAllLayers;
    private JMenuItem jSharpen;
    public JComboBox jSize;
    private JMenuItem jUndo;
    public LayerTree layerTree;
}
