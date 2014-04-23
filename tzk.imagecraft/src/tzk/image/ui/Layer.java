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
 *
 * @author Thomas
 */
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

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
            imageCraft.drawingArea.getCurrentDrawing().getGraphics().drawImage(
                    snapshot, 0, 0, null);
            imageCraft.drawingArea.getGraphics().drawImage(
                    imageCraft.drawingArea.getCurrentDrawing(), 0, 0, null);
        }
    }

    protected BufferedImage getLastSnapshot() {
        int size = historyArray.size();
        if (size > 0) {
            // Return a copy of the last snapshot
            BufferedImage image = (historyArray.get(size - 1)).getFinalImage();
            ColorModel model = image.getColorModel();
            WritableRaster raster = image.copyData(null);
            return new BufferedImage(model, raster, model.isAlphaPremultiplied(), null);
        } else {
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
        for (int i = historyArray.size() - 1; i > undoIndex; i--) {
            imageCraft.layerTree.removeHistory(historyArray.get(historyArray.size() - i - 1), this);
            historyArray.remove(i);
        }
        History history = new History(imageCraft, this, image, historyType);
        undoIndex++;
        imageCraft.layerTree.addHistory(history, this);
        historyArray.add(history);
        imageCraft.drawingArea.getCurrentDrawing().getGraphics().drawImage(
                image, 0, 0, null);

        imageCraft.drawingArea.repaint();

    }

    /**
     * Create new History object and add to historyArray.
     *
     * @param historyType
     */
    public void addHistory(String historyType) {
        //For all indices after the undoIndex delete the History object
        for (int i = historyArray.size() - 1; i > undoIndex; i--) {
            imageCraft.layerTree.removeHistory((History) historyArray.get(historyArray.size() - i - 1), this);
            historyArray.remove(i);
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
        imageCraft.drawingArea.getCurrentDrawing().getGraphics().clearRect(0, 0, imageCraft.drawingArea.getCurrentDrawing().getWidth(), imageCraft.drawingArea.getCurrentDrawing().getHeight());
        imageCraft.drawingArea.getCurrentDrawing().getGraphics().drawImage(
                history.getFinalImage(), 0, 0, null);

        imageCraft.drawingArea.repaint();

    }
    
    public void removeHistory(int i) {
        historyArray.remove(i);
        undoIndex = (short) (historyArray.size() - 1);
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

    private short drawNum = 0;
    private short rectangleNum = 0;
    private short fillNum = 0;
    private short imageNum = 0;
    private short filterNum = 0;
    //End of Variable Declaration
}
