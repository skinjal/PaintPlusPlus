package pl.karol202.paintplus.tool.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import pl.karol202.paintplus.image.Image.OnImageChangeListener;
import pl.karol202.paintplus.color.ColorsSet;

public abstract class Shape
{
	private boolean smooth;
	private float opacity;
	
	private OnImageChangeListener imageChangeListener;
	private OnShapeEditListener shapeEditListener;
	private boolean editMode;
	private Paint paint;
	private ColorsSet colors;
	
	public Shape(ColorsSet colors, OnImageChangeListener imageChangeListener, OnShapeEditListener shapeEditListener)
	{
		this.smooth = true;
		this.opacity = 1;
		
		this.imageChangeListener = imageChangeListener;
		this.shapeEditListener = shapeEditListener;
		this.paint = new Paint();
		this.colors = colors;
	}
	
	public abstract int getName();
	
	public abstract int getIcon();
	
	public abstract Class<? extends ShapeProperties> getPropertiesClass();
	
	public abstract boolean onTouch(MotionEvent event);
	
	public abstract void onScreenDraw(Canvas canvas);
	
	public abstract void apply(Canvas imageCanvas);
	
	public abstract void cancel();
	
	public float calcDistance(Point point, int x, int y)
	{
		return (float) Math.sqrt(Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2));
	}
	
	public void update()
	{
		updateColor();
		paint.setAntiAlias(smooth);
		imageChangeListener.onImageChanged();
	}
	
	public void updateColor()
	{
		paint.setColor(colors.getFirstColor());
		paint.setAlpha((int) (opacity * 255));
	}
	
	public void cleanUp()
	{
		editMode = false;
		imageChangeListener.onImageChanged();
	}
	
	public boolean isInEditMode()
	{
		return editMode;
	}
	
	public void enableEditMode()
	{
		editMode = true;
		shapeEditListener.onStartShapeEditing();
	}
	
	public Paint getPaint()
	{
		return paint;
	}
	
	public boolean isSmooth()
	{
		return smooth;
	}
	
	public void setSmooth(boolean smooth)
	{
		this.smooth = smooth;
		update();
	}
	
	public float getOpacity()
	{
		return opacity;
	}
	
	public void setOpacity(float opacity)
	{
		this.opacity = opacity;
		update();
	}
}