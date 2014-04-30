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
		LayerTree bug

	DREW: 
		Scale filter: SimpleTool method => Resizing Penstrokes && Resize Tool
		Importing image as penstroke

	High priority:
		Other types of layouts
	
		Tools: Select, Eraser (BufferedImageOp to selection? Change drawing type like clearRect?)

	Low Priority:
		Merge Down layers (merges to bottommost selected layer)
		
		System Clipboard: Copy/Paste
		
		When resizing, draw currently selected layers as alpha to jDesk

		Drag and Drop: How to modify the size/location of an object after creating it (shapes, pasting pictures, etc.)

		Clean up ColorPicker to be more user friendly

		Anti-Aliasing
		
		LayerTree rename, drag&drop

		Custom PopupDialog to allow for saving, exporting selections, specific layers, or all layers

		Stamp Tool