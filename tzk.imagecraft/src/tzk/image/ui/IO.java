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

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
            if (imageCraft.numLayer == 1 && imageCraft.layerList.get(0).getHistoryArray().isEmpty()) {
                readFile(file, imageCraft, true);
            } else {
                java.awt.EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        // Create new ImageCraft object
                        ImageCraft program = new ImageCraft();

                        // Show ImageCraft object
                        program.setVisible(true);

                        // Open BufferedImage in new layer
                        readFile(file, program, true);
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
     * Import a file to the current layer.
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
            readFile(file, imageCraft, false);

            System.out.println("Import " + file.toString());
        }
    }

    /**
     * Export a file as a GIF, JPG, or PNG.
     *
     * @param unique false: Overwrite last export file true: Write to a new
     * export file
     */
    protected void export(boolean unique) {
        if (unique || latestExport == null) {
            // Set available image file formats to PNG, GIF, JPG
            fileChooser.resetChoosableFileFilters();

            fileChooser.addChoosableFileFilter(pngFormat);
            fileChooser.addChoosableFileFilter(gifFormat);
            fileChooser.addChoosableFileFilter(jpgFormat);
            fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());
            fileChooser.setFileFilter(pngFormat);

            //Take a screenshot of all layers in the layerList
            //TODO: screenshot selection of layers (create ArrayList<Layer> 
            //for layers to be screenshot
            BufferedImage screenShot = screenshot(imageCraft.layerList);

            // Attempt JFileChooser export dialogue
            int exportOption = fileChooser.showDialog(imageCraft, "Export");
            // Returns JFileChooser.APPROVE_OPTION if the user clicked "Export"
            if (exportOption != JFileChooser.APPROVE_OPTION) {
                System.out.println("Export cancelled by user...");
                return;
            }

            // New file chosen to export image to
            latestExport = fileChooser.getSelectedFile();

            //Splits up the filename wherever a period is to get the extension
            String[] fileNameParts = latestExport.toString().split("\\.");
            String format;

            //Set the file's format based on which FileFilter is selected
            switch (fileChooser.getFileFilter().getDescription()) {
                case "GIF Images":
                    format = "gif";
                    break;
                case "PNG Images":
                    format = "png";
                    break;
                case "JPG Images":
                    format = "jpg";
                    break;
                default:
                    format = "png";
            }

            //If the latestExport string was able to be split then set the last
            //element to be the format.
            //Otherwise force the filename into the selected format
            if (fileNameParts.length > 1) {
                format = fileNameParts[fileNameParts.length - 1];
            } else {
                latestExport = new File(latestExport.toString() + "." + format);
            }
            
            BufferedImage filteredImage;
            //If exporting to a jpg or jpeg, export a BufferedImage of TYPE_INT_RGB
            //Else export as BufferedImage of TYPE_INT_ARGB
            if (format.equals("jpg") || format.equals("jpeg")) {
                filteredImage = new BufferedImage(
                        (int) imageCraft.drawingArea.getPreferredSize().getWidth(),
                        (int) imageCraft.drawingArea.getPreferredSize().getHeight(),
                        BufferedImage.TYPE_INT_RGB);
                filteredImage.getGraphics().drawImage(screenShot, 0, 0, null);
            } else {
                filteredImage = imageCraft.newBlankImage();
                filteredImage.getGraphics().drawImage(screenShot, 0, 0, null);
            }
            //Attempts to write the file and open it in the Desktop
            try {
                ImageIO.write(filteredImage, format, latestExport);
                try {
                    Desktop.getDesktop().open(new File(latestExport.toString()));
                } catch (IllegalArgumentException iae) {
                    System.out.println("File Not Found");
                }
            } catch (IOException err) {
            }
        }

        System.out.println("Export " + (unique ? "new " : "") + "to " + latestExport.toString());
    }

    private void readFile(File file, ImageCraft iC, boolean newIC) {
        try {
            Layer layer = newIC ? new Layer(iC) : iC.currentLayer;
            BufferedImage bufferedFile = ImageIO.read(file);

            //If the bufferedFile is too big resize it
            if (bufferedFile.getWidth() > imageCraft.drawingArea.getWidth()
                    || bufferedFile.getHeight() > imageCraft.drawingArea.getHeight()) {
                iC.drawingArea.resizeDrawing(
                        bufferedFile.getWidth() - iC.drawingArea.getWidth(),
                        bufferedFile.getHeight() - iC.drawingArea.getHeight());
            }
            layer.addHistory(bufferedFile, "Imported Image");
            iC.drawingArea.repaint();
        } catch (IOException err) {
            System.out.println("Not a real image..."); // You shmuck
        }
    }

    /**
     * Build a screenshot of the current drawing.
     *
     * @param layers which layers to show in screenshot
     * @return screenshot
     */
    private BufferedImage screenshot(ArrayList<Layer> layers) {
        // Create a new BufferedImage
        BufferedImage image = imageCraft.newBlankImage();
        Graphics g = image.getGraphics();

        // Draw a white background on the image
        g.setColor(Color.white);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Draw each layer on top of the background
        for (int i = layers.size() - 1; i > -1; i--) {
            Layer layer = layers.get(i);
            if (layer.getUndoIndex() != -1) {
                g.drawImage(((History) layer.getHistoryArray().get(layer.getUndoIndex())).getFinalImage(), 0, 0, null);
            }
        }

        // Return the screenshot
        return image;
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
    private static final FileNameExtensionFilter imageFormats = new FileNameExtensionFilter("All Image Formats", "gif", "jpg", "jpeg", "png");
    private static final FileNameExtensionFilter gifFormat = new FileNameExtensionFilter("GIF Images", "gif");
    private static final FileNameExtensionFilter jpgFormat = new FileNameExtensionFilter("JPG/JPEG Images", "jpg", "jpeg");
    private static final FileNameExtensionFilter pngFormat = new FileNameExtensionFilter("PNG Images", "png");

    // When something has been saved or exported, save to appropriate string.
    // When "Save" is clicked, lastSave is overwritten.
    // When "Save As" is clicked, new save is executed.
    // When "Export is clicked, lastExport is overwritten.
    // When "Export As" is clicked, new export is executed.
    private File latestSave;
    private File latestExport;
    // End of variables declaration
}
