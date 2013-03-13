package com.flaviojmendes.objetos;

import java.io.Serializable;
import java.util.ArrayList;

public class LinhaDetalhe implements Serializable{
	
	private static final long serialVersionUID = 5995008955661207602L;
	private String txtDetalhe;
	private String diasSemana;
	private ArrayList<String> horarios = new ArrayList<String>();
	public String getTxtDetalhe() {
		return txtDetalhe;
	}
	public void setTxtDetalhe(String txtDetalhe) {
		this.txtDetalhe = txtDetalhe;
	}
	public String getDiasSemana() {
		return diasSemana;
	}
	public void setDiasSemana(String diasSemana) {
		this.diasSemana = diasSemana;
	}
	public ArrayList<String> getHorarios() {
		return horarios;
	}
	public void setHorarios(ArrayList<String> horarios) {
		this.horarios = horarios;
	}
}
