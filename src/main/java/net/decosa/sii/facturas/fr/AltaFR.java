package net.decosa.sii.facturas.fr;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.decosa.sii.aeat.DesgloseFacturaRecibidasType;
import net.decosa.sii.aeat.DesgloseFacturaRecibidasType.DesgloseIVA;
import net.decosa.sii.aeat.DesgloseFacturaRecibidasType.InversionSujetoPasivo;
import net.decosa.sii.aeat.DetalleIVARecibida2Type;
import net.decosa.sii.aeat.DetalleIVARecibidaType;
import net.decosa.sii.aeat.FacturaRecibidaType;
import net.decosa.sii.aeat.LRFacturasRecibidasType;
import net.decosa.sii.aeat.RespuestaLRFRecibidasType;
import net.decosa.sii.aeat.RespuestaRecibidaType;
import net.decosa.sii.aeat.SuministroLRFacturasRecibidas;
import net.decosa.sii.ed.DesgloseIVAFR;
import net.decosa.sii.ed.FacturaRecibida;
import net.decosa.sii.facturas.AbstractAltaFacturasSII;
import net.decosa.sii.facturas.RespuestaAlta;
import net.decosa.sii.util.DateUtils;
import net.decosa.sii.util.NumberUtils;
import net.decosa.sii.ws.WSSIIRequest;


@Component
public class AltaFR extends AbstractAltaFacturasSII {
	
	@Value("${empresa.cif}")
	private String cif;
	
	@Value("${empresa.nombre}")
	private String nombreEmpresaDeco;
	
	@Autowired
	private WSSIIRequest wsSIIRequest;
	
	private SuministroLRFacturasRecibidas suministroFR;

	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void procesarDatos() throws Exception {
		// Se prepara la información
		suministroFR = new SuministroLRFacturasRecibidas();
		
		// Cabecera
		suministroFR.setCabecera(getCabeceraAltaSII());
		
		// Facturas
		List<LRFacturasRecibidasType> facturasRecibidas = toFacturasRecibidasSII((List<FacturaRecibida>) facturas);
		suministroFR.getRegistroLRFacturasRecibidas().addAll(facturasRecibidas);		
	}

	
	@Override
	public String getXML(boolean pretty) throws Exception {
		return getXML(suministroFR, pretty);
	}
	
	
	@Override
	public String getXML() throws Exception {
		return getXML(suministroFR);
	}

	
	@Override
	public RespuestaAlta send(boolean simularEnvio) throws Exception {
		// Envío
		RespuestaLRFRecibidasType respuestaFR = wsSIIRequest.altaFR(suministroFR, simularEnvio);
		
		// Guardar XML de respuesta?
		
		// Procesar respuesta
		return procesarRespuesta(respuestaFR, simularEnvio);
		
	}

	
	private RespuestaAlta procesarRespuesta(RespuestaLRFRecibidasType respuestaFR, boolean simularEnvio) {
		if (simularEnvio) return null;
		
		// Procesar cabecera
		RespuestaAlta respuestaAlta = procesarRespuestaCabecera(respuestaFR.getEstadoEnvio(), respuestaFR.getDatosPresentacion(), respuestaFR.getCSV());
		
		// Procesar detalles
		for(RespuestaRecibidaType respuestaLinea: respuestaFR.getRespuestaLinea())
			procesarRespuestaDetalles(respuestaAlta, respuestaLinea.getIDFactura().getNumSerieFacturaEmisor(), respuestaLinea.getEstadoRegistro(), 
					respuestaLinea.getCSV(), respuestaLinea.getDescripcionErrorRegistro(), respuestaLinea.getCodigoErrorRegistro());
		
		return respuestaAlta;
	}

	
	private List<LRFacturasRecibidasType> toFacturasRecibidasSII(List<FacturaRecibida> facturasRecibidas) {
		List<LRFacturasRecibidasType> facturasRecibidasSII = new ArrayList<LRFacturasRecibidasType>();
		
		for(FacturaRecibida facturaRecibida: facturasRecibidas) {
			
			// FacturaSII
			LRFacturasRecibidasType facturaRecibidaSII = new LRFacturasRecibidasType();
			
			// Periodo impositivo
			facturaRecibidaSII.setPeriodoImpositivo(facturaRecibida.getPeriodoImpositivo().toPeriodoImpositivoSII());
			
			// idFactura
			facturaRecibidaSII.setIDFactura(facturaRecibida.getIdFactura().toIDFacturaRecibidaSII());
			
			// Factura recibida
			FacturaRecibidaType facturaRecibidaType = new FacturaRecibidaType();
			facturaRecibidaType.setTipoFactura(facturaRecibida.getTipoFacturaSII());
			facturaRecibidaType.setClaveRegimenEspecialOTrascendencia(facturaRecibida.getClaveRegimenEspecialOTrascendencia());
			facturaRecibidaType.setFechaOperacion(DateUtils.format(facturaRecibida.getFechaOperacion()));
			
			// Contraparte
			facturaRecibidaType.setContraparte(facturaRecibida.getContraparte().toPersonaFisicaJuridicaType());
			
			// Desglose factura
			DesgloseFacturaRecibidasType desgloseFactura = new DesgloseFacturaRecibidasType();
			InversionSujetoPasivo isp = null;
			DesgloseIVA iva = null;
			Double quotaDeducible = 0.0;
			
			for(DesgloseIVAFR facturaLinea: facturaRecibida.getDetallesIVA()) {
				
				if (facturaLinea.getIsp()) {
					if (isp == null) isp = new InversionSujetoPasivo();
					
					// Detalle IVA 
					DetalleIVARecibida2Type detalleIVARecibida = new DetalleIVARecibida2Type();
					detalleIVARecibida.setTipoImpositivo("" + facturaLinea.getTipoImpositivo());
					detalleIVARecibida.setBaseImponible("" + facturaLinea.getBaseImponible());
					detalleIVARecibida.setCuotaSoportada("" + facturaLinea.getCuotaSoportada());
					
					isp.getDetalleIVA().add(detalleIVARecibida);
					
				} else {
					if (iva == null) iva = new DesgloseIVA();
					
					// Detalle IVA 
					DetalleIVARecibidaType detalleIVARecibida = new DetalleIVARecibidaType();
					detalleIVARecibida.setTipoImpositivo("" + facturaLinea.getTipoImpositivo());
					detalleIVARecibida.setBaseImponible("" + facturaLinea.getBaseImponible());
					detalleIVARecibida.setCuotaSoportada("" + facturaLinea.getCuotaSoportada());
					quotaDeducible += facturaLinea.getCuotaSoportada();
					
					iva.getDetalleIVA().add(detalleIVARecibida);
				}
			}
			
			if (isp != null) desgloseFactura.setInversionSujetoPasivo(isp);
			if (iva != null) desgloseFactura.setDesgloseIVA(iva);
			facturaRecibidaType.setDesgloseFactura(desgloseFactura);
			
			facturaRecibidaType.setDescripcionOperacion(facturaRecibida.getDescripcionOperacion());
			facturaRecibidaType.setFechaRegContable(DateUtils.format(facturaRecibida.getFechaRegContable()));
			facturaRecibidaType.setFechaOperacion(DateUtils.format(facturaRecibida.getFechaRegContable()));
			facturaRecibidaType.setCuotaDeducible("" + NumberUtils.round(quotaDeducible));
			
			facturaRecibidaSII.setFacturaRecibida(facturaRecibidaType);
			facturasRecibidasSII.add(facturaRecibidaSII);
		}
		
		return facturasRecibidasSII;
	}

}
