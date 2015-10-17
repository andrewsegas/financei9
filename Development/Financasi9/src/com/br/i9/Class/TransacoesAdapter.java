/**
 * 
 */
package com.br.i9.Class;
import java.util.ArrayList;
import java.util.List;

import com.br.i9.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @author Cesar
 * Somente para a tela de Transacoes
 */
public class TransacoesAdapter extends BaseAdapter {

	Context context;
	protected List<Transacoes> listTransacoes;
	LayoutInflater inflater;
	private String cor;
	CorValor CorValor;
	Boolean _selectAllCheckbox;
	com.br.i9.Fragments.Transacoes transacoesFragment;
	
	public TransacoesAdapter(Context context, List<Transacoes> listTransacoes, String corIngles, Boolean selectAllCheckbox) {
		this.listTransacoes = listTransacoes;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
		this.cor = corIngles;
		this._selectAllCheckbox = selectAllCheckbox;
		CorValor = new CorValor();
		transacoesFragment = new com.br.i9.Fragments.Transacoes(); 

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

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) 
		{
			holder = new ViewHolder();
			convertView = this.inflater.inflate(R.layout.layout_adapter_transacoes, parent, false);

			holder.txtEstabelecimento = (TextView) convertView.findViewById(R.id.txt_estabelecimento);
			holder.txtTipo = (TextView) convertView.findViewById(R.id.txtTipo);
			holder.txt_dtHora = (TextView) convertView.findViewById(R.id.txt_dtHora);
			holder.txt_valor = (TextView) convertView.findViewById(R.id.txt_valor);
			holder.txt_card = (TextView) convertView.findViewById(R.id.txt_card);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBoxTransacao);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		Transacoes transacoes = listTransacoes.get(position);
		holder.txtEstabelecimento.setText(transacoes.getestabelecimento());
		holder.txtTipo.setText(transacoes.getTipo());
		holder.txt_dtHora.setText(transacoes.getdtHora());
		holder.txt_card.setText(transacoes.getnrCartao());
		holder.checkbox.setChecked(transacoes.getCheck());
		
		if(transacoes.getRecDesp().contains("1"))
			holder.txt_valor.setText(CorValor.mudarCorValor("R$ " + transacoes.getvalor(), "gray"));
		else
			holder.txt_valor.setText(CorValor.mudarCorValor("R$ " + transacoes.getvalor(), this.cor));

		holder.checkbox.setTag(position);
		
		holder.checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton vw, boolean isChecked) {
            	transacoesFragment.itemSelecionado(isChecked, (int) holder.checkbox.getTag(), (ArrayList<Transacoes>) listTransacoes, _selectAllCheckbox);
            }
        });

		return convertView;
	}

	private class ViewHolder {
		TextView txtEstabelecimento;
		TextView txtTipo;
		TextView txt_dtHora;
		TextView txt_valor;
		TextView txt_card;
		CheckBox checkbox;
	}

}

