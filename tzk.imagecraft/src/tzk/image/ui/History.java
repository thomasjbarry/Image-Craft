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
 * This class holds information about all actions done to the project. It also
 * holds the filters
 * 
 * Contributers:    Thomas James Barry/ thomasbarry92@gmail.com   /5076942
 *                  Zachary Gateley/    zach.cykic@gmail.com      /5415772
 *                  K Drew Gonzales/    drewgonzales360@gmail.com /5470602
 */
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
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
            case "Oval":
                layerObject.setOvalNum( (short) (layerObject.getOvalNum() + 1) );
                historyNum = layerObject.getOvalNum();
                break;
            default:
                historyNum = 0;
                break;
        }
        historyName = historyType + " Object #" + historyNum;
        System.out.println("New History in " + this.layerObject.getLayerName() + ": " + historyName);
        createFinalImage(layerObject.getHistoryArray().size());
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

        chooseFilter(selected);
    }

    /**
     * Create snapshot of finalImage.
     *
     * @param index index of the history object to create a final image for
     */
    private void createFinalImage(int index) {
        // Get the most recent snapshot
        finalImage = layerObject.getSnapshot(index - 1);

        //If this History object is not a filter it has an actionImage;
        //Draw this actionImage to the finalImage
        //Else apply the filter to the finalImage
        if (actionImage != null) {
            Graphics finalGraphics = finalImage.getGraphics();
            finalGraphics.drawImage(actionImage, 0, 0, null);
            finalGraphics.dispose();
        } else {
            ArrayList<History> selected = new ArrayList<>();
            for (int i = 0; i < index +1; i++) {
                selected.add(layerObject.getHistoryArray().get(i));
            }
            chooseFilter(selected);
        }
    }

    /**
     * Updates the final image for this history object.
     */
    protected void updateFinalImage() {
        this.createFinalImage(layerObject.getHistoryArray().indexOf(this));
    }

    /**
     * This method redraws the historyobject. If you press ctrl z this method is
     * called to redraw the history object.
     *
     * @param image pass in the drawing area so that the undo history draws
     * draws to the current drawing.
     */
    protected void draw(BufferedImage image) {
        Graphics drawGraphics = image.getGraphics();
        if (actionImage != null) {
            drawGraphics.drawImage(actionImage, 0, 0, null);
        } else {
            drawGraphics.drawImage(finalImage, 0, 0, null);
        }
        drawGraphics.dispose();
    }

    private void chooseFilter(ArrayList<History> selected) {
                //Filter the selected History objects and create the finalImage
        System.out.println(actionFilter);
        switch (actionFilter) {
            case "Grayscale":
                grayScale(selected);
                break;
            case "Negative":
                negative(selected);
                break;
            case "Sharpen":
                sharpen(selected);
                break;
            case "Blur":
                blur(selected);
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
    private void grayScale(ArrayList<History> selected) {
        //new BufferedImageOp (filter) to change the color of a BufferedImage to
        //a grayscale image
        BufferedImageOp grayScaleOp = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

        applyFilter(grayScaleOp, selected);
    }

    /**
     * Applies negative filter to layer.
     *
     * @param selected array of history objects that will be negative'd
     */
    private void negative(ArrayList<History> selected) {

        short[] normal = new short[256];
        short[] reverse = new short[256];

        for (int i = 0; i < 256; i++) {
            normal[i] = (short) i;
            reverse[i] = (short) (255 - i);
        }

        short[][] lookup = {reverse, reverse, reverse, normal};

        BufferedImageOp negativeOp = new LookupOp(new ShortLookupTable(0, lookup), null);

        applyFilter(negativeOp, selected);
    }

    private void sharpen(ArrayList<History> selected) {
        float[] sharpKernel = {
            0.0f, -1.0f, 0.0f,
            -1.0f, 5.0f, -1.0f,
            0.0f, -1.0f, 0.0f
        };
        BufferedImageOp sharpenOp = new ConvolveOp(new Kernel(3, 3, sharpKernel),
                ConvolveOp.EDGE_NO_OP, null);

        applyFilter(sharpenOp, selected);
    }

    /**
     * This makes the edges in a picture more clear. I overrode this so that we
     * could use the sharpen method for other things. Not just filters. I used
     * it when I wrote resize.
     *
     * @param image
     * @return
     */
    private BufferedImage sharpen(BufferedImage image) {
        float[] sharpKernel = {
            0.0f, -1.0f, 0.0f,
            -1.0f, 5.0f, -1.0f,
            0.0f, -1.0f, 0.0f
        };
        BufferedImageOp sharpenOp = new ConvolveOp(new Kernel(3, 3, sharpKernel),
                ConvolveOp.EDGE_NO_OP, null);
        Graphics imageGraphics = image.getGraphics();
        imageGraphics.drawImage(sharpenOp.filter(image, null), 0, 0, null);
        imageGraphics.dispose();
        return image;
    }

    private void blur(ArrayList<History> selected) {
        float ninth = 1.0f / 9.0f;
        float[] blurKernel = {
            ninth, ninth, ninth,
            ninth, ninth, ninth,
            ninth, ninth, ninth
        };
        BufferedImageOp blurOp = new ConvolveOp(new Kernel(3, 3, blurKernel));

        applyFilter(blurOp, selected);
    }

    private void applyFilter(BufferedImageOp op, ArrayList<History> selected) {
        //Make a blank BufferedImage to hold the new filtered image
        BufferedImage copy = imageCraft.newBlankImage();
        Graphics copyGraphics = copy.getGraphics();

        //For each History object in this layers HistoryArray, if its in the
        //selection to be filtered and it isn't a filter itself, draw the 
        //filtered version of that History object to the copy. Else draw the
        //unfiltered version of that History object to the copy.
//        for (History historyObj : layerObject.getHistoryArray()) {
//            if (historyObj.actionImage == null) {
//            } else {
//                if (selected.contains(historyObj)) {
//                    copyGraphics.drawImage(
//                            op.filter(historyObj.actionImage, null),
//                            0, 0, null);
//                } else {
//                    copyGraphics.drawImage(historyObj.actionImage, 0, 0, null);
//                }
//            }
//        }
//        copyGraphics.drawImage(op.filter(layerObject.getSnapshot(layerObject.getHistoryArray().indexOf(this)),null), 0, 0, null);
        //copyGraphics.drawImage(op.filter(layerObject.getSnapshot(), null), 0, 0, null);
        
        // Compile correct snapshot up to this history item
        for (History historyObj : selected) {
            historyObj.draw(copy);
        }
        copyGraphics.drawImage(op.filter(copy, null), 0, 0, null);

        //Clean up resources and set this History object's finalImage to the
        //now filtered image
        copyGraphics.dispose();
        this.finalImage = copy;
    }

    public BufferedImage getActionImage() {
        return this.actionImage;
    }

    public void setActionImage(BufferedImage image) {
        this.actionImage = image;
    }

    protected String getActionFilter() {
        return this.actionFilter;
    }

    public BufferedImage getFinalImage() {
        return this.finalImage;
    }

    public void setFinalImage(BufferedImage image) {
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
