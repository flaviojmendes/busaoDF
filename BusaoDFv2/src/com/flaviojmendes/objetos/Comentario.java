package com.flaviojmendes.objetos;

import java.util.Date;

public class Comentario {
	private String descComentario;
	private Date dataCadastro;
	private String nomeUsuario;
	
	public String getDescComentario() {
		return descComentario;
	}
	public void setDescComentario(String descComentario) {
		this.descComentario = descComentario;
	}
	public Date getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
}
