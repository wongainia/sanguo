package com.vikings.sanguo.ui.window;

import android.view.View;
import android.widget.ProgressBar;

import com.vikings.sanguo.R;

public class ListLoading {

	private View parent;
	
	private boolean running;
	
	private ProgressBar loading;
	
	public ListLoading(View v){
		if(v != null){
			this.parent = v;
			this.loading = (ProgressBar)v.findViewById(R.id.footLoading);
		}
	}
	
	public void start(){
		this.running = true;
		if(parent != null){
			loading.setVisibility(View.VISIBLE);
			parent.setVisibility(View.VISIBLE);
		}
		
	}
	
	public void stop(){
		this.running = false;
		if(parent != null){
			loading.setVisibility(View.GONE);
			parent.setVisibility(View.GONE);
		}
	}
	
	public boolean isStarted(){
		return running;
	}
}
