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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.*;
import javax.swing.*;

/**
 * This class allows the user to pick color to paint with,
 * draw shapes with, and draw with. 
 * 
 * Contributers:    Thomas James Barry/ thomasbarry92@gmail.com   /5076942
 *                  Zachary Gateley/    zach.cykic@gmail.com      /5415772
 *                  K Drew Gonzales/    drewgonzales360@gmail.com /5470602
 */
public class ColorPicker extends JFrame {


    /**
     * Used to make a new ColorPicker. 
     * Allows user to pick a color to fill, draw, and shape with.
     * Use this when a ColorPicker belongs to the ImageCraft class.
     */
    public ColorPicker() {
        imageCraft = null;        
        initComponents();
    }
    
    /**
     * Creates new form ColorPicker.
     * Use this when a ColorPicker belongs to an ImageCraft object.
     * 
     * @param opener This is the ImageCraft object that opens this ColorPicker
     */
    public ColorPicker(final ImageCraft opener)
    {
        //Initialize variables
        imageCraft = opener;
        
        //Create the GUI components
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        // Content pane uses BorderLayout by default
        Container pane = getContentPane();

        // Interactive components
        jColorChooser = new JColorChooser();
        // Apply but do not close
        jApplyButton = new JButton();
        // Apply and close
        jSelectButton = new JButton();
        // Do not apply, close
        jCancelButton = new JButton();
        
        // Set button titles
        jApplyButton.setText("Apply");
        jSelectButton.setText("OK");
        jCancelButton.setText("Cancel");
        
        // Add events
        jApplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setColor();
            }
        });
        jSelectButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent evt) {
               saveColor();
           }
        });
        jCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                cancelSelect();
            }
        });
        
        // Add ColorChooser to CENTER
        JPanel top = new JPanel();
        top.add(jColorChooser);
        pane.add(top, BorderLayout.CENTER);
        
        // Add action buttons to SOUTH
        JPanel bottom = new JPanel();
        bottom.add(jApplyButton);
        bottom.add(jSelectButton);
        bottom.add(jCancelButton);
        pane.add(bottom, BorderLayout.SOUTH);

        pack();
    }
    
    /**
     * Set the selected color and keep the ColorPicker open.
     */
    private void setColor() {
        if (imageCraft != null) 
        {
            // Update the ImageCraft object's paint color
            imageCraft.setPaintColor(primary, jColorChooser.getColor());
            
            //Paint jColorSwatch component to the newly selected color
            imageCraft.jColorSwatch.repaint();
        }
    }

    /**
     * Save the selected color and close the ColorPicker. 
     * Called by action event.
     */
    private void saveColor() {
        // Update the ImageCraft's color
        setColor();
        
        //Close the ColorPicker
        this.dispatchEvent(windowClosing);
    }

    /**
     * Do not select color. Hide ColorPicker.
     */
    private void cancelSelect() {
        //Decided not to change the color so close the ColorPicker
        this.dispatchEvent(windowClosing);
    }

    /**
     * Set the current color of the color picker.
     * Called on ColorPicker open.
     * 
     * @param color new color to set
     */
    protected void setColor(Color color) {
        jColorChooser.setColor(color);
    }
    
    /**
     * Set the ImageCraft object this ColorPicker belongs to.
     * 
     * @param iC the new ImageCraft object
     */
    protected void setImageCraft(ImageCraft iC) {
        imageCraft = iC;
    }
    
    /**
     * Set the flag whether we're working on the primary or the secondary color.
     * 
     * @param 
     */
    protected void setPrimaryFlag(boolean primary) {
        this.primary = primary;
    }
    
    // Variables declaration
    private ImageCraft imageCraft;
    private final WindowEvent windowClosing
            = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
    private boolean primary;
    private JButton jCancelButton;
    private JColorChooser jColorChooser;
    private JButton jSelectButton;
    private JButton jApplyButton;
    // End of variables declaration
}
