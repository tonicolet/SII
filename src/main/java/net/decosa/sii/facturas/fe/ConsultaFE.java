package net.decosa.sii.facturas.fe;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.decosa.sii.aeat.CabeceraConsultaSii;
import net.decosa.sii.aeat.LRConsultaEmitidasType;
import net.decosa.sii.aeat.LRFiltroEmitidasType;
import net.decosa.sii.aeat.PersonaFisicaJuridicaUnicaESType;
import net.decosa.sii.aeat.RespuestaConsultaLRFacturasEmitidasType;
import net.decosa.sii.ws.WSSIIRequest;


@Component
public class ConsultaFE {

	private static final Logger log = Logger.getLogger(ConsultaFE.class.getName());
	
	@Value("${es.aeat.sii.version}")
	private String siiVersion;
	
	@Value("${empresa.cif}")
	private String cif;
	
	@Value("${empresa.nombre}")
	private String nombreEmpresa;
	
	@Autowired
	private WSSIIRequest wsRequest;
	
	
	
	public void getQuote(boolean simularEnvio, String anyo, String mes) {
		
		log.info("Consultando FACTURAS EMITIDAS DE " + anyo + "/" + mes);
		
		// CABECERA
		PersonaFisicaJuridicaUnicaESType personaFisicaJuridicaUnicaESType = new PersonaFisicaJuridicaUnicaESType();
		personaFisicaJuridicaUnicaESType.setNIF(cif);
		personaFisicaJuridicaUnicaESType.setNombreRazon(nombreEmpresa.toUpperCase());
		
		CabeceraConsultaSii cabeceraConsultaSii = new CabeceraConsultaSii();
		cabeceraConsultaSii.setIDVersionSii(siiVersion);
		cabeceraConsultaSii.setTitular(personaFisicaJuridicaUnicaESType);
		
		// FILTRO
		LRFiltroEmitidasType filtroEmitidas = new LRFiltroEmitidasType();
//		filtroEmitidas.setPeriodoImpositivo(new PeriodoImpositivo(anyo, mes));
		
		// CONJUNTO
		LRConsultaEmitidasType lrConsultaEmitidasType = new LRConsultaEmitidasType();
		lrConsultaEmitidasType.setCabecera(cabeceraConsultaSii);
		lrConsultaEmitidasType.setFiltroConsulta(filtroEmitidas);
		
		try {
			RespuestaConsultaLRFacturasEmitidasType respuestaConsulta = wsRequest.consultaFE(lrConsultaEmitidasType, simularEnvio);
			
			System.out.println(respuestaConsulta.getResultadoConsulta());
			System.out.println(respuestaConsulta.getRegistroRespuestaConsultaLRFacturasEmitidas().size());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
