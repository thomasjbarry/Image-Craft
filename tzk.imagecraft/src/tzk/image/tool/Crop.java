/*
 * The MIT License
 *
 * Copyright 2014 thomas.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tzk.image.tool;

import javax.swing.JButton;
import tzk.image.ui.History;
import tzk.image.ui.ImageCraft;
import tzk.image.ui.Layer;

/**
 *
 * @author thomas
 */
public class Crop extends SimpleTool{
    public Crop(ImageCraft iC) {
        super(iC);
        imageCraft = iC;
        
//        super.setButton(imageCraft.jCrop);
        this.button = imageCraft.jCrop;
    }
    
    @Override
    public void select() {
        if (false) {
            //If there is a selection then apply crop to the selection
            //imageCraft.getSelection() will return an int[] of size 4,
            //containing the topleft corner's x,y and the width and height of
            //the rectangle
            
//            applyCrop(imageCraft.getSelection()[0],
//                    imageCraft.getSelection()[1],
//                    imageCraft.getSelection()[2],
//                    imageCraft.getSelection()[3]);
        } else {
            //There is nothing selected, apply Crop to the current layer with
            //the DrawingArea's current size
            applyCrop(0, 0, (int) imageCraft.drawingArea.getPreferredSize().getWidth(),
                    (int) imageCraft.drawingArea.getPreferredSize().getHeight());
        }
    }
    
    private void applyCrop(int x, int y, int width, int height) {
        //Set the final&&action images for
        for (int index : imageCraft.layerTree.getSelected().get(0)) {
            Layer layerObj = imageCraft.layerList.get(index);
            for (History historyObj : layerObj.getHistoryArray()) {
                historyObj.setFinalImage(historyObj.getFinalImage().getSubimage(
                        x, y, width, height));

                historyObj.setActionImage(historyObj.getActionImage().getSubimage(
                        x, y, width, height));
            }
        }
    }
    
    //Declare Variables
    private final ImageCraft imageCraft;
    private final JButton button;
}
