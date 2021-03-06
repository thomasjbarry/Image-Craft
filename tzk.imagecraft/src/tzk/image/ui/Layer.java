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
 * The user can make as many layers as they want. Each has their own buffered
 * image. 
 * 
 * Contributers:    Thomas James Barry/ thomasbarry92@gmail.com   /5076942
 *                  Zachary Gateley/    zach.cykic@gmail.com      /5415772
 *                  K Drew Gonzales/    drewgonzales360@gmail.com /5470602
 */
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;

public class Layer {

    public Layer(ImageCraft iC) {
        imageCraft = iC;
        if (imageCraft.numLayer == 0) {
            layerName = "Background Layer";
            imageCraft.numLayer++;
        } else {
            layerName = "Layer " + imageCraft.numLayer++;
        }

        initLayer();

        // Add this layer to the list of layers
        // Initialize ArrayList historyArray which keeps track of history objects
        // Each Layer object has its own unique historyArray
        historyArray = new ArrayList<>();

        //UndoIndex starts at 0
        undoIndex = -1;
    }

    // draws all objects in a layer to a BufferedImage object passed as a param
    protected void drawLayer() {
        if (historyArray != null && historyArray.size() > 0 && undoIndex > -1) {
            BufferedImage snapshot = ((History) historyArray.get(undoIndex)).getFinalImage();
            Graphics currentGraphics = imageCraft.drawingArea.getCurrentDrawing().getGraphics();
            Graphics areaGraphics = imageCraft.drawingArea.getGraphics();
            currentGraphics.drawImage(
                    snapshot, 0, 0, null);
            areaGraphics.drawImage(
                    imageCraft.drawingArea.getCurrentDrawing(), 0, 0, null);
            currentGraphics.dispose();
            areaGraphics.dispose();
        }
    }
    
    /**
     * Get a snapshot of the entire layer.
     * Takes into account undos
     * 
     * @return layer snapshot
     */
    public BufferedImage getSnapshot() {
        return getSnapshot(Math.min(historyArray.size() - 1, undoIndex));
    }

    /**
     * Return a snapshot of a history item in this layer.
     * Gets the finalImage from a specific history object in this layer.
     * 
     * @param index
     * @return the snapshot
     */
    public BufferedImage getSnapshot(int index) {
        if (index > -1 && index < historyArray.size()) {
//        if (historyArray.size() > 0) {
            // Return a copy of the last snapshot
            BufferedImage image = (historyArray.get(index)).getFinalImage();
            ColorModel model = image.getColorModel();
            WritableRaster raster = image.copyData(null);
            return new BufferedImage(model, raster, model.isAlphaPremultiplied(), null);
        } else {
            // No history, return blank image
            return imageCraft.newBlankImage();
        }
    }

    public void initLayer() {
        // Add layer to top of the list
        imageCraft.layerList.add(0, this);

        //Add this new layer to the layerTree and select it
        imageCraft.layerTree.addLayer(this);
        imageCraft.layerTree.setSelected(this);

        // Make this new layer the current layer
        imageCraft.currentLayer = this;
    }

    /**
     * Create new History object and add to historyArray.
     *
     * @param image
     * @param historyType
     */
    public void addHistory(BufferedImage image, String historyType) {
        //For all indices after the undoIndex delete the History object
        int index = undoIndex;
        for (int i = historyArray.size() - 1; i > index; i--) {
            System.out.println("UNDO " + undoIndex + " i " + i);
            //Removes history object from both the layer tree and History Array
            imageCraft.layerTree.removeHistory(historyArray.get(i), this);
            System.out.println("AFTER i" + i);
        }
        History history = new History(imageCraft, this, image, historyType);
        undoIndex++;
        imageCraft.layerTree.addHistory(history, this);
        historyArray.add(history);
        BufferedImage currentDrawing = imageCraft.drawingArea.getCurrentDrawing();
        Graphics currentGraphics = currentDrawing.getGraphics();
        currentGraphics.clearRect(0, 0, currentDrawing.getWidth(), currentDrawing.getHeight());        
        currentGraphics.drawImage(
                image, 0, 0, null);
        currentGraphics.dispose();

        imageCraft.drawingArea.repaint();
    }

