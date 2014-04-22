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
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.util.ArrayList;

public class History {

    /**
     * Create new History image object.
     *
     * @param iC the ImageCraft to which the Layer object belongs
     * @param layer the Layer to which this History object belongs
     * @param image a BufferedImage object with ONLY the latest action's changes
     * @param historyType
     */
    public History(ImageCraft iC, Layer layer, BufferedImage image, String historyType) {
        imageCraft = iC;
        // Save parameters to object
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

        filter(selected);
        createFinalImage();
    }

    /**
     * Create snapshot of finalImage.
     */
    private void createFinalImage() {
        // TODO: Create final image
        BufferedImage lastImage = layerObject.getLastSnapshot();
        if (actionImage != null) {
            finalImage = lastImage;
            finalImage.getGraphics().drawImage(actionImage, 0, 0, null);
        }
    }

    //draws the History object to a BufferedImage object passed as a param
    protected void draw(BufferedImage image) {
        Graphics drawGraphics = image.getGraphics();
        drawGraphics.drawImage(actionImage, 0, 0, null);
        drawGraphics.dispose();
    }

    /**
     * Calls the proper filter method with the index of this History object in
     * its layer's HistoryArray
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
        for (History historyObj : selected) {
            //new BufferedImageOp (filter) to change the color of a BufferedImage to
            //a grayscale image
            BufferedImageOp grayScaleOp = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
            
            //Applies the grayScaleOp to the finalImage of the historyObj
            historyObj.finalImage = grayScaleOp.filter(historyObj.finalImage, historyObj.finalImage);
        }
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
    // Snapshot of the action
    private BufferedImage actionImage;
    // This is the exact filter command
    // This type will change, assuming we create a Filter type
    private String actionFilter;
    // Snapshot of the layer up to this point, including this action
    private BufferedImage finalImage;
    private final Layer layerObject;
    private String historyName;
    //End of variable declaration
}
