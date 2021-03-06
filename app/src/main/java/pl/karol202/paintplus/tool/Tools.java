package pl.karol202.paintplus.tool;

import pl.karol202.paintplus.AsyncManager;
import pl.karol202.paintplus.image.Image;
import pl.karol202.paintplus.image.Image.OnImageChangeListener;
import pl.karol202.paintplus.tool.brush.ToolBrush;
import pl.karol202.paintplus.tool.fill.ToolFill;
import pl.karol202.paintplus.tool.marker.ToolMarker;
import pl.karol202.paintplus.tool.pan.ToolPan;
import pl.karol202.paintplus.tool.pickcolor.ToolColorPick;
import pl.karol202.paintplus.tool.selection.ToolSelection;
import pl.karol202.paintplus.tool.shape.ToolShape;

import java.util.ArrayList;

public class Tools
{
	private ArrayList<Tool> tools;
	
	public Tools(Image image, OnImageChangeListener listener, AsyncManager asyncManager)
	{
		tools = new ArrayList<>();
		tools.add(new ToolPan(image));
		tools.add(new ToolMarker(image));
		tools.add(new ToolBrush(image));
		tools.add(new ToolFill(image, asyncManager));
		tools.add(new ToolShape(image, listener));
		tools.add(new ToolSelection(image));
		tools.add(new ToolColorPick(image));
	}
	
	public Tool getTool(int id)
	{
		return tools.get(id);
	}
	
	public int getToolId(Tool tool)
	{
		for(int i = 0; i < tools.size(); i++)
		{
			Tool next = tools.get(i);
			if(next == tool) return i;
		}
		return -1;
	}
	
	public ArrayList<Tool> getTools()
	{
		return tools;
	}
}