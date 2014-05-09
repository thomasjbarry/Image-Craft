Image-Craft
===========

Bugs:
	LayerTree
		update finalImage of history objects: move up/down, delete
			in its own method so we can call that method to rebuild the layers
		undo-redo
TODO:

	ZACH:
		Undo-redo problems

	THOMAS: 
		LayerTree bug

	DREW: 
		Scale filter: SimpleTool method => Resizing Penstrokes && Resize Tool
		Importing image as penstroke

High priority:
	Undo/redo
	Pretty gooey
	Clean code
	Commenting
	Disposing graphics

No Time:
	Tools: Select, Eraser (BufferedImageOp to selection? Change drawing type like clearRect?)

	Medium priority:
		Find all Graphics objects and make sure that whenever they are assigned, they are disposed. We're lagging.
			From the API: "For efficiency, programmers should call dispose when finished using a Graphics object
				       only if it was created directly from a component or another Graphics object."
			That is to say: do not dispose of the Graphics object unless you initialized it within the 
				same method.

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