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
package tzk.image.tool;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import javax.swing.JToggleButton;
import tzk.image.ui.ImageCraft;

/**
 * Basic tool class. Extend this class with your tool.
 *
 * Contributers:    Thomas James Barry/ thomasbarry92@gmail.com   /5076942
 *                  Zachary Gateley/    zach.cykic@gmail.com      /5415772
 *                  K Drew Gonzales/    drewgonzales360@gmail.com /5470602
 */
public class SimpleTool {

    public SimpleTool(ImageCraft iC) {
        imageCraft = iC;
    }

    /**
     * What happens when the mouse is pressed. When this tool is selected, this
     * event fires when the mouse is pressed in the DrawingArea
     *
     * @param evt
     */
    public void mousePressed(MouseEvent evt) {
    }

    /**
     * What happens when the mouse is dragged. When this tool is selected, this
     * method is called when the mouse has been pressed within the DrawingArea
     * and the mouse is dragged in the drawing area.
     *
     * @param evt
     */
    public void mouseDragged(MouseEvent evt) {
    }

    /**
     * What happens when the mouse is released. When this tool is selected, this
     * method is called when the mouse has been pressed within the DrawingArea
     * and the mouse is released
     *
     * @param evt
     */
    public void mouseReleased(MouseEvent evt) {
    }

    /**
     * Deselects any current tool and selects the new tool.
     *
     *
     */
    public void select() {
        if (imageCraft.currentTool != null) {
            deselect();
        }

        // Select currentTool
        // Set the toggle button of the current tool to selected
        imageCraft.currentTool = this;
        if (imageCraft.currentTool != null && imageCraft.currentTool.toolButton != null) {
            imageCraft.currentTool.toolButton.setSelected(true);
        }
        //By default you cannot select a size for a tool (this is overrided if
        //this is a draw or shape tool.
        imageCraft.jSize.setEnabled(false);
    }

    /**
     * Deselects this SimpleTool
     *
     *
     */
    public void deselect() {
        // In case this was called when there is no current tool, end execution
        if (imageCraft.currentTool == null) {
            return;
        }

        // Toggle its button
        if (imageCraft.currentTool.toolButton != null) {
            imageCraft.currentTool.toolButton.setSelected(false);
        }
    }

    
    


    public void setButton(JToggleButton button) {
        toolButton = button;
    }

    public void setPenStroke(String filePath) {
    }

    public void setPenWidth(int width) {
    }

    // Variables declaration
    private final ImageCraft imageCraft;

    // What button the tool is using
    private JToggleButton toolButton = null;

    // End of variables declaration
}
