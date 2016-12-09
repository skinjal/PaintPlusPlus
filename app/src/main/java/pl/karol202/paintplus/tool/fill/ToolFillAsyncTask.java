package pl.karol202.paintplus.tool.fill;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import pl.karol202.paintplus.Image;
import pl.karol202.paintplus.color.ColorsSet;

import java.util.Stack;

public class ToolFillAsyncTask extends AsyncTask<FillParams, Void, Bitmap>
{
	public interface OnFillCompleteListener
	{
		void onFillComplete(Bitmap bitmap);
	}
	
	private final float COLOR_COMPARISION_CONST = (float) Math.sqrt(3 * Math.pow(255, 2));
	
	private OnFillCompleteListener listener;
	private Bitmap bitmap;
	private int destColor;
	private float threshold;
	private int x;
	private int y;
	
	@Override
	protected Bitmap doInBackground(FillParams... paramsArray)
	{
		if(paramsArray.length != 1)
			throw new IllegalArgumentException("There must be only one params object passed to ToolFillAsyncTask.");
		FillParams params = paramsArray[0];
		Image image = params.getImage();
		ColorsSet colorsSet = image.getColorsSet();
		
		listener = params.getListener();
		bitmap = image.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
		destColor = colorsSet.getFirstColor();
		threshold = params.getThreshold();
		x = params.getX();
		y = params.getY();
		
		fill();
		return bitmap;
	}
	
	private void fill()
	{
		int touchedColor = bitmap.getPixel(x, y);
		if(touchedColor == destColor) return;
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		
		Stack<Point> pointsToCheck = new Stack<>();
		pointsToCheck.push(new Point(x, y));
		while(!pointsToCheck.isEmpty())
		{
			Point point = pointsToCheck.pop();
			int pos = point.y * width + point.x;
			int color = pixels[pos];
			
			if(!checkColor(touchedColor, color)) continue;
			pixels[pos] = destColor;
			
			if(point.x > 0)
				pointsToCheck.add(new Point(point.x - 1, point.y));
			if(point.y > 0)
				pointsToCheck.add(new Point(point.x, point.y - 1));
			if(point.x < width - 1)
				pointsToCheck.add(new Point(point.x + 1, point.y));
			if(point.y < height - 1)
				pointsToCheck.add(new Point(point.x, point.y + 1));
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
	}
	
	private boolean checkColor(int touched, int current)
	{
		if(current == touched) return true;
		if(threshold == 0f) return false;
		else
		{
			int distanceR = Color.red(touched) - Color.red(current);
			int distanceG = Color.green(touched) - Color.green(current);
			int distanceB = Color.blue(touched) - Color.blue(current);
			float distance = (float) Math.sqrt(Math.pow(distanceR, 2) + Math.pow(distanceG, 2) + Math.pow(distanceB, 2));
			float percentage = distance / COLOR_COMPARISION_CONST * 100;
			return percentage <= threshold;
		}
	}
	
	@Override
	protected void onPostExecute(Bitmap bitmap)
	{
		super.onPostExecute(bitmap);
		if(listener != null) listener.onFillComplete(bitmap);
	}
}