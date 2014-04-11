
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tzk.image.tool;

import java.awt.event.*;
import javax.swing.JToggleButton;
import tzk.image.ui.ImageCraft;


/**
 * Basic tool class.
 * Extend this class with your tool.
 * 
 * @author zach
 */
public class SimpleTool {
    
    public SimpleTool(ImageCraft iC) {
        imageCraft = iC;
    }
    
    /**
     * What happens when the mouse is pressed.
     * When this tool is selected, this event fires when the mouse is
     * pressed in the DrawingArea
     * 
     * @param evt 
     */
    public void mousePressed(MouseEvent evt) {
    }
    
    /**
     * What happens when the mouse is dragged.
     * When this tool is selected, this method is called when the mouse
     * has been pressed within the DrawingArea and the mouse is dragged
     * in the drawing area.
     * 
     * @param evt 
     */
    public void mouseDragged(MouseEvent evt) {
    }
    
    /** 
     * What happens when the mouse is released.
     * When this tool is selected, this method is called when the mouse
     * has been pressed within the DrawingArea and the mouse is 
     * released
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
    public void select()
    {
        if (imageCraft.currentTool != null) {
            deselect();
        }
        
        // Select currentTool
        // Set the toggle button of the current tool to selected
        imageCraft.currentTool = this;
        if (imageCraft.currentTool != null && imageCraft.currentTool.toolButton != null) {
            imageCraft.currentTool.toolButton.setSelected(true);
        }
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
    
    
    // Variables declaration
    
    private final ImageCraft imageCraft;
    
    // What button the tool is using
    public JToggleButton toolButton = null;
    
    // End of variables declaration
}
