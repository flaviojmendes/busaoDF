package com.flaviojmendes.webservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.flaviojmendes.objetos.Comentario;

public class ClienteWebservice {
	private static final String NAMESPACE = "http://comentarios";
//	private static String URL="http://10.0.2.2:8080/busaodf-webservices/services/ComentarioServico?wsdl";
	private static String URL="http://Default-Environment-eqivjuawfm.elasticbeanstalk.com/services/ComentarioServico?wsdl"; 
	private static final String METHOD_OBTER_COMENTARIOS_NAME = "obterComentarios";
	private static final String METHOD_INCLUIR_COMENTARIO_NAME = "incluirComentario";
	private static final String SOAP_ACTION =  "http://comentarios/obterComentarios";
	
	public static String COMENTARIO_SUCESSO 	= "SUC";
	public static String COMENTARIO_OFENSIVO 	= "OFE";
	public static String COMENTARIO_INVALIDO 	= "INV";
	public static String COMENTARIO_ERRO	 	= "ERR";
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Comentario> obterComentarios(String linha) throws IOException, XmlPullParserException {
		ArrayList<Comentario> listaComentarios = new ArrayList<Comentario>();
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_OBTER_COMENTARIOS_NAME);
		request.addProperty("linha", linha);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		androidHttpTransport.call(SOAP_ACTION, envelope);
		Object result = envelope.getResponse();
		if(result instanceof SoapObject) {
			Comentario comentario = new Comentario();
			comentario.setDataCadastro(new Date(Long.parseLong(((SoapObject)result).getProperty(0).toString())));
			comentario.setNomeUsuario(((SoapObject)result).getPropertyAsString(1));
			comentario.setDescComentario(((SoapObject)result).getProperty(2).toString());
			listaComentarios.add(comentario);
		} else {
			Vector<SoapObject> results = (Vector<SoapObject>) result;
			for(SoapObject resultComentario : results) {
				Comentario comentario = new Comentario();
				comentario.setDataCadastro(new Date(Long.parseLong(resultComentario.getProperty(0).toString())));
				comentario.setNomeUsuario(resultComentario.getPropertyAsString(1));
				comentario.setDescComentario(resultComentario.getProperty(2).toString());
				listaComentarios.add(comentario);
			}
		}
		
		return listaComentarios;
	}

	public String comentar(String nomeUsuario, String comentario, String linha)  {
		SoapObject request = new SoapObject(NAMESPACE, METHOD_INCLUIR_COMENTARIO_NAME);
		
		request.addProperty("comentario", comentario);
		request.addProperty("linha", linha);
		request.addProperty("nomeUsuario", nomeUsuario);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		envelope.setOutputSoapObject(request);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
			SoapPrimitive result =  (SoapPrimitive) envelope.getResponse();
			
			return result.toString();
		} catch (IOException e1) {
			return null;
		} catch (XmlPullParserException e1) {
			return null;
		}
	}
	
}
