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
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * Color Swatch is a custom jPanel in the top right of ImageCraft and tells the
 * user what color is currently selected.
 *
 * @author Zach
 */
public class ColorSwatch extends JPanel {

    /**
     * Empty Color swatch constructor made by form.
     *
     * @deprecated
     */
    public ColorSwatch() {
        initComponents();
    }

    /**
     * Create color swatch.
     * Proper constructor.
     * 
     * @param iC the ImageCraft object this belongs to
     */
    public ColorSwatch(ImageCraft iC) {
        imageCraft = iC;
        initComponents();
    }

    private void initComponents() {
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                swatchClicked(evt);
            }
        });

        // Must be at least 56 by 56 but can be stretched larger
        this.setMinimumSize(new Dimension(56, 56));
    }

    private void swatchClicked(MouseEvent evt) {
        // No events if object not instantiated correctly
        // Use constructor with ImageCraft parameter
        if (imageCraft == null) {
            return;
        }

        int x = evt.getX();
        int y = evt.getY();
        boolean doPicker = false;
        boolean primary = true;

        if (x > -1 && x < getWidth() - 16 && y > -1 && y < getHeight() - 16) {
            // Clicked on primary color
            doPicker = true;
        } else if (x > 14 && x < getWidth() && y > 14 && y < getHeight()) {
            // Clicked on secondary coor
            primary = false;
            doPicker = true;
        }
        else if (x > 14) {
            // Clicked on switch icon, swap primary and secondary
            Color c = imageCraft.getPaintColor(true);
            imageCraft.setPaintColor(true, imageCraft.getPaintColor(false));
            imageCraft.setPaintColor(false, c);
            repaint();
        }

        // Either primary or secondary was clicked
        // Open the color picker
        if (doPicker) {
            ImageCraft.colorPicker.setImageCraft(imageCraft);
            ImageCraft.colorPicker.setColor(imageCraft.getPaintColor(primary));
            ImageCraft.colorPicker.setPrimaryFlag(primary);
            ImageCraft.colorPicker.setVisible(true);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color secondaryColor, primaryColor;
        if (imageCraft == null) {
            // Dummy display. ColorSwatch not properly instantiated.
            // Use constructor with ImageCraft parameter
            secondaryColor = Color.black;
            primaryColor = Color.white;
        } else {
            secondaryColor = imageCraft.getPaintColor(false);
            primaryColor = imageCraft.getPaintColor(true);
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
        try {
            BufferedImage switcher = ImageIO.read(
                    new File("src/tzk/image/img/colorSwitch.png"));
            g.drawImage(switcher, side + 2, 1, null);
        } catch (IOException err) {
            System.out.println("Not a real image..."); 
        }
    }

    // Variables declaration
    private ImageCraft imageCraft;
    // End variables declaration
}
