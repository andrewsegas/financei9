package com.br.i9.Fragments;

import com.br.i9.Database.CrudDatabase;
import android.app.AlertDialog.Builder;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

public class NovaTransacao extends Fragment {
	EditText nmNovaCategoria;
	CrudDatabase bd;
	Builder Popup;
	String label;
	View viewLista;	int CorGrafica;
/*
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		viewLista = inflater.inflate(R.layout.nova_categoria, null);
		final Spinner spinnerTipo = (Spinner) viewLista.findViewById(R.id.spinnerTipo);
		bd = new CrudDatabase(getActivity());
		nmNovaCategoria = (EditText) viewLista.findViewById(R.id.txtNomeCategoria);
		Button btnCadastar = (Button)viewLista.findViewById(R.id.btnCadastar);
		View escolherCor = vi	ewLista.findViewById(R.id.escolherCor);

		//popularSpinnerTipoCategoria(spinnerTipo);

		
	};*/
}
