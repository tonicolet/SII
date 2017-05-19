package net.decosa.sii.ws;

import javax.xml.bind.JAXBElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import net.decosa.sii.aeat.BajaLRFacturasRecibidas;
import net.decosa.sii.aeat.LRConsultaEmitidasType;
import net.decosa.sii.aeat.ObjectFactory;
import net.decosa.sii.aeat.RespuestaConsultaLRFacturasEmitidasType;
import net.decosa.sii.aeat.RespuestaLRBajaFRecibidasType;
import net.decosa.sii.aeat.RespuestaLRFEmitidasType;
import net.decosa.sii.aeat.RespuestaLRFRecibidasType;
import net.decosa.sii.aeat.RespuestaLROComunitariasType;
import net.decosa.sii.aeat.SuministroLRDetOperacionIntracomunitaria;
import net.decosa.sii.aeat.SuministroLRFacturasEmitidas;
import net.decosa.sii.aeat.SuministroLRFacturasRecibidas;


public class WSSIIRequest extends WebServiceGatewaySupport {
	
	private static final Log log = LogFactory.getLog(WSSIIRequest.class);
	
	private static final String ACCION_ALTA		= "ALTA";
	private static final String ACCION_BAJA		= "BAJA";
	private static final String ACCION_CONSULTA	= "CONSULTA";
	
	@Value("${es.aeat.sii.uri}")
	private String siiURI;
	
	@Value("${es.aeat.sii.version}")
	private String siiVersion;
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	//
	// FACTURAS EMITIDAS
	//
	
	@SuppressWarnings("unchecked")
	public RespuestaConsultaLRFacturasEmitidasType consultaFE(LRConsultaEmitidasType lrConsultaEmitidasType, boolean simularEnvio) 
			throws Exception {
		
		ObjectFactory of = new ObjectFactory();
		
		JAXBElement<RespuestaConsultaLRFacturasEmitidasType> response = (JAXBElement<RespuestaConsultaLRFacturasEmitidasType>)
				sendAndReceiveFE(of.createConsultaLRFacturasEmitidas(lrConsultaEmitidasType), simularEnvio, ACCION_CONSULTA);
		
		return response.getValue();
	}

	
	@SuppressWarnings("unchecked")
	public RespuestaLRFEmitidasType altaFE(SuministroLRFacturasEmitidas suministroLRFacturasEmitidas, boolean simularEnvio)
			throws Exception {
		
		JAXBElement<RespuestaLRFEmitidasType> response = (JAXBElement<RespuestaLRFEmitidasType>)
				sendAndReceiveFE(suministroLRFacturasEmitidas, simularEnvio, ACCION_ALTA);
		
		if (simularEnvio) return null;
		return response.getValue();
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	//
	// FACTURAS RECIBIDAS
	//
	
	@SuppressWarnings("unchecked")
	public RespuestaLRFRecibidasType altaFR(SuministroLRFacturasRecibidas suministroLRFacturasRecibidas, boolean simularEnvio)
			throws Exception {
		
		JAXBElement<RespuestaLRFRecibidasType> response = (JAXBElement<RespuestaLRFRecibidasType>)
			sendAndReceiveFR(suministroLRFacturasRecibidas, simularEnvio, ACCION_ALTA);
	
		if (simularEnvio) return null;
		return response.getValue();
	}
	
	
	@SuppressWarnings("unchecked")
	public RespuestaLRBajaFRecibidasType bajaFR(BajaLRFacturasRecibidas bajaFacturasRecibidas, boolean simularEnvio)
			throws Exception {
		
		JAXBElement<RespuestaLRBajaFRecibidasType> response = (JAXBElement<RespuestaLRBajaFRecibidasType>)
			sendAndReceiveFR(bajaFacturasRecibidas, simularEnvio, ACCION_BAJA);
	
		if (simularEnvio) return null;
		return response.getValue();
	}
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	//
	// OPERACIONES INTRACOMUNITARIAS
	//
	
	@SuppressWarnings("unchecked")
	public RespuestaLROComunitariasType altaOI(SuministroLRDetOperacionIntracomunitaria suministroOIRecibidas, boolean simularEnvio)
			throws Exception {
		
//		// Añadimos la cabecera
//		PersonaFisicaJuridicaESType personaFisicaJuridicaESType = new PersonaFisicaJuridicaESType();
//		personaFisicaJuridicaESType.setNIF(cif);
//		personaFisicaJuridicaESType.setNombreRazon(nombreEmpresaDeco.toUpperCase());
//		
//		CabeceraSii cabeceraSii = new CabeceraSii();
//		cabeceraSii.setIDVersionSii(siiVersion);
//		cabeceraSii.setTipoComunicacion(ClaveTipoComunicacionType.A_0);
//		cabeceraSii.setTitular(personaFisicaJuridicaESType);
//		
//		suministroOIRecibidas.setCabecera(cabeceraSii);
		
		JAXBElement<RespuestaLROComunitariasType> response = (JAXBElement<RespuestaLROComunitariasType>)
			sendAndReceiveOI(suministroOIRecibidas, simularEnvio, ACCION_ALTA);
	
		if (simularEnvio) return null;
		return response.getValue();
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////
	//
	// CORE
	//
	
	private Object sendAndReceiveFR(Object sendObject, boolean simularEnvio, String accion) throws Exception {
		return sendAndReceive("fr", sendObject, accion, simularEnvio);
	}
	
	private Object sendAndReceiveFE(Object sendObject, boolean simularEnvio, String accion) throws Exception {
		return sendAndReceive("fe", sendObject, accion, simularEnvio);
	}
	
	private Object sendAndReceiveOI(Object sendObject, boolean simularEnvio, String accion) throws Exception {
		return sendAndReceive("oi", sendObject, accion, simularEnvio);
	}
	
	
	private Object sendAndReceive(String prefijo, Object sendObject, String accion, boolean simularEnvio) throws Exception {
		String uri = siiURI + prefijo + "/SiiFact" + prefijo.toUpperCase() + "V1SOAP";
		
		if (simularEnvio) {
			log.info(">> SIMULANDO " + accion + " [" + uri + "] >>");
			return null;
		}
		
		log.info(">> ENVIANDO " + accion + " [" + uri + "] >>");
		return getWebServiceTemplate().marshalSendAndReceive(uri, sendObject);
	}
	
	
	
//	private CabeceraSiiBaja getCabeceraBajaSII() {
//		PersonaFisicaJuridicaESType personaFisicaJuridicaESType = new PersonaFisicaJuridicaESType();
//		personaFisicaJuridicaESType.setNIF(cif);
//		personaFisicaJuridicaESType.setNombreRazon(nombreEmpresaDeco.toUpperCase());
//		
//		CabeceraSiiBaja cabeceraSii = new CabeceraSiiBaja();
//		cabeceraSii.setIDVersionSii(siiVersion);
//		cabeceraSii.setTitular(personaFisicaJuridicaESType);
//		
//		return cabeceraSii;
//	}

}
