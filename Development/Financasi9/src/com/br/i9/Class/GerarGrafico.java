package com.br.i9.Class;

import java.util.ArrayList;

import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;

public class GerarGrafico {

	static ArrayList<Entry> yVals1;
	static ArrayList<String> xVals;
	static ArrayList<Integer> colors;
	public GerarGrafico() {	}
	
	public static void GerarGraficoPie(PieChart mChart, float[] yDataValues, String[] xDataStrings, int[] cores)
	{        
        mChart.setCenterTextSize(22f);
        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
         
        // radius of the center hole in percent of maximum radius
        mChart.setHoleRadius(45f); 
        mChart.setTransparentCircleRadius(20f);
        mChart.setDescription("");
        
        Legend l = mChart.getLegend();
        l.setPosition(LegendPosition.BELOW_CHART_CENTER);	        
       
            AdicionarValores(yDataValues);
	        AdicionarTextos(xDataStrings);
	        AdicionarCores(cores);
        
	        PopularChart(mChart);
	}
	
	private static void PopularChart(PieChart mChart)
	{
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.WHITE);
        
        mChart.setData(data);
	}
	
	private static void AdicionarCores(int[] cores)
	{
		colors = new ArrayList<Integer>();
        
        for(int i = 0; i < cores.length; i++)
        {
	       colors.addAll(ColorTemplate.createColors(cores));
        }
        
        colors.add(ColorTemplate.getHoloBlue());
	}
	
	//Essa função passa a receber valor e converter para percentual
	private static void AdicionarValores(float[] yDataValues)
	{       
		yVals1 = new ArrayList<Entry>();
		float nfTotal = 0;
		float nfMult = 0;
        
		for (int i = 0; i < yDataValues.length; i++){
        	nfTotal += yDataValues[i];
        }	
        
        nfMult = (nfTotal/100) ;
        
        for (int i = 0; i < yDataValues.length; i++){
        	yDataValues[i] /= nfMult;
        	yVals1.add(new Entry(yDataValues[i], i));
        }
	}
	
	private static void AdicionarTextos(String[] xDataStrings)
	{       
		xVals = new ArrayList<String>();
        
        for (int i = 0; i < xDataStrings.length; i++)
          xVals.add(xDataStrings[i]);
	}
}
