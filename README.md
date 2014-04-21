Image-Craft
===========

Bugs:

	LayerTree- doesn't repaint drawing area upon deleting first drawn SimpleHistory object
				StackOverFlowError when selecting SH objects
	Export- If no file extension is written in the filename the file will not save
	Shapes- redraws the entire drawing area making it choppy

TODO:

	ZACH: Shapes Bug (layered JPanels), More efficient Fill Tool

	THOMAS: LayerTree bugs

	DREW: Easel: MouseEvent- on mouse over change mouse icon

	For anyone:
	
		IO.java:
			ImageCraft format - See IO.imageCraftFormat
			
		Tools: Draw (Penstrokes), Eraser, Select, Zoom, Resize, Text

		System Clipboard: Copy/Paste
		
		Complex History class (filters-- BufferedImageOp)		

		Drag and Drop: How to modify the size/location of an object after creating it (shapes, pasting pictures, etc.)

		Clean up ColorPicker to be more user friendly

		Anti-Aliasing
		
		Custom PopupDialog to allow for saving, exporting selections, specific layers, or all layers