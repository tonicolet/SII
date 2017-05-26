package net.decosa.sii.facturas;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.decosa.sii.SiiConfig;
import net.decosa.sii.aeat.DatosPresentacionType;
import net.decosa.sii.aeat.EstadoEnvioType;
import net.decosa.sii.aeat.EstadoRegistroType;
import net.decosa.sii.ed.Factura;
import net.decosa.sii.util.DateUtils;
import net.decosa.sii.ws.WSSIIRequest;


@Component
public abstract class AbstractFacturasSII {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	protected List<? extends Factura> facturas;
	
	@Autowired
	protected WSSIIRequest wsSIIRequest;

	@Value("${es.aeat.sii.version}")
	protected String siiVersion;
	
	@Value("${empresa.cif}")
	protected String cif;
	
	@Value("${empresa.nombre}")
	protected String nombreEmpresa;
	
	
	
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
		procesar();
	}
	
	
	protected abstract void procesar() throws Exception;
	public abstract Respuesta enviar(boolean simularEnvio) throws Exception;
	public abstract String getXML();
	public abstract String getXML(boolean pretty);
	
	
	public String getVersionSII() {
		return this.siiVersion;
	}
	
	
	protected String getXML(Object o) {
		return getXML(o, true);
	}
	
	
	protected String getXML(Object o, boolean pretty) {
		if (o == null) return null;
		
		try {
			Marshaller m = crearMarshaller(o, pretty);
			StringWriter sw = new StringWriter();
			m.marshal(o, sw);
		
			return sw.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	private Marshaller crearMarshaller(Object o, boolean pretty) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(o.getClass());
		Marshaller m = context.createMarshaller();
		
		if (pretty)
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		return m;
	}
	
	
	protected Respuesta procesarRespuestaCabecera(EstadoEnvioType estadoEnvio, 
			DatosPresentacionType datosPresentacion, String csv) {
		
		log.info("Generando objeto de respuesta...");
		Respuesta respuesta = new Respuesta();
		
		respuesta.setEstadoEnvio(estadoEnvio.value());
		log.debug("  Resultado del envío: " + respuesta.getEstadoEnvio());
		
		// Si el resultado es CORRECTO o PARCIALMENTECORRECTO
		if ((estadoEnvio == EstadoEnvioType.CORRECTO)||(estadoEnvio == EstadoEnvioType.PARCIALMENTE_CORRECTO)) {
			respuesta.setFechaPresentacion(DateUtils.parse(datosPresentacion.getTimestampPresentacion(), SiiConfig.DEFAULT_DATE_FORMAT + " HH:mm:ss"));
			respuesta.setCsv(csv);
		}
		
		return respuesta;
	}
	
	
	
	protected Respuesta procesarRespuestaDetalles(Respuesta respuesta, String numSerieFactura, 
			EstadoRegistroType estadoRegistro, String descripcionError, BigInteger codigoError) {
		
		RespuestaDetalle respuestaAltaDetalle = new RespuestaDetalle();
		respuestaAltaDetalle.setEstadoRegistro(estadoRegistro.value());
		respuestaAltaDetalle.setNumSerieFactura(numSerieFactura);

		if (estadoRegistro == EstadoRegistroType.INCORRECTO) {
			respuestaAltaDetalle.setDescripcionError(descripcionError);
			respuestaAltaDetalle.setCodigoError(codigoError.intValue());
		}
		
		respuesta.addRespuestaDetalle(respuestaAltaDetalle);
		return respuesta;
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
	
	
}
