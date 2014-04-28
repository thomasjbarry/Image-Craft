Image-Craft
===========

Bugs:
	LayerTree
		update finalImage of history objects: move up/down, delete
			in its own method so we can call that method to rebuild the layers
TODO:

	ZACH:
		IO.java:
			ImageCraft format - See IO.imageCraftFormat

	THOMAS: 
		Filters - function to build all selected layers into copy BufferedImage
		Background of drawingArea to checkerboard/crosshair/etc

	DREW: 
		2/3 Pen strokes and 5 Filters(different types?)

	High priority:
		Other types of layouts
	
		Tools: Select, Crop,  Eraser (BufferedImageOp to selection? Change drawing type like clearRect?)

	Low Priority:
		Merge Down layers (merges to bottommost selected layer)
		
		Resize (scale),
		
		System Clipboard: Copy/Paste
		
		When resizing, draw currently selected layers as alpha to jDesk

		Drag and Drop: How to modify the size/location of an object after creating it (shapes, pasting pictures, etc.)

		Clean up ColorPicker to be more user friendly

		Anti-Aliasing
		
		LayerTree rename, drag&drop

		Custom PopupDialog to allow for saving, exporting selections, specific layers, or all layers