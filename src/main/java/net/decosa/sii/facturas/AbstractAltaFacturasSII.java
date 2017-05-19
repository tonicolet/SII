package net.decosa.sii.facturas;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import net.decosa.sii.SiiConfig;
import net.decosa.sii.aeat.CabeceraSii;
import net.decosa.sii.aeat.ClaveTipoComunicacionType;
import net.decosa.sii.aeat.DatosPresentacionType;
import net.decosa.sii.aeat.EstadoEnvioType;
import net.decosa.sii.aeat.EstadoRegistroType;
import net.decosa.sii.aeat.PersonaFisicaJuridicaESType;
import net.decosa.sii.ed.Factura;
import net.decosa.sii.util.DateUtils;


public abstract class AbstractAltaFacturasSII {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	protected List<? extends Factura> facturas;

	@Value("${es.aeat.sii.version}")
	private String siiVersion;
	
	@Value("${empresa.cif}")
	private String cif;
	
	@Value("${empresa.nombre}")
	private String nombreEmpresa;
	
	
	
	protected CabeceraSii getCabeceraAltaSII() {
		PersonaFisicaJuridicaESType personaFisicaJuridicaESType = new PersonaFisicaJuridicaESType();
		personaFisicaJuridicaESType.setNIF(cif);
		personaFisicaJuridicaESType.setNombreRazon(nombreEmpresa.toUpperCase());
		
		CabeceraSii cabeceraSii = new CabeceraSii();
		cabeceraSii.setIDVersionSii(siiVersion);
		cabeceraSii.setTipoComunicacion(ClaveTipoComunicacionType.A_0);
		cabeceraSii.setTitular(personaFisicaJuridicaESType);
		
		return cabeceraSii;
	}
	
	
	public void setFacturas(List<? extends Factura> facturas) throws Exception {
		// Precondiciones para la lista de facturas
		
		log.info("Precondiciones del listado de facturas...");
		if ((facturas == null) || (facturas.size() == 0))
			throw new Exception("Sin facturas para enviar");
		
		// Verificar límite de cantidad
		if (facturas.size() > SiiConfig.MAX_FACTURAS_POR_ENVIO)
			throw new Exception("Sólo se permiten " + SiiConfig.MAX_FACTURAS_POR_ENVIO + " facturas por envío");
		
		
		this.facturas = facturas;
		log.info("... (" + facturas.size() + ") Ok");
		
		log.info("Iniciando la creación de ED...");
		procesarDatos();
	}
	
	
	protected abstract void procesarDatos() throws Exception;
	public abstract String getXML() throws Exception;
	public abstract String getXML(boolean pretty) throws Exception;
	public abstract RespuestaAlta send(boolean simularEnvio) throws Exception;
	
	
	public void send() throws Exception {
		send(false);
	}
	

	protected String getXML(Object o) throws JAXBException {
		return getXML(o, true);
	}
	
	
	protected String getXML(Object o, boolean pretty) throws JAXBException {
		if (o == null) return null;
		
		Marshaller m = crearMarshaller(o, pretty);
		
		StringWriter sw = new StringWriter();
		m.marshal(o, sw);
		
		return sw.toString();
	}
	
	
	private Marshaller crearMarshaller(Object o, boolean pretty) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(o.getClass());
		Marshaller m = context.createMarshaller();
		
		if (pretty)
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		return m;
	}
	
//	
//	/**
//	 * Procesar respuesta de OPERACIONES INTRACOMUNITARIAS
//	 * 
//	 * @param respuestaOI
//	 */
//	protected void procesarRespuesta(RespuestaLROComunitariasType respuestaOI, boolean simularEnvio) {
//		if (simularEnvio) return;
//		
//		// Procesar cabecera
//		procesarRespuestaCabecera(respuestaOI.getEstadoEnvio(), respuestaOI.getDatosPresentacion(), respuestaOI.getCSV());
//		
//		// Procesar detalles
//		for(RespuestaComunitariaType respuestaLinea: respuestaOI.getRespuestaLinea())
//			procesarRespuestaDetalles(respuestaLinea.getIDFactura().getNumSerieFacturaEmisor(), 
//					respuestaLinea.getEstadoRegistro(), respuestaLinea.getDescripcionErrorRegistro(),
//					respuestaLinea.getCodigoErrorRegistro());
//	}
	
	
	
	protected RespuestaAlta procesarRespuestaCabecera(EstadoEnvioType estadoEnvio, 
			DatosPresentacionType datosPresentacion, String csv) {
		
		log.info("Generando objeto de respuesta...");
		RespuestaAlta respuestaAlta = new RespuestaAlta();
		
		respuestaAlta.setEstadoEnvio(estadoEnvio.value());
		log.debug("  Resultado del envío: " + respuestaAlta.getEstadoEnvio());
		
		// Si el resultado es CORRECTO o PARCIALMENTECORRECTO
		if ((estadoEnvio == EstadoEnvioType.CORRECTO)||(estadoEnvio == EstadoEnvioType.PARCIALMENTE_CORRECTO)) {
			respuestaAlta.setFechaPresentacion(DateUtils.parse(datosPresentacion.getTimestampPresentacion(), SiiConfig.DEFAULT_DATE_FORMAT + " HH:mm:ss"));
			respuestaAlta.setCsv(csv);
		}
		
		return respuestaAlta;
	}
	
	
	
	protected RespuestaAlta procesarRespuestaDetalles(RespuestaAlta respuestaAlta, String numSerieFactura, 
			EstadoRegistroType estadoRegistro, String csv, String descripcionError, BigInteger codigoError) {
		
		RespuestaAltaDetalle respuestaAltaDetalle = new RespuestaAltaDetalle();
		respuestaAltaDetalle.setEstadoRegistro(estadoRegistro.value());
		respuestaAltaDetalle.setNumSerieFactura(numSerieFactura);
		respuestaAltaDetalle.setCsv(csv);

		if (estadoRegistro == EstadoRegistroType.INCORRECTO) {
			respuestaAltaDetalle.setDescripcionError(descripcionError);
			respuestaAltaDetalle.setCodigoError(codigoError.intValue());
		}
		
		respuestaAlta.addRespuestaAltaDetalle(respuestaAltaDetalle);
		return respuestaAlta;
	}
}
