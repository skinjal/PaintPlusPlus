package pl.karol202.paintplus.tool.marker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import pl.karol202.paintplus.R;
import pl.karol202.paintplus.tool.ToolProperties;
import pl.karol202.paintplus.util.SeekBarTouchListener;

import java.util.Locale;

public class MarkerProperties extends ToolProperties implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener
{
	private ToolMarker marker;

	private View view;
	private SeekBar seekMarkerSize;
	private TextView textMarkerSize;
	private SeekBar seekMarkerTranslucency;
	private TextView textMarkerTranslucency;
	private CheckBox checkSmooth;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.properties_marker, container, false);
		marker = (ToolMarker) tool;
		
		seekMarkerSize = (SeekBar) view.findViewById(R.id.seekBar_marker_size);
		seekMarkerSize.setProgress((int) (marker.getSize() - 1));
		seekMarkerSize.setOnSeekBarChangeListener(this);
		seekMarkerSize.setOnTouchListener(new SeekBarTouchListener());
		
		textMarkerSize = (TextView) view.findViewById(R.id.marker_size);
		textMarkerSize.setText(String.valueOf(seekMarkerSize.getProgress() + 1));
		
		seekMarkerTranslucency = (SeekBar) view.findViewById(R.id.seekBar_marker_translucency);
		seekMarkerTranslucency.setProgress((int) ((1 - marker.getOpacity()) * 100));
		seekMarkerTranslucency.setOnSeekBarChangeListener(this);
		seekMarkerTranslucency.setOnTouchListener(new SeekBarTouchListener());
		
		textMarkerTranslucency = (TextView) view.findViewById(R.id.marker_translucency);
		textMarkerTranslucency.setText(String.format(Locale.US, "%1$d%%", seekMarkerTranslucency.getProgress()));
		
		checkSmooth = (CheckBox) view.findViewById(R.id.check_smooth);
		checkSmooth.setChecked(marker.isSmooth());
		checkSmooth.setOnCheckedChangeListener(this);
		return view;
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{
		if(seekBar == seekMarkerSize) setMarkerSize(progress);
		else if(seekBar == seekMarkerTranslucency) setMarkerTranslucency(progress);
	}
	
	private void setMarkerSize(int size)
	{
		marker.setSize(size);
		textMarkerSize.setText(String.valueOf(size + 1));
	}
	
	private void setMarkerTranslucency(int translucency)
	{
		marker.setOpacity(1 - (translucency / 100f));
		textMarkerTranslucency.setText(String.format(Locale.US, "%1$d%%", translucency));
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) { }
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) { }
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		marker.setSmooth(isChecked);
	}
}
