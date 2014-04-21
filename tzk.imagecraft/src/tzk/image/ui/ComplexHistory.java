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


public class ComplexHistory {
    public ComplexHistory(ImageCraft iC, Layer layer, String filter) {
        this.imageCraft = iC;
        this.layerObject = layer;
        this.filterType = filter;
    }
    
    /**
     * Calls the proper filter method with the index of this ComplexHistory
     * object in its layer's HistoryArray
     */
    protected void filter() {
        switch (filterType) {
            case "Grayscale":
                grayScale((short) layerObject.getHistoryArray().indexOf(this));
                break;
            default:
                break;
        }
    }
    
    /**
     * Grayscale filter applied to all SimpleHistory objects before this object
     * in layerObject's HistoryArray.
     * @param index 
     */
    protected void grayScale(short index) {
        for (int i = 0; i < index; i++) {
            //apply grayscale BufferedImageOp to layerObject.getHistoryArray.get(i) (if SimpleHistory);
        }
    }
    //Declare Variables
    private final ImageCraft imageCraft;
    private final Layer layerObject;
    private final String filterType;
}
