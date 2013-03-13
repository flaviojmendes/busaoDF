package com.flaviojmendes.busaodf;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.flaviojmendes.interpreter.LinhasInterpreter;
import com.flaviojmendes.objetos.Linha;

public class MainBusao extends SherlockActivity {

	private AdView adView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		
		// Criar o adView
		adView = new AdView(this, AdSize.BANNER, "a1506733e3c0d09");

		// Pesquisar seu LinearLayout presumindo que ele foi dado
		LinearLayout layout = (LinearLayout)findViewById(R.id.layout_ads);

		// Adicionar o adView a ele
		layout.addView(adView);

		adView.loadAd(new AdRequest());
		
		final LinearLayout botaoPesquisaCompleto = (LinearLayout) findViewById(R.id.botao_completo);
		final Button botaoPesquisa = (Button) findViewById(R.id.botao_pesquisa);
		botaoPesquisaCompleto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				 pesquisaLinhas();
			}
		});
		botaoPesquisa.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pesquisaLinhas();
			}
		});
		
		botaoPesquisaCompleto.setOnTouchListener(new View.OnTouchListener() {
		    public boolean onTouch(View v, MotionEvent event) {
		        switch(event.getAction()) {
		        case MotionEvent.ACTION_DOWN:
		        	botaoPesquisaCompleto.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_botoes_pressed));
		        	botaoPesquisa.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0_border_pressed));
		            break;
		        case MotionEvent.ACTION_UP:
		        	botaoPesquisaCompleto.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_botoes));
		        	botaoPesquisa.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0_border));
		        	break;
		        }
		        return false;
		    }
		});

		botaoPesquisa.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					botaoPesquisaCompleto.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_botoes_pressed));
					botaoPesquisa.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0_border_pressed));
					break;
				case MotionEvent.ACTION_UP:
					botaoPesquisaCompleto.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_botoes));
					botaoPesquisa.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0_border));
					break;
				}
				return false;
			}
		});
		setSupportProgressBarIndeterminateVisibility(false);
		
		EditText inputItinerario = (EditText) findViewById(R.id.input_origem_destino);
		inputItinerario.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		        	
		        	pesquisaLinhas();
		        	
		        	return true;
		        }
		        return false;
		    }
		});
		
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Busao")
            .setIcon(R.drawable.ic_busao_bar)
            .setEnabled(false).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }
    
    @SuppressWarnings("deprecation")
	private void pesquisaLinhas() {
    	setSupportProgressBarIndeterminateVisibility(true);
    	EditText inputLinha = (EditText) findViewById(R.id.input_linha);
		String linhaPesquisa = inputLinha.getText().toString();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		EditText inputItinerario = (EditText) findViewById(R.id.input_origem_destino);
		String itinerarioPesquisa = inputItinerario.getText().toString();
		// caso n‹o haja texto no campo alerta usu‡rio.
		if("".equals(linhaPesquisa) && "".equals(itinerarioPesquisa)) {
			AlertDialog alertDialog = new AlertDialog.Builder(MainBusao.this).create();
			alertDialog.setTitle("Dados Inv‡lidos");
			alertDialog.setMessage("Informe a linha ou itiner‡rio desejados!");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			setSupportProgressBarIndeterminateVisibility(false);
			alertDialog.show();
		} else {
			LinearLayout botaoPesquisaCompleto = (LinearLayout) findViewById(R.id.botao_completo);
			Button botaoPesquisa = (Button) findViewById(R.id.botao_pesquisa);
			
			botaoPesquisaCompleto.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_botoes_disabled));
        	botaoPesquisa.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0_border_disabled));
			botaoPesquisaCompleto.setEnabled(false);
			botaoPesquisa.setEnabled(false);
			
			// Instancia o interpreter e pesquisa linhas
			LinhasInterpreter linhasInterpreter = new LinhasInterpreter();
			ArrayList<Linha> linhas = linhasInterpreter.pesquisaLinhaHTML(linhaPesquisa, itinerarioPesquisa);
			// Ap—s encontrar as linhas carrega a tela.
			Toast.makeText(MainBusao.this, linhas.size() + " linhas encontradas!", Toast.LENGTH_LONG).show();
			Intent mainIntent = new Intent(MainBusao.this,ResultadoLinhas.class);
			mainIntent.putExtra("linhas", linhas);
			mainIntent.putExtra("linhaPesq", linhaPesquisa);
			mainIntent.putExtra("itinPesq", itinerarioPesquisa);
			MainBusao.this.startActivity(mainIntent); 
			 
		}
	}
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if(adView != null) {
    		adView.loadAd(new AdRequest());
    	}
    	setSupportProgressBarIndeterminateVisibility(false);
    	LinearLayout botaoPesquisaCompleto = (LinearLayout) findViewById(R.id.botao_completo);
		Button botaoPesquisa = (Button) findViewById(R.id.botao_pesquisa);
		botaoPesquisaCompleto.setEnabled(true);
		botaoPesquisa.setEnabled(true);
    	botaoPesquisaCompleto.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_botoes));
		botaoPesquisa.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0_border));
    }
    
    @Override
	  public void onDestroy() {
//	    adView.destroy();
	    super.onDestroy();
	  }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(event.getAction() == KeyEvent.ACTION_DOWN){
	            switch(keyCode) {
	            case KeyEvent.KEYCODE_BACK:
	    			finish(); 
	            	return true;
	            }

	        }
	        return super.onKeyDown(keyCode, event);
	}
}
