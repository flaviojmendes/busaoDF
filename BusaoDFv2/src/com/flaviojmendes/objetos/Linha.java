package com.flaviojmendes.objetos;

import java.io.Serializable;

public class Linha implements Serializable{
	
	private static final long serialVersionUID = 5961430236469626757L;
	private String numLinha;
	private String txtLinha;
	
	public String getNumLinha() {
		return numLinha;
	}
	public void setNumLinha(String numLinha) {
		this.numLinha = numLinha;
	}
	public String getTxtLinha() {
		return txtLinha;
	}
	public void setTxtLinha(String txtLinha) {
		this.txtLinha = txtLinha;
	}
}
