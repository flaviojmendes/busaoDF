package com.flaviojmendes.busaodf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.flaviojmendes.interpreter.PDFInterpreter;
import com.flaviojmendes.objetos.LinhaDetalhe;
import com.flaviojmendes.webservice.ClienteWebservice;

public class Comentario extends SherlockActivity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.comentario);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		
		
		final LinearLayout botaoComentarCompleto = (LinearLayout) findViewById(R.id.botao_comentar_completo);
		final Button botaoComentar = (Button) findViewById(R.id.botao_comentar);
		botaoComentarCompleto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				 comentar();
			}
		});
		botaoComentar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				comentar();
			}
		});
		
		botaoComentarCompleto.setOnTouchListener(new View.OnTouchListener() {
		    public boolean onTouch(View v, MotionEvent event) {
		        switch(event.getAction()) {
		        case MotionEvent.ACTION_DOWN:
		        	botaoComentarCompleto.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_botoes_pressed));
		        	botaoComentar.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0_border_pressed));
		            break;
		        case MotionEvent.ACTION_UP:
		        	botaoComentarCompleto.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_botoes));
		        	botaoComentar.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0_border));
		        	break;
		        }
		        return false;
		    }
		});

		botaoComentar.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					botaoComentarCompleto.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_botoes_pressed));
					botaoComentar.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0_border_pressed));
					break;
				case MotionEvent.ACTION_UP:
					botaoComentarCompleto.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_botoes));
					botaoComentar.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0_border));
					break;
				}
				return false;
			}
		});
		setSupportProgressBarIndeterminateVisibility(false);
		
		EditText inputComentario = (EditText) findViewById(R.id.input_comentario);
		inputComentario.setOnKeyListener(new OnKeyListener() {
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // If the event is a key-down event on the "enter" button
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		            (keyCode == KeyEvent.KEYCODE_ENTER)) {
		        		InputMethodManager imm = (InputMethodManager)getSystemService(
		        		Context.INPUT_METHOD_SERVICE);
		        		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
	private void comentar() {
    	setSupportProgressBarIndeterminateVisibility(true);
    	EditText inputNome = (EditText) findViewById(R.id.input_nome);
		String nomeUsuario = inputNome.getText().toString();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		EditText inputComentario = (EditText) findViewById(R.id.input_comentario);
		String comentario = inputComentario.getText().toString();
		// caso n‹o haja texto no campo alerta usu‡rio.
		if("".equals(nomeUsuario) && "".equals(comentario)) {
			AlertDialog alertDialog = new AlertDialog.Builder(Comentario.this).create();
			alertDialog.setTitle("Dados Inv‡lidos");
			alertDialog.setMessage("Informe o seu nome e o seu coment‡rio!");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			});
			setSupportProgressBarIndeterminateVisibility(false);
			alertDialog.show();
		} else {
			LinearLayout botaoComentarCompleto = (LinearLayout) findViewById(R.id.botao_comentar_completo);
			Button botaoComentar = (Button) findViewById(R.id.botao_comentar);
			
			botaoComentarCompleto.setBackgroundDrawable(getResources().getDrawable(R.drawable.style_botoes_disabled));
			botaoComentar.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_0_border_disabled));
        	botaoComentarCompleto.setEnabled(false);
        	botaoComentar.setEnabled(false);
			
			// Instancia cliente Webservice
        	ClienteWebservice clienteWebservice = new ClienteWebservice();
        	String linha = (String) getIntent().getSerializableExtra("linha");
        	String resultComentario = clienteWebservice.comentar(nomeUsuario, comentario, linha);
        	
			// Ap—s comentar, carrega os hor‡rios.
        	String textoResultComentario = null;
			if(resultComentario.equals(ClienteWebservice.COMENTARIO_SUCESSO)) {
				textoResultComentario = "Coment‡rio realizado com sucesso!";
			} else if(resultComentario.equals(ClienteWebservice.COMENTARIO_OFENSIVO)) {
				textoResultComentario = "O coment‡rio n‹o foi feito, pois h‡ texto ofensivo!";
			} else if(resultComentario.equals(ClienteWebservice.COMENTARIO_INVALIDO)) {
				textoResultComentario = "O coment‡rio n‹o foi feito, pois n‹o parece real!";
			} else {
				textoResultComentario = "Ops, o ™nibus dos coment‡rios passou r‡pido demais. Tente novamente mais tarde.";
			}
			Toast.makeText(Comentario.this, textoResultComentario, Toast.LENGTH_LONG).show();
			pesquisaHorarios(linha);
			 
		}
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
			AlertDialog alertDialog = new AlertDialog.Builder(Comentario.this).create();
			alertDialog.setTitle("Erro");
			alertDialog.setMessage("Desculpe mas os dados n‹o existem no servidor.");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			alertDialog.show();
		}
		
		// Ap—s encontrar as linhas carrega a tela.
		Intent mainIntent = new Intent(Comentario.this,ResultadoHorarios.class);
		mainIntent.putExtra("linhasDetalhe", linhasDetalhe);
		mainIntent.putExtra("linhaPesq", (String) getIntent().getSerializableExtra("linhaPesq"));
		mainIntent.putExtra("itinPesq", (String) getIntent().getSerializableExtra("itinPesq"));
		mainIntent.putExtra("descLinha", linhaPesq);
		Comentario.this.startActivity(mainIntent); 
	}
    
    @Override
	  public void onDestroy() {
	    super.onDestroy();
	  }
}
