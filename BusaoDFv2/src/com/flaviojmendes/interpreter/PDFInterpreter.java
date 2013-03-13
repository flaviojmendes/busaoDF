package com.flaviojmendes.interpreter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.flaviojmendes.objetos.LinhaDetalhe;

public class PDFInterpreter {

	@SuppressWarnings("unchecked")
	public ArrayList<LinhaDetalhe> getHorariosLinhasFromDFTrans(InputStream linhaString, boolean isHorario) throws IOException {

		BufferedReader in = new BufferedReader(new InputStreamReader(linhaString));  
		in.mark(100);
		// Listas auxiliares
		ArrayList<String> listTxtDetalhe = new ArrayList<String>();
		ArrayList<String> listHorarios = new ArrayList<String>();
		ArrayList<String> listDias = new ArrayList<String>();
		String linha;  
		while((linha = in.readLine())!=null) {
			if(linha.trim().toLowerCase().startsWith("saindo")) {
				if(linha.trim().toLowerCase().split("saindo").length > 2) {
					listTxtDetalhe.add("Saindo" + linha.trim().toLowerCase().split("saindo")[1]);
					listTxtDetalhe.add("Saindo" + linha.trim().toLowerCase().split("saindo")[2]);
				} else {
					listTxtDetalhe.add(linha);
				}
			} else if (linha.contains(":")) {
				listHorarios.add(linha);
			} else if(linha.trim().toLowerCase().startsWith("dom") ||
					linha.trim().toLowerCase().startsWith("seg") || 
					linha.trim().toLowerCase().startsWith("ter") || 
					linha.trim().toLowerCase().startsWith("qua") || 
					linha.trim().toLowerCase().startsWith("qui") || 
					linha.trim().toLowerCase().startsWith("sex") || 
					linha.trim().toLowerCase().startsWith("s‡b") ||
					linha.trim().toLowerCase().startsWith("sab")) {
				listDias.add(linha);
			}
		}

		ArrayList<String> listHorariosClone = (ArrayList<String>) listHorarios.clone();

		ArrayList<LinhaDetalhe> linhasDetalhe = new ArrayList<LinhaDetalhe>();
		int qtDetalhes = 0;
		for(String txtDetalhe : listTxtDetalhe) {
			LinhaDetalhe linhaDetalhe = new LinhaDetalhe();
			linhaDetalhe.setTxtDetalhe(txtDetalhe);
			linhaDetalhe.setDiasSemana(listDias.get(qtDetalhes));

			for(int i = 0; i < listHorarios.size() ; i++) {
				if(i != listHorarios.size()-1) {
					if(listHorarios.get(i).compareTo(listHorarios.get(i+1)) < 0) {
						linhaDetalhe.getHorarios().add(listHorarios.get(i));
						listHorariosClone.remove(0);
					} else {
						linhaDetalhe.getHorarios().add(listHorarios.get(i));
						listHorariosClone.remove(0);
						listHorarios = (ArrayList<String>) listHorariosClone.clone();
						linhasDetalhe.add(linhaDetalhe);
						break;
					}
				} else {
					linhaDetalhe.getHorarios().add(listHorarios.get(i));
					listHorariosClone.remove(0);
					listHorarios = (ArrayList<String>) listHorariosClone.clone();
					linhasDetalhe.add(linhaDetalhe);
				}
			}
			qtDetalhes++;
		}
		// onibus bizarros
		if(qtDetalhes == 0) {
			// Listas auxiliares
			listTxtDetalhe = new ArrayList<String>();
			listHorarios = new ArrayList<String>();
			listDias = new ArrayList<String>();
			in.reset();
			int contador = 1;
			while((linha = in.readLine())!=null) {
				if(linha.trim().toLowerCase().startsWith("dom") ||
						linha.trim().toLowerCase().startsWith("seg") || 
						linha.trim().toLowerCase().startsWith("ter") || 
						linha.trim().toLowerCase().startsWith("qua") || 
						linha.trim().toLowerCase().startsWith("qui") || 
						linha.trim().toLowerCase().startsWith("sex") || 
						linha.trim().toLowerCase().startsWith("s‡b") ||
						linha.trim().toLowerCase().startsWith("sab")) {
					listDias.add(linha);
					if(contador % 2 == 1) {
						listTxtDetalhe.add("IDA");
					} else {
						listTxtDetalhe.add("VOLTA");
					}
					contador++;
				} else if (linha.contains(":") && 
						!linha.trim().endsWith(":") &&
						linha.trim().compareTo("99:9999:9999:9999:9999:9999:9999:9999:9999:9999:9999:99") < 0 && 
						linha.length() > 7)  {
					listHorarios.add(linha);
				}
			}

			qtDetalhes = 0;
			listHorariosClone = (ArrayList<String>) listHorarios.clone();
			for(String txtDetalhe : listTxtDetalhe) {
				LinhaDetalhe linhaDetalhe = new LinhaDetalhe();
				linhaDetalhe.setTxtDetalhe(txtDetalhe);
				linhaDetalhe.setDiasSemana(listDias.get(qtDetalhes));

				for(int i = 0; i < listHorarios.size() ; i++) {
					if(i != listHorarios.size()-1) {
						if(listHorarios.get(i).compareTo(listHorarios.get(i+1)) < 0) {
							linhaDetalhe.getHorarios().add(listHorarios.get(i));
							listHorariosClone.remove(0);
						} else {
							linhaDetalhe.getHorarios().add(listHorarios.get(i));
							listHorariosClone.remove(0);
							listHorarios = (ArrayList<String>) listHorariosClone.clone();
							linhasDetalhe.add(linhaDetalhe);
							break;
						}
					} else {
						linhaDetalhe.getHorarios().add(listHorarios.get(i));
						listHorariosClone.remove(0);
						listHorarios = (ArrayList<String>) listHorariosClone.clone();
						linhasDetalhe.add(linhaDetalhe);
					}
				}
				qtDetalhes++;
			}
		}
		// onibus mais bizarros ainda sem padr‹o
		if(linhasDetalhe.size() < 1) {
			in.reset();
			String linhaGambi = "";  
			while((linha = in.readLine())!=null) {
				linhaGambi += linha + "\n";
			}
			LinhaDetalhe linhaDetGambi = new LinhaDetalhe();
			linhaDetGambi.getHorarios().add(linhaGambi);
		}
		return linhasDetalhe;
	}
}
