package com.br.i9.Class;
import java.util.List;

import com.br.i9.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class CategoriasAdapter extends BaseAdapter {

	Context context;
	protected List<Categorias> listCategorias;
	LayoutInflater inflater;

	public CategoriasAdapter(Context context, List<Categorias> listCategorias) {
		this.listCategorias = listCategorias;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public int getCount() {
		return listCategorias.size();
	}

	public Categorias getItem(int position) {
		return listCategorias.get(position);
	}
	
	public long getItemId(int position) {
		return listCategorias.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) 
		{
			holder = new ViewHolder();
			convertView = this.inflater.inflate(R.layout.layout_adapter_categorias, parent, false);

			holder.nomeCategoria = (TextView) convertView.findViewById(R.id.txtCategoria);

			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		Categorias categoria = listCategorias.get(position);
		holder.nomeCategoria.setText(categoria.getnmCategoria());
		holder.nomeCategoria.setId(Integer.parseInt(categoria.getgrCategoria()));


		return convertView;
	}

	private class ViewHolder {
		TextView nomeCategoria;
	}

}

