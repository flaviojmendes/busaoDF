package com.flaviojmendes.busaodf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.flaviojmendes.interpreter.LinhasInterpreter;
import com.flaviojmendes.interpreter.PDFInterpreter;
import com.flaviojmendes.objetos.Linha;
import com.flaviojmendes.objetos.LinhaDetalhe;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class ResultadoLinhas extends SherlockActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.resultado_linhas);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		
		@SuppressWarnings("unchecked")
		final ArrayList<Linha> linhas = (ArrayList<Linha>) getIntent().getSerializableExtra("linhas");
		
		setContentView(R.layout.resultado_linhas);
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.view_resultado_linhas);
		for(Linha linha : linhas) {
			LinearLayout linhaLayout = new LinearLayout(this);
			linhaLayout.setOrientation(LinearLayout.HORIZONTAL);
			linhaLayout.setBackgroundColor(Color.parseColor("#EEF0EF"));
			linhaLayout.setGravity(Gravity.CENTER_VERTICAL);
			linhaLayout.setPadding(10, 10, 10, 10);
			
			ImageView iconeSeta = new ImageView(this);
			iconeSeta.setImageResource(R.drawable.ic_seta_direita);
			
			// Instancia e formata textoLinha
			LinearLayout textoLayout = new LinearLayout(this);
			textoLayout.setOrientation(LinearLayout.VERTICAL);
			textoLayout.setPadding(10, 10, 10, 10);
			TextView textoLinha = new TextView(this);
			textoLinha.setText(linha.getNumLinha());
			textoLinha.setTextColor(Color.parseColor("#DC211A"));
			textoLinha.setClickable(true);
			textoLinha.setTextSize(16);
			textoLinha.setGravity(Gravity.LEFT);
			textoLinha.setTypeface(null, Typeface.BOLD);
			textoLinha.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT	));
			TextView textoLinhaDetalhe = new TextView(this);
			textoLinhaDetalhe.setText(linha.getTxtLinha());
			textoLinhaDetalhe.setClickable(true);
			textoLinhaDetalhe.setTextColor(Color.parseColor("#4F4F4F"));
			textoLinhaDetalhe.setTextSize(11);
			textoLinhaDetalhe.setGravity(Gravity.LEFT);
			textoLinhaDetalhe.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT	));

			// Adiciona textoLinha
			linhaLayout.addView(iconeSeta);
			textoLayout.addView(textoLinha);
			textoLayout.addView(textoLinhaDetalhe);
			linhaLayout.addView(textoLayout);
			layout.addView(linhaLayout);
			linhaLayout.bringToFront();
			textoLayout.bringToFront();
			
			// Adiciona Ação ao textoLinha
			linhaLayout.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String linhaPesquisa = ((TextView)((LinearLayout)((LinearLayout)v).getChildAt(1)).getChildAt(0)).getText().toString().split("\n")[0];
					pesquisaHorarios(linhaPesquisa);
				}
			});
			textoLayout.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String linhaPesquisa = ((TextView)((LinearLayout)v).getChildAt(0)).getText().toString().split("\n")[0];
					pesquisaHorarios(linhaPesquisa);
				}
			});
			
			textoLinha.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String linhaPesquisa = ((TextView)v).getText().toString().split("\n")[0];
					pesquisaHorarios(linhaPesquisa);
				}
			});
			textoLinhaDetalhe.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String linhaPesquisa = ((TextView)((LinearLayout)((TextView)v).getParent()).getChildAt(0)).getText().toString().split("\n")[0];
					pesquisaHorarios(linhaPesquisa);
				}
			});
			
			// Adiciona separador
			View ruler = new View(this); 
			ruler.setBackgroundColor(Color.BLACK);
			layout.addView(ruler, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 1));
		}
		setSupportProgressBarIndeterminateVisibility(false);
	}
	
	@SuppressWarnings("deprecation")
	public void pesquisaHorarios(String linhaPesq) {
		setSupportProgressBarIndeterminateVisibility(true);
		// Instancia o interpreter e pesquisa os detalhes das linhas
		PDFInterpreter pdfInterpreter = new PDFInterpreter();
		ArrayList<LinhaDetalhe> linhasDetalhe = new ArrayList<LinhaDetalhe>();
		try {
			InputStream inputStream = getAssets().open("TH"+linhaPesq+".bsdf");

			linhasDetalhe = pdfInterpreter.getHorariosLinhasFromDFTrans(inputStream, true);
		} catch (IOException e) {
			AlertDialog alertDialog = new AlertDialog.Builder(ResultadoLinhas.this).create();
			alertDialog.setTitle("Erro");
			alertDialog.setMessage("Desculpe mas os dados não existem no servidor.");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			alertDialog.show();
		}
		
		// Após encontrar as linhas carrega a tela.
		Intent mainIntent = new Intent(ResultadoLinhas.this,ResultadoHorarios.class);
		mainIntent.putExtra("linhasDetalhe", linhasDetalhe);
		mainIntent.putExtra("descLinha", linhaPesq);
		mainIntent.putExtra("linhaPesq", (String) getIntent().getSerializableExtra("linhaPesq"));
		mainIntent.putExtra("itinPesq", (String) getIntent().getSerializableExtra("itinPesq"));
		ResultadoLinhas.this.startActivity(mainIntent); 
	}
	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Busao")
            .setIcon(R.drawable.ic_busao_bar)
            .setEnabled(false).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }
	
	@Override
    protected void onResume() {
    	super.onResume();
    	setSupportProgressBarIndeterminateVisibility(false);
    }
	
	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 if(event.getAction() == KeyEvent.ACTION_DOWN){
		            switch(keyCode) {
		            case KeyEvent.KEYCODE_BACK:
		    			Intent mainIntent = new Intent(ResultadoLinhas.this,MainBusao.class);
		    			ResultadoLinhas.this.startActivity(mainIntent); 
		            	return true;
		            }

		        }
		        return super.onKeyDown(keyCode, event);
		}
}
