package com.br.i9.Fragments;

import com.br.i9.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
 
public class CountryFragment extends Fragment{
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
 
        int position = getArguments().getInt("position");
 
        String[] countries = getResources().getStringArray(R.array.itens);
 
        View v = inflater.inflate(R.layout.fragment_layout, container, false);
 
        TextView tv = (TextView) v.findViewById(R.id.tv_content);
 
        tv.setText(countries[position]);
 
        return v;
    }
}