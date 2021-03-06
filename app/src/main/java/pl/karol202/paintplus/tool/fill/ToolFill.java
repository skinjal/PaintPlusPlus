package pl.karol202.paintplus.tool.fill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Region;
import android.os.AsyncTask;
import android.view.MotionEvent;
import pl.karol202.paintplus.AsyncBlocker;
import pl.karol202.paintplus.AsyncManager;
import pl.karol202.paintplus.R;
import pl.karol202.paintplus.image.Image;
import pl.karol202.paintplus.image.Layer;
import pl.karol202.paintplus.tool.Tool;
import pl.karol202.paintplus.tool.ToolProperties;
import pl.karol202.paintplus.tool.fill.ToolFillAsyncTask.OnFillCompleteListener;

public class ToolFill extends Tool implements OnFillCompleteListener, AsyncBlocker
{
	private float fillThreshold;
	private float opacity;
	
	private Canvas canvas;
	private AsyncManager asyncManager;
	private AsyncTask asyncTask;
	
	public ToolFill(Image image, AsyncManager asyncManager)
	{
		super(image);
		this.fillThreshold = 0;
		this.opacity = 1;
		
		this.asyncManager = asyncManager;
	}
	
	@Override
	public int getName()
	{
		return R.string.tool_fill;
	}
	
	@Override
	public int getIcon()
	{
		return R.drawable.ic_tool_fill_black_24dp;
	}
	
	@Override
	public Class<? extends ToolProperties> getPropertiesFragmentClass()
	{
		return FillProperties.class;
	}
	
	@Override
	public boolean isLayerSpace()
	{
		return true;
	}
	
	@Override
	public boolean onTouch(MotionEvent event)
	{
		Layer layer = image.getSelectedLayer();
		if(event.getX() < 0 || event.getY() < 0 ||
		   event.getX() > layer.getWidth() - 1 || event.getY() > layer.getHeight() - 1)
			return false;
		
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			canvas = image.getSelectedCanvas();
			if(canvas == null) return false;
			
			if(!asyncManager.block(this)) return false;
			cancelClipping();
			
			FillParams params = new FillParams(this, image, fillThreshold, 1 - opacity, (int) event.getX(), (int) event.getY());
			asyncTask = new ToolFillAsyncTask().execute(params);
		}
		return false;
	}
	
	private void cancelClipping()
	{
		canvas.clipRect(0, 0, canvas.getWidth(), canvas.getHeight(), Region.Op.UNION);
	}
	
	@Override
	public void onScreenDraw(Canvas canvas) { }
	
	@Override
	public void onFillComplete(Bitmap bitmap)
	{
		canvas.drawBitmap(bitmap, 0, 0, null);
		image.updateImage();
		if(!asyncManager.unblock(this)) throw new RuntimeException("Unable to unblock async blocker.");
	}
	
	@Override
	public void cancel()
	{
		asyncTask.cancel(true);
		if(!asyncManager.unblock(this)) throw new RuntimeException("Unable to unblock async blocker.");
	}
	
	public float getFillThreshold()
	{
		return fillThreshold;
	}
	
	public void setFillThreshold(float fillThreshold)
	{
		this.fillThreshold = fillThreshold;
	}
	
	public float getOpacity()
	{
		return opacity;
	}
	
	public void setOpacity(float opacity)
	{
		this.opacity = opacity;
	}
}