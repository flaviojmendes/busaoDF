package com.flaviojmendes.interpreter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.flaviojmendes.objetos.Linha;

public class LinhasInterpreter {
	public ArrayList<Linha> pesquisaLinhaHTML(String linha, String itinerario) {
		try {
			// Construct data
			String data = URLEncoder.encode("linha", "UTF-8") + "=" + URLEncoder.encode(linha, "UTF-8");
			data += "&" + URLEncoder.encode("itinerario", "UTF-8") + "=" + URLEncoder.encode(itinerario, "UTF-8");
			// Send data
			URL url = new URL("http://www.horarios.dftrans.df.gov.br/resultado.php");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String linhaHtml;
			ArrayList<Linha> listaLinhas = new ArrayList<Linha>();
			while ((linhaHtml = rd.readLine()) != null) {
				if(linhaHtml.trim().startsWith("<td>")) {
					// Instancia nova linha
					Linha novaLinha = new Linha();
					// Seta número da linha
					novaLinha.setNumLinha(linhaHtml.trim().replace("<td>", "")
															.replace("</td>", ""));
					// Lê a descrição da linha
					linhaHtml = rd.readLine();
					// Seta a descrição da linha
					novaLinha.setTxtLinha(linhaHtml.trim().replace("<td id=\"descricao\">", "")
															.replace("</td>", "")
															.replace("  "," "));
					// Lê e pula as linhas do itinerário
					linhaHtml = rd.readLine();
					linhaHtml = rd.readLine();
					
					listaLinhas.add(novaLinha);
				}
			}
				
			wr.close();
			rd.close();
			

			return listaLinhas;
		} catch (Exception e) {
			return null;
		}
	}
	
}