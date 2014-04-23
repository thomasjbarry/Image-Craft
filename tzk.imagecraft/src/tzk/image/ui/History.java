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
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.util.ArrayList;

public class History {

    /**
     * Create new History image object. The History object stores the color of
     * whatever change was made to the drawing area and any other information 
     * needed to redraw for the undo history. 
     *
     * @param iC the ImageCraft to which the Layer object belongs
     * @param layer the Layer to which this History object belongs
     * @param image a BufferedImage object with ONLY the latest action's changes
     * @param historyType
     */
    public History(ImageCraft iC, Layer layer, BufferedImage image, String historyType) {
        imageCraft = iC;
        layerObject = layer;
        actionImage = image;
        actionFilter = null;
        short historyNum;
        switch (historyType) {
            case "Draw":
                layerObject.setDrawNum((short) (layerObject.getDrawNum() + 1));
                historyNum = layerObject.getDrawNum();
                break;
            case "Rectangle":
                layerObject.setRectangleNum((short) (layerObject.getRectangleNum() + 1));
                historyNum = layerObject.getRectangleNum();
                break;
            case "Fill":
                layerObject.setFillNum((short) (layerObject.getFillNum() + 1));
                historyNum = layerObject.getFillNum();
                break;
            case "Imported Image":
                layerObject.setImageNum((short) (layerObject.getImageNum() + 1));
                historyNum = layerObject.getImageNum();
                break;
            default:
                historyNum = 0;
                break;
        }
        historyName = historyType + " Object #" + historyNum;
        System.out.println("New History in " + this.layerObject.getLayerName() + ": " + historyName);
        createFinalImage();
    }

    /**
     * Create a new History filter object.
     *
     * @param iC the ImageCraft object this History object belongs to
     * @param layer the layer to which this History object belongs
     * @param filterType the type of filter to be applied
     * @param selected
     */
    public History(ImageCraft iC, Layer layer, String filterType, ArrayList<History> selected) {
        // Save parameters to object
        imageCraft = iC;
        layerObject = layer;
        actionImage = null;
        actionFilter = filterType;
        
        //Increment the filterNum for this History's layerObject and set this
        //History object's name
        layerObject.setFilterNum((short) (layerObject.getFilterNum() + 1));
        historyName = filterType + " Object #" + layerObject.getFilterNum();

        //Filter the selected History objects and create the finalImage
        filter(selected);        
    }

    /**
     * Create snapshot of finalImage.
     */
    private void createFinalImage() {
        // Get the most recent snapshot
        BufferedImage lastImage = layerObject.getLastSnapshot();
        
        //If this History object is not a filter it has an actionImage;
        //Draw this actionImage to the lastImage and set it as this finalImage
        if (actionImage != null) {
            finalImage = lastImage;
            finalImage.getGraphics().drawImage(actionImage, 0, 0, null);
        }
    }

    /**
     * This method redraws the historyobject. If you press ctrl z this method is 
     * called to redraw the history object.
     * 
     * @param image pass in the drawing area so that the undo history draws
     *              draws to the current drawing. 
     */
    protected void draw(BufferedImage image) {
        Graphics drawGraphics = image.getGraphics();
        drawGraphics.drawImage(actionImage, 0, 0, null);
        drawGraphics.dispose();
    }

    
    /**
     * The method adds filters to all the other drawn actions in the drawing area.
     * @param selected 
     */
    private void filter(ArrayList<History> selected) {
        switch (actionFilter) {
            case "Grayscale":
                grayScale(selected);
                break;
            default:
                break;
        }
    }

    /**
     * Grayscale filter applied to all History objects before this object in
     * layerObject's HistoryArray.
     *
     * @param selected
     */
    protected void grayScale(ArrayList<History> selected) {
        //new BufferedImageOp (filter) to change the color of a BufferedImage to
        //a grayscale image
        BufferedImageOp grayScaleOp = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        
        //Make a new blank image to copy the History objects to, in order to make
        //a finalImage for this History object.
        BufferedImage copy = imageCraft.newBlankImage();
        Graphics copyGraphics = copy.getGraphics();
        
        //For each historyObj in the selected ArrayList apply the grayScaleOp to
        //its actionImage, then draw it (whether it was filtered or not) to copy
        for (History historyObj : layerObject.getHistoryArray()) {
            if (selected.contains(historyObj)) {
                copyGraphics.drawImage(
                        grayScaleOp.filter(historyObj.actionImage, historyObj.actionImage),
                        0, 0, null);
            } else {
                copyGraphics.drawImage(historyObj.actionImage, 0, 0, null);
            }
        }
        //Set this history's finalImage to copy
        this.finalImage = copy;
    }

    protected BufferedImage getActionImage() {
        return this.actionImage;
    }

    protected void setActionImage(BufferedImage image) {
        this.actionImage = image;
    }

    protected String getActionFilter() {
        return this.actionFilter;
    }

    protected BufferedImage getFinalImage() {
        return this.finalImage;
    }

    protected void setFinalImage(BufferedImage image) {
        this.finalImage = image;
    }

    protected Layer getLayer() {
        return this.layerObject;
    }

    protected String getHistoryName() {
        return this.historyName;
    }

    protected void setHistoryName(String name) {
        this.historyName = name;
    }

    public static void main(String[] args) {
        // History is never executed, so this may remain empty
    }

    // Variable Declaration
    private ImageCraft imageCraft;
    private BufferedImage actionImage;
    private String actionFilter;
    private BufferedImage finalImage;
    private final Layer layerObject;
    private String historyName;
    //End of variable declaration
}
