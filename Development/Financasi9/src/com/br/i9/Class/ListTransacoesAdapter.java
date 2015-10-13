package com.br.i9.Class;

import java.util.List;
import com.br.i9.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListTransacoesAdapter extends BaseAdapter {

	Context context;
	protected List<Transacoes> listTransacoes;
	LayoutInflater inflater;
	private String cor;
	CorValor CorValor;

	public ListTransacoesAdapter(Context context, List<Transacoes> _listTransacoes, String corIngles) {
		this.listTransacoes = _listTransacoes;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.cor = corIngles;
		
		CorValor = new CorValor();
	}

	public int getCount() {
		return listTransacoes.size();
	}

	public Transacoes getItem(int position) {
		return listTransacoes.get(position);
	}
	
	public long getItemId(int position) {
		return listTransacoes.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {

			holder = new ViewHolder();
			convertView = this.inflater.inflate(R.layout.layout_list_item, parent, false);

			holder.txtEstabelecimento = (TextView) convertView.findViewById(R.id.txt_estabelecimento);
			holder.txtDthora = (TextView) convertView.findViewById(R.id.txt_dtHora);
			holder.txtValor = (TextView) convertView.findViewById(R.id.txt_valor);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Transacoes transacoes = listTransacoes.get(position);
		holder.txtEstabelecimento.setText(transacoes.getestabelecimento());
		holder.txtDthora.setText(transacoes.getdtHora());
		holder.txtValor.setText(CorValor.mudarCorValor(transacoes.getvalor()+"", this.cor));

		return convertView;
	}

	private class ViewHolder {
		TextView txtEstabelecimento;
		TextView txtDthora;
		TextView txtValor;
	}
}
