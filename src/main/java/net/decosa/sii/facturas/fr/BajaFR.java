package net.decosa.sii.facturas.fr;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import net.decosa.sii.aeat.BajaLRFacturasRecibidas;
import net.decosa.sii.aeat.LRBajaRecibidasType;
import net.decosa.sii.aeat.RespuestaLRBajaFRecibidasType;
import net.decosa.sii.aeat.RespuestaRecibidaBajaType;
import net.decosa.sii.ed.FacturaRecibida;
import net.decosa.sii.facturas.AbstractBajaFacturasSII;
import net.decosa.sii.facturas.Respuesta;


@Component
public class BajaFR extends AbstractBajaFacturasSII {
	
	private BajaLRFacturasRecibidas bajaFR;

	
	@SuppressWarnings("unchecked")
	@Override
	protected void procesar() throws Exception {
		// Se prepara la información
		bajaFR = new BajaLRFacturasRecibidas();
		
		// Cabecera
		bajaFR.setCabecera(getCabeceraBajaSII());

		// Facturas
		List<LRBajaRecibidasType> facturasRecibidasBaja = toFacturasRecibidasSII((List<FacturaRecibida>) facturas);
		bajaFR.getRegistroLRBajaRecibidas().addAll(facturasRecibidasBaja);		
	}

	
	@Override
	public String getXML(boolean pretty) {
		return getXML(bajaFR, pretty);
	}
	
	
	@Override
	public String getXML() {
		return getXML(bajaFR);
	}

	
	@Override
	public Respuesta enviar(boolean simularEnvio) throws Exception {
		// Envío
		RespuestaLRBajaFRecibidasType respuestaFR = wsSIIRequest.bajaFR(bajaFR, simularEnvio);
		
		// Guardar XML de respuesta?
		
		// Procesar respuesta
		return procesarRespuesta(respuestaFR, simularEnvio);
		
	}

	
	private Respuesta procesarRespuesta(RespuestaLRBajaFRecibidasType respuestaFR, boolean simularEnvio) {
		if (simularEnvio) return null;
		
		// Procesar cabecera
		Respuesta respuestaAlta = procesarRespuestaCabecera(respuestaFR.getEstadoEnvio(),
				respuestaFR.getDatosPresentacion(), respuestaFR.getCSV());
		
		// Procesar detalles
		for(RespuestaRecibidaBajaType respuestaLinea: respuestaFR.getRespuestaLinea())
			procesarRespuestaDetalles(respuestaAlta, respuestaLinea.getIDFactura().getNumSerieFacturaEmisor(),
					respuestaLinea.getEstadoRegistro(), respuestaLinea.getDescripcionErrorRegistro(),
					respuestaLinea.getCodigoErrorRegistro());
		
		return respuestaAlta;
	}

	
	private List<LRBajaRecibidasType> toFacturasRecibidasSII(List<FacturaRecibida> facturasRecibidas) {
		List<LRBajaRecibidasType> facturasRecibidasSII = new ArrayList<LRBajaRecibidasType>();
		
		for(FacturaRecibida facturaRecibida: facturasRecibidas) {
			
			// FacturaSII
			LRBajaRecibidasType facturaRecibidaSII = new LRBajaRecibidasType();
			
			// Periodo impositivo
			facturaRecibidaSII.setPeriodoImpositivo(facturaRecibida.getPeriodoImpositivo().toPeriodoImpositivoSII());
			
			// idFactura
			facturaRecibidaSII.setIDFactura(facturaRecibida.getIdFactura().getIDFacturaRecibidaNombreBCType());
			facturasRecibidasSII.add(facturaRecibidaSII);
		}
		
		return facturasRecibidasSII;
	}
}
