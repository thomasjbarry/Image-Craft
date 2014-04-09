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
import java.awt.image.BufferedImage;

public class SimpleHistory {
    /**
     * Create new SimpleHistory image object.
     * 
     * @param layer the layer to which this SimpleHistory object belongs
     * @param image a BufferedImage object with ONLY the latest action's changes
     */
    public SimpleHistory(Layer layer, BufferedImage image)
    {
        // Save parameters to object
        layerObject = layer;
        actionImage = image;
        actionFilter = null;
        
        createFinalImage();
    }
    
    /**
     * Create a new SimpleHistory filter object.
     * 
     * @param layer the layer to which this SimpleHistory object belongs
     * @param image a filter object that has been executed
     */
    /*public SimpleHistory(Layer layer, Filter filter)
    {
        // Save parameters to object
        layerObject = layer;
        actionImage = null;
        actionFilter = filter;
        
        createFinalImage();
    }*/
    
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
    
    
    
    //draws the SimpleHistory object to a BufferedImage object passed as a param
    protected void draw(BufferedImage image)
    {
        Graphics drawGraphics = image.getGraphics();
        drawGraphics.drawImage(actionImage, 0, 0, null);
        drawGraphics.dispose();
    }
    
    
    public static void main(String[] args)
    {
        // SimpleHistory is never executed, so this may remain empty
    }
    
    // Variable Declaration
    // Snapshot of the action
    protected BufferedImage actionImage;
    // This is the exact filter command
    // This type will change, assuming we create a Filter type
    protected final Layer actionFilter;
    // Snapshot of the layer up to this point, including this action
    protected BufferedImage finalImage;
    protected final Layer layerObject;
    //End of variable declaration
}
