package com.br.i9.Class;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AjusteListView {
	
	public void ajustarListViewInScrollView(ListView listView) {
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null)
	        return;

	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
	    int totalHeight = 0;
	    View view = null;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        view = listAdapter.getView(i, view, listView);
	        if (i == 0)
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	        totalHeight += view.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	    listView.requestLayout();
	    
	    ajustarExpansaoListView(view, listAdapter, listView);
	}
	
	@SuppressWarnings("unused")
	public void ajustarExpansaoListView(View view, ListAdapter listAdapter, ListView listView)
	{
		ExpandableListView: view = listAdapter.getView(0, view, listView);
		int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.EXACTLY);
		int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.EXACTLY);
		view.measure(widthMeasureSpec, heightMeasureSpec);
	}
	
	public void validarExistenciaDados(TextView textView, Boolean lVisible)
	{
		if (lVisible){
			textView.setVisibility(View.VISIBLE);
		}else{
			textView.setVisibility(View.INVISIBLE);
		}
	}

}
