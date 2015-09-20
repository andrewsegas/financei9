package com.br.i9.Class;
import java.util.List;

import com.br.i9.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CategoriasAdapter extends BaseAdapter {

	Context context;
	protected List<Categorias> listCategorias;
	LayoutInflater inflater;
	List<Integer> imageId;

	public CategoriasAdapter(Context context, List<Categorias> listCategorias, List<Integer> Arrayimage) {
		this.listCategorias = listCategorias;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.imageId = Arrayimage;
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
			holder.img =(ImageView) convertView.findViewById(R.id.imageView1);   

			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		Categorias categoria = listCategorias.get(position);
		holder.nomeCategoria.setText(categoria.getnmCategoria());
		holder.nomeCategoria.setId(Integer.parseInt(categoria.getgrCategoria()));
		holder.img.setImageResource(imageId.get(position));

		return convertView;
	}

	private class ViewHolder {
		TextView nomeCategoria;
		ImageView img;
	}

}

