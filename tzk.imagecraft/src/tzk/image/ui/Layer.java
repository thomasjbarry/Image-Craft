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
            BufferedImage snapshot = historyArray.get(undoIndex).finalImage;
            imageCraft.drawingArea1.currentDrawing.getGraphics().drawImage(
                    snapshot, 0, 0, null);
            imageCraft.drawingArea1.getGraphics().drawImage(
                    imageCraft.drawingArea1.currentDrawing, 0, 0, null);
        }
    }

    protected BufferedImage getLastSnapshot() {
        int size = historyArray.size();
        if (size > 0) {
            // Return a copy of the last snapshot
            BufferedImage image = historyArray.get(size - 1).finalImage;
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
        imageCraft.layerTree1.addLayer(this);
        imageCraft.layerTree1.setSelected(this);

        // Make this new layer the current layer
        imageCraft.currentLayer = this;
    }

    /**
     * Create new SimpleHistory object and add to historyArray.
     *
     * @param image
     * @param historyType
     */
    public void addHistory(BufferedImage image, String historyType) {
        //For all indices after the undoIndex delete the SimpleHistory object
        for (int i = historyArray.size() - 1; i > undoIndex; i--) {
            imageCraft.layerTree1.removeHistory(historyArray.get(historyArray.size() - i - 1), this);
            historyArray.remove(i);
        }
        SimpleHistory history = new SimpleHistory(imageCraft, this, image, historyType);
        undoIndex++;
        imageCraft.layerTree1.addHistory(history, this);
        historyArray.add(history);
        imageCraft.drawingArea1.currentDrawing.getGraphics().drawImage(
                image, 0, 0, null);

        imageCraft.drawingArea1.paintComponent(imageCraft.drawingArea1.getGraphics());

    }

    public void removeHistory(int i) {
        historyArray.remove(i);
        undoIndex = (short) (historyArray.size() - 1);
    }

    protected void undo() {
        if (undoIndex > -1) {
            undoIndex--;
            imageCraft.layerTree1.repaint();
            imageCraft.drawingArea1.paintComponent(imageCraft.drawingArea1.getGraphics());
            System.out.println(undoIndex);
        }
    }

    protected void redo() {
        if (undoIndex < historyArray.size() - 1) {
            undoIndex++;
            imageCraft.layerTree1.repaint();
            imageCraft.drawingArea1.paintComponent(imageCraft.drawingArea1.getGraphics());
            System.out.println(undoIndex);
        }
    }

    public static void main(String[] args) {
    }

    //Declare Variables
    protected short undoIndex;

    protected String layerName;
    public final ArrayList<SimpleHistory> historyArray;
    private final ImageCraft imageCraft;

    protected short drawNum = 0;
    protected short rectangleNum = 0;
    protected short fillNum = 0;
    protected short imageNum = 0;
    //End of Variable Declaration
}
