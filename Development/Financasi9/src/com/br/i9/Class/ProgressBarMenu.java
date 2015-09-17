package com.br.i9.Class;

import com.br.i9.R;
import android.app.Activity;
import android.view.MenuItem;

public class ProgressBarMenu {

	public static void atualizar(final Activity activity, final MenuItem mi){
		mi.setActionView(R.layout.progress_bar_menu);
		
		new Thread(){
			public void run(){
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				activity.runOnUiThread(new Runnable(){
					public void run(){
						mi.setActionView(null);
						mi.setIcon(R.drawable.ic_action_refresh);
					}
				});
			}
		}.start();
	}

}
