/*
 * The MIT License
 *
 * Copyright 2014 Zach.
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
package tzk.image.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Zach
 */
public class IO {

    /**
     * Create a new IO object. This will be used for all opening and saving of
     * images.
     *
     * @param iC the ImageCraft object this IO object belongs to
     */
    public IO(ImageCraft iC) {
        // What ImageCraft object this object belongs to
        imageCraft = iC;

        // Reuse the same fileChooser
        fileChooser = new JFileChooser();

        // Set up our file filters. when we open, save, or export, applying 
        // these filters shows what formats we can open, save, or export in.
    }

    /**
     * Open a file.
     */
    protected void open() {
        // Set available file formats, all
        fileChooser.resetChoosableFileFilters();
        fileChooser.addChoosableFileFilter(imageCraftFormat);
        fileChooser.addChoosableFileFilter(imageFormats);
        fileChooser.setFileFilter(imageCraftFormat);

        // Attempt open
        int openOption = fileChooser.showOpenDialog(imageCraft);
        // Returns JFileChooser.APPROVE_OPTION if the "Open" button was clicked
        if (openOption != JFileChooser.APPROVE_OPTION) {
            System.out.println("Open cancelled by user...");
            return;
        }

        // File chosen, open
        final File file = fileChooser.getSelectedFile();
        if (file.exists()) {
            //If we have never made a new Layer or made a new history element
            //in the background layer then open the file in this ImageCraft in
            //a new layer, otherwise open it in a new ImageCraft instance.
            if (imageCraft.numLayer == 1 && imageCraft.layerList.get(0).historyArray.isEmpty()) {
                try {
                    // Create a new layer in this ImageCraft
                    Layer openedLayer = new Layer(imageCraft);

                    // Buffer the image file
                    BufferedImage bufferedFile = ImageIO.read(file);

                    // If the bufferedFile is too big, resize it
                    if (bufferedFile.getWidth() > imageCraft.drawingArea1.getWidth()
                            || bufferedFile.getHeight() > imageCraft.drawingArea1.getHeight()) {
                        imageCraft.drawingArea1.increaseSize(
                                bufferedFile.getWidth() - imageCraft.drawingArea1.getWidth(),
                                bufferedFile.getHeight() - imageCraft.drawingArea1.getHeight());
                    }

                    // Add BufferedImage to layer history
                    openedLayer.addHistory(bufferedFile);
                } catch (IOException err) {
                    System.out.println("Not a real image..."); // You shmuck
                }
            } // Otherwise, open a new ImageCraft window
            else {
                java.awt.EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        // Create new ImageCraft object
                        ImageCraft program = new ImageCraft();

                        // Show ImageCraft object
                        program.setVisible(true);

                        // Open BufferedImage in new layer
                        try {
                            Layer openedLayer = new Layer(program);

                            BufferedImage bufferedFile = ImageIO.read(file);
                            System.out.println("File turned into buffered2");

                            //If the bufferedFile is too big resize it                            
                            if (bufferedFile.getWidth() > imageCraft.drawingArea1.getWidth()
                                    || bufferedFile.getHeight() > imageCraft.drawingArea1.getHeight()) {

                                imageCraft.drawingArea1.increaseSize(
                                        bufferedFile.getWidth() - imageCraft.drawingArea1.getWidth(),
                                        bufferedFile.getHeight() - imageCraft.drawingArea1.getHeight());
                                System.out.println(bufferedFile.getWidth() + " " + bufferedFile.getHeight());
                                System.out.println(imageCraft.drawingArea1.getWidth() + " " + imageCraft.drawingArea1.getHeight());

                            }
                            openedLayer.addHistory(bufferedFile);
                        } catch (IOException err) {
                            System.out.println("Not a real image..."); // You shmuck
                        }
                    }
                });
            }
        }
        System.out.println("Open " + file.toString());
    }

    /**
     * Save a file in ImageCraft format.
     *
     * @param unique false: Overwrite last save file true: Write to new save
     * file
     */
    protected void save(boolean unique) {// For new saves or saving to a current file, no fileChooser is necessary
        if (unique || latestSave == null) {
            // Set available file formats, ImageCraft only
            fileChooser.resetChoosableFileFilters();
            fileChooser.setFileFilter(imageCraftFormat);

            // Attempt JFileChooser save dialogue
            int saveOption = fileChooser.showSaveDialog(imageCraft);

            // Returns JFileChooser.APPROVE_OPTION if the user clicked "Save"
            if (saveOption != JFileChooser.APPROVE_OPTION) {
                System.out.println("Export cancelled by user...");
                return;
            }

            // New file chosen, save
            latestSave = fileChooser.getSelectedFile();

            // Update ImageCraft title
            imageCraft.setTitle(latestSave.toString());
        }

        System.out.println("Save " + (unique ? "new " : "") + "to " + latestSave.toString());

    }

    /**
     * Import a file to the current layer
     */
    protected void fileImport() {
        // Set available file formats, all
        fileChooser.resetChoosableFileFilters();
        fileChooser.addChoosableFileFilter(imageCraftFormat);
        fileChooser.addChoosableFileFilter(imageFormats);
        fileChooser.setFileFilter(imageCraftFormat);

        // Attempt open
        int importOption = fileChooser.showDialog(imageCraft, "Import");
        // Returns JFileChooser.APPROVE_OPTION if the "Import" button was clicked
        if (importOption != JFileChooser.APPROVE_OPTION) {
            System.out.println("Import cancelled by user...");
            return;
        }

        // File chosen, open
        final File file = fileChooser.getSelectedFile();
        if (file.exists()) {
            try {
                BufferedImage bufferedFile = ImageIO.read(file);

                //If the bufferedFile is too big resize it
//                    if (bufferedFile.getWidth() > imageCraft.drawingArea1.getWidth()
//                            || bufferedFile.getHeight() > imageCraft.drawingArea1.getHeight()) {
                imageCraft.drawingArea1.increaseSize(
                        bufferedFile.getWidth() - imageCraft.drawingArea1.getWidth(),
                        bufferedFile.getHeight() - imageCraft.drawingArea1.getHeight());
//                    }
                imageCraft.currentLayer.addHistory(bufferedFile);
                imageCraft.drawingArea1.paintComponent(imageCraft.drawingArea1.getGraphics());

            } catch (IOException err) {
                System.out.println("Not a real image..."); // You shmuck
            }
            System.out.println("Import " + file.toString());
        }
    }

    /**
     * Export a file as a JPG.
     *
     * @param unique false: Overwrite last export file true: Write to a new
     * export file
     */
    protected void export(boolean unique) {
        if (unique || latestExport == null) {
            // Set available image file formats
            fileChooser.resetChoosableFileFilters();
            fileChooser.setFileFilter(imageFormats);
            BufferedImage image = imageCraft.newBlankImage();
            Graphics g = image.getGraphics();
            for (Layer layer : imageCraft.layerList) {
                if (layer.undoIndex != -1) {
                    g.drawImage(layer.historyArray.get(layer.undoIndex).finalImage, 0, 0, null);
                }
            }
            BufferedImage filteredImage = new BufferedImage(
                    (int) imageCraft.drawingArea1.getPreferredSize().getWidth(),
                    (int) imageCraft.drawingArea1.getPreferredSize().getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            Graphics g2 = filteredImage.getGraphics();
            g2.setColor(Color.white);
            g2.fillRect(0, 0,
                    (int) imageCraft.drawingArea1.getPreferredSize().getWidth(),
                    (int) imageCraft.drawingArea1.getPreferredSize().getHeight());
            g2.drawImage(image, 0, 0, null);

            // Attempt JFileChooser save dialogue
            int exportOption = fileChooser.showDialog(imageCraft, "Export");
            // Returns JFileChooser.APPROVE_OPTION if the user clicked "Export"
            if (exportOption != JFileChooser.APPROVE_OPTION) {
                System.out.println("Export cancelled by user...");
                return;
            }

            // New file chosen, export
            latestExport = fileChooser.getSelectedFile();

            String[] fileNameParts = latestExport.toString().split("\\.");
            String format;
            if (fileNameParts.length > 1) {
                format = fileNameParts[fileNameParts.length - 1];
            } else {
                Path source = latestExport.toPath();
                String formattedName = latestExport.toString() + ".png";
                try {
                    Files.move(source, source.resolveSibling(formattedName));
                } catch (IOException err) {
                    System.out.println("err");
                }
                format = ".png";
            }

            try {
                ImageIO.write(filteredImage, format, latestExport);
            } catch (IOException err) {
            }
        }

        System.out.println("Export " + (unique ? "new " : "") + "to " + latestExport.toString());
    }

    // Variables declaration
    private final ImageCraft imageCraft;
    private final JFileChooser fileChooser;

    // List of filetype extension filters for open/save/export dialogues
    // These remain the same from object to object, so make them class fields
    // ImageCraft format: Extension *.icf
    private static final FileNameExtensionFilter imageCraftFormat = new FileNameExtensionFilter("ImageCraft File", "icf");
    // What images we are willing to import: *.gif, *.jpg, *.jpeg, *.png
    // We can knock this down depending on its level of difficulty
    private static final FileNameExtensionFilter imageFormats = new FileNameExtensionFilter("GIF, JPG, and PNG Images", "gif", "jpg", "jpeg", "png");

    // When something has been saved or exported, save to appropriate string.
    // When "Save" is clicked, lastSave is overwritten.
    // When "Save As" is clicked, new save is executed.
    // When "Export is clicked, lastExport is overwritten.
    // When "Export As" is clicked, new export is executed.
    private File latestSave;
    private File latestExport;
    // End of variables declaration
}
