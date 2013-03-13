package com.flaviojmendes.busaodf;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.flaviojmendes.interpreter.LinhasInterpreter;
import com.flaviojmendes.objetos.Comentario;
import com.flaviojmendes.objetos.Linha;
import com.flaviojmendes.objetos.LinhaDetalhe;
import com.flaviojmendes.webservice.ClienteWebservice;

public class ResultadoHorarios extends SherlockActivity {
	
	private String linha;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.resultado_linhas);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		
		@SuppressWarnings("unchecked")
		ArrayList<LinhaDetalhe> linhasDetalhe = (ArrayList<LinhaDetalhe>) getIntent().getSerializableExtra("linhasDetalhe");
		setContentView(R.layout.resultado_linhas);
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.view_resultado_linhas);
		for(LinhaDetalhe linhaDetalhe : linhasDetalhe) {

			// Instancia e formata textoLinha
			TextView textoLinhaDesc = new TextView(this);
			textoLinhaDesc.setText(linhaDetalhe.getTxtDetalhe());
			textoLinhaDesc.setGravity(Gravity.CENTER_HORIZONTAL);
			textoLinhaDesc.setTypeface(null, Typeface.BOLD);
			textoLinhaDesc.setBackgroundColor(Color.parseColor("#EEF0EF"));
			textoLinhaDesc.setTextSize(16);
			textoLinhaDesc.setTextColor(Color.parseColor("#DC211A"));
			textoLinhaDesc.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT	));
			
			TextView textoLinhaDias = new TextView(this);
			textoLinhaDias.setText(linhaDetalhe.getDiasSemana());
			textoLinhaDias.setGravity(Gravity.CENTER_HORIZONTAL);
			textoLinhaDias.setTypeface(null, Typeface.BOLD);
			textoLinhaDias.setTextColor(Color.parseColor("#4D4D4D"));
			textoLinhaDias.setBackgroundColor(Color.parseColor("#EEF0EF"));
			textoLinhaDias.setTextSize(15);
			textoLinhaDias.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT	));
			
			
			TextView textoLinha = new TextView(this);
			
			String texto = "";
			for(String horario : linhaDetalhe.getHorarios()) {
				texto += horario.replace("  ", " ").replace("  ", " ");
			}
			texto = texto.replaceFirst(" ", "").replace(" ", "   ");
			textoLinha.setText(texto);
			textoLinha.setTextSize(13);
			textoLinha.setGravity(Gravity.LEFT);
			textoLinha.setBackgroundColor(Color.parseColor("#EEF0EF"));
			textoLinha.setTextColor(Color.parseColor("#4D4D4D"));
			textoLinha.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT	));
			textoLinha.setPadding(10, 10, 10, 10);
			
			// Adiciona textoLinha
			layout.addView(textoLinhaDesc);
			layout.addView(textoLinhaDias);
			layout.addView(textoLinha);
			// Adiciona separador
			View ruler = new View(this); 
			ruler.setBackgroundColor(Color.BLACK);
			layout.addView(ruler, new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 1));
		}
		ClienteWebservice clienteWebservice = new ClienteWebservice();
		try {
			this.linha = (String) getIntent().getSerializableExtra("descLinha");
			List<Comentario> comentarios = clienteWebservice.obterComentarios(this.linha);
			
			TextView textoTitComentarios = new TextView(this);
			textoTitComentarios.setText("Coment‡rios");
			textoTitComentarios.setGravity(Gravity.CENTER_HORIZONTAL);
			textoTitComentarios.setTypeface(null, Typeface.BOLD);
			textoTitComentarios.setBackgroundColor(Color.parseColor("#EEF0EF"));
			textoTitComentarios.setTextSize(16);
			textoTitComentarios.setTextColor(Color.parseColor("#DC211A"));
			textoTitComentarios.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			layout.addView(textoTitComentarios);
			
			
			SimpleDateFormat simpleDF = new SimpleDateFormat("dd/MM/yyyy 'ˆs' HH:mm:ss");
			for(Comentario comentario : comentarios) {
				// Adiciona a data do coment‡rio
				TextView textoData = new TextView(this);
				textoData.setText(simpleDF.format(comentario.getDataCadastro()) + " - " + comentario.getNomeUsuario());
				textoData.setGravity(Gravity.LEFT);
				textoData.setTypeface(null, Typeface.BOLD);
				textoData.setTextColor(Color.parseColor("#4D4D4D"));
				textoData.setBackgroundColor(Color.parseColor("#EEF0EF"));
				textoData.setTextSize(15);
				textoData.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT	));
				layout.addView(textoData);
				
				// Adiciona o texto do coment‡rio
				TextView textoComentario = new TextView(this);
				textoComentario.setText(comentario.getDescComentario());
				textoComentario.setTextSize(13);
				textoComentario.setGravity(Gravity.LEFT);
				textoComentario.setBackgroundColor(Color.parseColor("#EEF0EF"));
				textoComentario.setTextColor(Color.parseColor("#4D4D4D"));
				textoComentario.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT	));
//				textoComentario.setPadding(10, 10, 10, 10);
				layout.addView(textoComentario);

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		setSupportProgressBarIndeterminateVisibility(false);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add("Comentar")
    		.setIcon(R.drawable.ic_compose_inverse)
    		.setEnabled(true).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        
        return true;
    }

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		return;
	}
	
	@Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
		Intent mainIntent = new Intent(ResultadoHorarios.this,com.flaviojmendes.busaodf.Comentario.class);
		mainIntent.putExtra("linha", this.linha);
		mainIntent.putExtra("linhaPesq", (String) getIntent().getSerializableExtra("linhaPesq"));
		mainIntent.putExtra("itinPesq", (String) getIntent().getSerializableExtra("itinPesq"));
		ResultadoHorarios.this.startActivity(mainIntent); 
        return true;
    }

	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
		 	Intent mainIntent = new Intent(ResultadoHorarios.this,com.flaviojmendes.busaodf.Comentario.class);
			mainIntent.putExtra("linha", this.linha);
			mainIntent.putExtra("linhaPesq", (String) getIntent().getSerializableExtra("linhaPesq"));
			mainIntent.putExtra("itinPesq", (String) getIntent().getSerializableExtra("itinPesq"));
			ResultadoHorarios.this.startActivity(mainIntent); 
	        return true;
	    }
	 
	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(event.getAction() == KeyEvent.ACTION_DOWN){
	            switch(keyCode) {
	            case KeyEvent.KEYCODE_BACK:
	            	// Instancia o interpreter e pesquisa linhas
	    			LinhasInterpreter linhasInterpreter = new LinhasInterpreter();
	    			ArrayList<Linha> linhas = linhasInterpreter.pesquisaLinhaHTML((String) getIntent().getSerializableExtra("linhaPesq"), (String) getIntent().getSerializableExtra("itinPesq"));
	    			// Ap—s encontrar as linhas carrega a tela.
	    			Intent mainIntent = new Intent(ResultadoHorarios.this,ResultadoLinhas.class);
	    			mainIntent.putExtra("linhas", linhas);
	    			mainIntent.putExtra("linhaPesq", (String) getIntent().getSerializableExtra("linhaPesq"));
	    			mainIntent.putExtra("itinPesq", (String) getIntent().getSerializableExtra("itinPesq"));
	    			ResultadoHorarios.this.startActivity(mainIntent); 
	            	return true;
	            }

	        }
	        return super.onKeyDown(keyCode, event);
	}

}
