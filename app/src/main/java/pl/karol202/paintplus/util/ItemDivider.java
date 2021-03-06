package pl.karol202.paintplus.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import pl.karol202.paintplus.R;

public class ItemDivider extends RecyclerView.ItemDecoration
{
	private Drawable divider;
	
	public ItemDivider(Context context)
	{
		divider = ContextCompat.getDrawable(context, R.drawable.divider);
	}
	
	@Override
	public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state)
	{
		int left = parent.getPaddingLeft();
		int right = parent.getWidth() - parent.getPaddingRight();
		
		int childCount = parent.getChildCount();
		for(int i = 0; i < childCount; i++)
		{
			View child = parent.getChildAt(i);
			RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			int top = child.getBottom() + params.bottomMargin;
			int bottom = top + divider.getIntrinsicHeight();
			divider.setBounds(left, top, right, bottom);
			divider.draw(canvas);
		}
	}
}