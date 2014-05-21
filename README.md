ImageCraft README:
--------------------

ImageCraft has a collection of tools whose toggle buttons are at the top of the window on the right side. You will see Draw, Fill, Picker, Rectangle, Oval, Stroke Width, Crop, and a color selection/preview tool.

The canvas is where you draw below the toggle buttons. You will see three blue boxes on the right side, button right corner, and bottom side of the canvas area. You may drag and drop these blue squares to resize the canvas area.

On the left side, you see your layers for this current drawing. A drawing may have any number of layers. When you click "Select All," it will show how your current drawing looks with all of the layers selected. Otherwise, you may select a layer at a time to isolate that layer and work on it by itself.

The menu bar at the top has four options: File, Edit, Filter, and Help. The File option lets you interface with your operating system. The Edit option applies to the current layer (which will be colored red in the layer tree). The Filter options has different filters you can apply to the current layer as well. The Help option is entirely useless.

Tools:
	The Current Colors:
		In the top right of the window, you will see two rectangles and a swap image. The rectangle on top (and a little to the left) is the current color. For any tool that uses a color, this is the color it will pick from when you click with the left mouse button. When you click with the right mouse button with a tool that uses color, it will use the other color. Click on one of the rectangles to change its color. Click on the swap icon to exchange the two rectangles' colors.
	Draw Tool:
		Draws like a pen on the canvas. When the draw tool is selected, you may select a stroke width and pen style in the drop down to the left of the Crop tool and to the right of the Oval tool.
	Fill Tool:
		Is like pouring a bucket of paint into a specific color on the drawing. Click on a color in the canvas, and that color will be filled with the current color in the top left.
	Picker Tool:
		This will change the current color with the color that you click on ON THIS LAYER in the canvas area.
	Rectangle Tool:
		Draw a rectangle on the canvas area. Drag and drop to create it. You may select a stroke width and pen style in the drop down to the left of the Crop tool and to the right of the Oval tool.
	Oval Tool:
		Draw an oval on the canvas area. Drag and drop to create it. You may select a stroke width and pen style in the drop down to the left of the Crop tool and to the right of the Oval tool.
	Crop Tool:
		When you resize the canvas area with the blue boxes, NO INFORMATION IS ACTUALLY LOST. The drawings and images are saved as they were before the resize. However, if you click the Crop tool, you will then lose the information and crop the image to the viewable area only.

Layers:
	General functionality:
		When you click on a layer, it selects it. The layer highlights, its color turns red, and it becomes viewable in the canvas area. If you hold down the CTRL key or the SHIFT key, you may select multiple layers. When multiple layers are selected, they will all be viewable in the canvas area. However, when multiple layers are selected, when you draw, it only draws to the CURRENT LAYER, which is highlighted in RED. When multiple layers are selected, you may change which layer is currently selected by right clicking and selecting "Set as Current Layer." Click on "New" to create a new layer.

	History:
		When you click on the little icon to the left of a layer, it will expand that layer. Each layer contains a list of history items. Every time that you take an action on the layer, it will add a history item to this list. You may select a current history item to view what it specifically has in it, which will show up in the canvas area. 
		You will see that when you undo (CTRL+Z) or redo (CTRL+Y), the colors of these history items change. The blue-text history items are current in the history list. Any black-text history items are not currently showing because they have been undone. You may redo them to make them part of the layer again. If you make new action when there are history items that are black-text, they will be lost.

	Menu Options:
		General:
			The menu is the same for layers and for history items. Depending on what you click on, certain menu options will be enabled or disabled.
		Set as Current Layer:
			When you have multiple layers selected, setting a current layer means that even though multiple layers will be visible, this will be the layer that you are currently working on.
		Rename:
			Change the layer's or history item's name. You may not change the name of the Background Layer.
		Move Up / Down:
			Move the layer or history item up or down in the layer tree. You may not move the Background Layer up or down. Absolutely no moving left or right.
'		Clear:
			Empties the layer's history.
		Delete:
			Deletes the layer or history item. You may not delete the Background Layer.

Menu Bar:
	General:
		Everything is pretty intuitive. But there are some differences between File->Open and Edit->Import. When you File->Open anything, it will open in a new instance of ImageCraft, unless you have not done anything in the current instance of ImageCraft. When you Edit->Import, you may ONLY import an IMAGE, which will be imported as a history item to the current layer.
		