    /**
     * Create new History object and add to historyArray.
     *
     * @param historyType
     */
    public void addHistory(String historyType) {
        int index = undoIndex;
        
        //For all indices after the undoIndex delete the History object
        for (int i = historyArray.size() - 1; i > index; i--) {
            System.out.println(undoIndex);
            System.out.println(i);
            //Removes history object from both the layer tree and History Array
            imageCraft.layerTree.removeHistory((History) historyArray.get(i), this);
        }
        //Create new History Object for the filter
        History history = new History(imageCraft, this, historyType, this.historyArray);
        
        //Update the undoIndex
        undoIndex++;
        
        //Add the History Object to the LayerTree
        imageCraft.layerTree.addHistory(history, this);
        
        //Add the History Object to this layer's historyArray
        historyArray.add(history);
        
        //Clear the Drawing Area and draw the current snapshot to the current drawing
        BufferedImage currentDrawing = imageCraft.drawingArea.getCurrentDrawing();
        Graphics currentGraphics = currentDrawing.getGraphics();
        currentGraphics.clearRect(0, 0, currentDrawing.getWidth(), currentDrawing.getHeight());
        currentGraphics.drawImage(
                history.getFinalImage(), 0, 0, null);
        currentGraphics.dispose();

        imageCraft.drawingArea.repaint();

    }
    
    public void removeHistory(int i) {
        historyArray.remove(i);
        undoIndex = (short) (historyArray.size() - 1);
    }
    
    protected void editHistorySnapshot(History historyObj) {
        System.out.println("GOT HERE");
        for (int i = historyArray.indexOf(historyObj); i < (int) historyArray.size(); i++) {
            System.out.println("I " + i);
            historyArray.get(i).updateFinalImage();
        }
    }    

    protected void undo() {
        if (undoIndex > -1) {
            undoIndex--;
            imageCraft.layerTree.repaint();
            imageCraft.drawingArea.repaint();
            System.out.println(undoIndex);
        }
    }

    protected void redo() {
        if (undoIndex < historyArray.size() - 1) {
            undoIndex++;
            imageCraft.layerTree.repaint();
            imageCraft.drawingArea.repaint();
            System.out.println(undoIndex);
        }
    }

    protected short getUndoIndex() {
        return this.undoIndex;
    }
    
    protected void setUndoIndex(short num) {
        this.undoIndex = num;
    }

    protected String getLayerName() {
        return this.layerName;
    }
    
    protected void setLayerName(String name) {
        this.layerName = name;
    }

    public ArrayList<History> getHistoryArray() {
        return this.historyArray;
    }

    protected short getDrawNum() {
        return this.drawNum;
    }
    
    protected void setDrawNum(short num) {
        this.drawNum = num;
    }

    protected short getRectangleNum() {
        return this.rectangleNum;
    }
    
    protected void setRectangleNum(short num) {
        this.rectangleNum = num;
    }    
    
    protected short getOvalNum(){
        return this.ovalNum;
    }
    
    protected void setOvalNum(short num){
        this.ovalNum = num;
    }

    protected short getFillNum() {
        return this.fillNum;
    }
    
    protected void setFillNum(short num) {
        this.fillNum = num;
    }    

    protected short getImageNum() {
        return this.imageNum;
    }
    
    protected void setImageNum(short num) {
        this.imageNum = num;
    }    

    protected short getFilterNum() {
        return this.filterNum;
    }
    
    protected void setFilterNum(short num) {
        this.filterNum = num;
    }
    public static void main(String[] args) {
    }

    //Declare Variables
    private short undoIndex;

    private String layerName;
    private final ArrayList<History> historyArray;
    private final ImageCraft imageCraft;

    private short ovalNum = 0;
    private short drawNum = 0;
    private short rectangleNum = 0;
    private short fillNum = 0;
    private short imageNum = 0;
    private short filterNum = 0;
    //End of Variable Declaration
}
