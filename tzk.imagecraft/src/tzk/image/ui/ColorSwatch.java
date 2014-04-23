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

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

/**
 *
 * @author Zach
 */
public class ColorSwatch extends javax.swing.JPanel {

    /**
     * Creates new form ColorSwatch
     */
    public ColorSwatch() {
        initComponents();
    }
    
    public ColorSwatch(ImageCraft iC)
    {
        imageCraft = iC;        
        initComponents();
//        colorPicker = new ColorPicker(iC);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // No events if object not instantiated correctly
        // Use constructor with ImageCraft parameter
        if (imageCraft == null) 
        {
            return;
        }
        
        int x = evt.getX();
        int y = evt.getY();
        boolean doPicker = false;
        boolean primary = true;
        
        // Primary color
        if (x > -1 && x < getWidth()-16 && y > -1 && y < getHeight()-16)
        {
            doPicker = true;
        }
        // Secondary color
        else if (x > 14 && x < getWidth() && y > 14 && y < getHeight())
        {
            primary = false;
            doPicker = true;
        }
        // Click on switch icon
        else if (x > 14)
        {
            Color c = imageCraft.primaryColor;
            imageCraft.primaryColor = imageCraft.secondaryColor;
            imageCraft.secondaryColor = c;
            repaint();
        }
        
        if (doPicker) 
        {
            ImageCraft.colorPicker.imageCraft = imageCraft;            
            ImageCraft.colorPicker.jColorChooser.setColor((primary ? imageCraft.primaryColor : imageCraft.secondaryColor));
            ImageCraft.colorPicker.primary = primary;
            ImageCraft.colorPicker.setVisible(true);
        }
    }//GEN-LAST:event_formMouseClicked

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);        
        
        Color secondaryColor, primaryColor;
        if (imageCraft == null)
        {
            // Dummy display. ColorSwatch not properly instantiated.
            // Use constructor with ImageCraft parameter
            secondaryColor = Color.black;
            primaryColor = Color.white;
        }
        else 
        {
            secondaryColor = imageCraft.secondaryColor;
            primaryColor = imageCraft.primaryColor;
        }
        
        // Swatch rectangles are always squares
        // Auto size them  to fit well in ColorSwatch
        // Set to 16px less than width of ColorSwatch
        int side = getWidth() - 16;
        
        // Secondary first, goes underneath primary
        // Draw the color box
        g.setColor(secondaryColor);
        g.fillRect(15, 15, side, side);
        // Then put a black border on it
        g.setColor(Color.black);
        g.drawRect(15, 15, side, side);
        
        // Primary color, goes on top of secondary color
        // Draw the color box
        g.setColor(primaryColor);
        g.fillRect(0, 0, side, side);
        // Then put a black border on it
        g.setColor(Color.black);
        g.drawRect(0, 0, side, side);

        
        // Add switcher icon
        try
        {
            BufferedImage switcher = ImageIO.read(
                    new File("src/tzk/image/img/colorSwitch.png"));
            g.drawImage(switcher, side + 2, 1, null);
        }
        catch (IOException err)
        {
            System.out.println("Not a real image..."); // You shmuck
        }
    }
    
    // Variables declaration
    private ImageCraft imageCraft;
//    private ColorPicker colorPicker;
    // End of variables declaration

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
