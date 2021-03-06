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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * IO handles all the appropriate functionality for the importing and saving of
 * ImageCraft projects. Such as opening, importing, saving, exporting...
 *
 * Contributers: Thomas James Barry/ thomasbarry92@gmail.com /5076942 Zachary
 *              Gateley/ zach.cykic@gmail.com /5415772 K Drew Gonzales/
 *              drewgonzales360@gmail.com /5470602
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

        // Reuse this fileChooser each time
        // It only allows choosing one file at a time
        fileChooser = new JFileChooser();
    }

    /**
     * Opens the FileChooser window and allows the user to pick a file.
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
        // Must be final to be able to be read in invokeLaters
        final File file = fileChooser.getSelectedFile();
        if (!file.exists()) {
            // Nothing to do
            return;
        }

        // Get extension
        String fileName = file.toString();
        int j = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        fileName = fileName.substring(j + 1);
        String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        // No actions taken on this ImageCraft?
        boolean newCraft = imageCraft.numLayer == 1 && imageCraft.layerList.get(0).getHistoryArray().isEmpty();

        // ImageCraft Format
        if (ext.equals("icf")) {
            if (newCraft) {
                // Open into this imageCraft
                openImageCraft(file, imageCraft);
            } else {
                // Open ICF file in new ImageCraft instance
                java.awt.EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        // Create new ImageCraft object
                        final ImageCraft iC = new ImageCraft();

                        // Must invokeLater because of NullPointerExceptions
                        java.awt.EventQueue.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                openImageCraft(file, iC);
                            }

                        });

                        // Show ImageCraft object
                        // Should repaint
                        iC.setVisible(true);
                    }
                });
            }
        } // GIF, JPEG, or PNG
        else {
            //If we have never made a new Layer or made a new history element
            //in the background layer then open the file in this ImageCraft in
            //a new layer
            if (newCraft) {
                readFile(file, imageCraft, true);
            } // Otherwise open it in a new ImageCraft instance
            else {
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
     * Save a file in ImageCraft format ("icf"). Add all layers together into
     * one PNG. Save these files into one file but make the first line
     * proprietary: the color value of the first pixel in the first line of the
     * image translates to the number of layers in the drawing.
     *
     * @param unique false: Overwrite last save file true: Write to new save
     * file
     */
    protected void save(boolean unique) {
        // For new saves or saving to a current file, no fileChooser is necessary
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

        // Get extension
        // If extension is empty, set to "icf"
        String fileName = latestSave.toString();
        int j = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));
        fileName = fileName.substring(j + 1);
        int lastPeriod = fileName.lastIndexOf(".");
        if (lastPeriod < 0 || !fileName.substring(lastPeriod + 1).toLowerCase().equals("icf")) {
            String ext = "icf";
            if (fileName.charAt(fileName.length() - 1) != '.') {
                ext = '.' + ext;
            }
            latestSave = new File(latestSave.getPath() + ext);
        }

        // Compile all layers into one beefy BufferedImage
        BufferedImage tempImage;
        Graphics tempGraphics;
        int width = imageCraft.drawingArea.getWidth();
        int height = imageCraft.drawingArea.getHeight();
        int size = imageCraft.layerList.size();
        BufferedImage saveTo = new BufferedImage(
                width, 1 + height * size, BufferedImage.TYPE_INT_ARGB);
        Graphics saveGraphics = saveTo.getGraphics();
        saveTo.setRGB(0, 0, size);
        for (int i = 0; i < size; i++) {
            tempImage = imageCraft.newBlankImage();
            tempGraphics = tempImage.getGraphics();
            tempGraphics.drawImage(imageCraft.layerList.get(i).getSnapshot(), 0, 0, null);
            saveGraphics.drawImage(tempImage, 0, 1 + (size - i - 1) * height, null);
            tempGraphics.dispose();
        }
        saveGraphics.dispose();

        // Save image to operating system
        // First save as PNG format in ".icf" file
        // PNG is lossless
        try {
            ImageIO.write(saveTo, "png", latestSave);
        } catch (IOException err) {
            System.out.println("Could not save file!");
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
            } else {
                filteredImage = imageCraft.newBlankImage();
            }
            Graphics filteredGraphics = filteredImage.getGraphics();
            filteredGraphics.drawImage(screenShot, 0, 0, null);
            filteredGraphics.dispose();

            //Attempts to write the file and open it in the Desktop
            try {
                ImageIO.write(filteredImage, format, latestExport);
                try {
                    Desktop.getDesktop().open(new File(latestExport.toString()));
                } catch (IllegalArgumentException iae) {
                    System.out.println("File Not Found");
                }
            } catch (IOException err) {
                System.out.println("Error in writing.");
            }
        }

        System.out.println("Export " + (unique ? "new " : "") + "to " + latestExport.toString());
    }

    /**
     * ReadFile properly reads the image file into a buffered image, then
     * resizes the drawing area. When new files are read, if the layer is empty,
     * it makes a new layer.
     *
     * @param file
     * @param iC
     * @param newIC
     */
    private void readFile(File file, ImageCraft iC, boolean newIC) {
        try {
            //The layer we're importing/opening the file to
            Layer layer = newIC ? new Layer(iC) : iC.currentLayer;

            //Read the file and then draw it to a BufferedImage of TYPE_INT_ARGB
            //This guarantees that the file has an alpha (JPG's do not)
            BufferedImage readFile = ImageIO.read(file);
            BufferedImage bufferedFile = new BufferedImage(readFile.getWidth(),
                    readFile.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics bufferedGraphics = bufferedFile.getGraphics();
            bufferedGraphics.drawImage(readFile, 0, 0, null);
            bufferedGraphics.dispose();

            //If the bufferedFile is too big resize the Drawing Area
            if (bufferedFile.getWidth() > imageCraft.drawingArea.getWidth()
                    || bufferedFile.getHeight() > imageCraft.drawingArea.getHeight()) {
                iC.drawingArea.resizeDrawing(
                        bufferedFile.getWidth() - iC.drawingArea.getWidth(),
                        bufferedFile.getHeight() - iC.drawingArea.getHeight());

                iC.getEasel().resizeEasel(
                        bufferedFile.getWidth() - iC.drawingArea.getWidth() + 10,
                        bufferedFile.getHeight() - iC.drawingArea.getHeight() + 10);
            }

            //Create a new history object for the imported image and add it to
            //the layer
            layer.addHistory(bufferedFile, "Imported Image");
            iC.drawingArea.repaint();

        } catch (IOException err) {
            System.out.println("Not a real image..."); // You shmuck
        }
    }

    /**
     * Open ImageCraft format file. ImageCraft files are always opened in a new
     * file.
     *
     * @param file the file to open
     * @param iC the ImageCraft object it belongs to
     */
    private void openImageCraft(File file, ImageCraft iC) {
        // Open the file
        BufferedImage readImage;
        try {
            readImage = ImageIO.read(file);
        } catch (IOException err) {
            System.out.println("Could not open file!");
            return;
        }

        // Get information about file
        // The first pixel in the first line holds the number of layers
        int numLayers = readImage.getRGB(0, 0);
        int width = readImage.getWidth();
        int height = (readImage.getHeight() - 1) / numLayers;

        // Import all layers
        BufferedImage tempImage;
        Graphics tempGraphics;
        for (int i = 0; i < numLayers; i++) {
            // This is a new ImageCraft
            // If layer 0, use background layer that already exists
            // Otherwise, create a new layer.
            Layer layer;
            if (i == 0) {
                // Put in background layer
                layer = iC.layerList.get(iC.layerList.size() - 1);
            } else {
                layer = new Layer(iC);
            }
            tempImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            tempGraphics = tempImage.getGraphics();
            tempGraphics.drawImage(readImage,
                    0, 0, width, height,
                    0, i * height + 1, width, (i + 1) * height, null);
            tempGraphics.dispose();

            // Create a new history object for that layer
            layer.addHistory(tempImage, "Opened drawing");
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

        // Clean up resources
        g.dispose();

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
