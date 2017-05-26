package net.decosa.sii.facturas.fe;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import net.decosa.sii.aeat.BajaLRFacturasEmitidas;
import net.decosa.sii.aeat.LRBajaExpedidasType;
import net.decosa.sii.aeat.RespuestaExpedidaBajaType;
import net.decosa.sii.aeat.RespuestaLRBajaFEmitidasType;
import net.decosa.sii.ed.FacturaEmitida;
import net.decosa.sii.facturas.AbstractBajaFacturasSII;
import net.decosa.sii.facturas.Respuesta;


@Component
public class BajaFE extends AbstractBajaFacturasSII {
	
	private BajaLRFacturasEmitidas bajaFE;

	
	@SuppressWarnings("unchecked")
	@Override
	protected void procesar() throws Exception {
		// Se prepara la información
		bajaFE = new BajaLRFacturasEmitidas();
		
		// Cabecera
		bajaFE.setCabecera(getCabeceraBajaSII());

		// Facturas
		List<LRBajaExpedidasType> facturasEmitidasBaja = toFacturasExpedidasSII((List<FacturaEmitida>) facturas);
		bajaFE.getRegistroLRBajaExpedidas().addAll(facturasEmitidasBaja);
	}

	
	@Override
	public String getXML(boolean pretty) {
		return getXML(bajaFE, pretty);
	}
	
	
	@Override
	public String getXML() {
		return getXML(bajaFE);
	}

	
	@Override
	public Respuesta enviar(boolean simularEnvio) throws Exception {
		// Envío
		RespuestaLRBajaFEmitidasType respuestaFE = wsSIIRequest.bajaFE(bajaFE, simularEnvio);
		
		// Guardar XML de respuesta?
		
		// Procesar respuesta
		return procesarRespuesta(respuestaFE, simularEnvio);
		
	}

	
	private Respuesta procesarRespuesta(RespuestaLRBajaFEmitidasType respuestaFE, boolean simularEnvio) {
		if (simularEnvio) return null;
		
		// Procesar cabecera
		Respuesta respuestaAlta = procesarRespuestaCabecera(respuestaFE.getEstadoEnvio(),
				respuestaFE.getDatosPresentacion(), respuestaFE.getCSV());
		
		// Procesar detalles
		for(RespuestaExpedidaBajaType respuestaLinea: respuestaFE.getRespuestaLinea())
			procesarRespuestaDetalles(respuestaAlta, respuestaLinea.getIDFactura().getNumSerieFacturaEmisor(),
					respuestaLinea.getEstadoRegistro(), respuestaLinea.getDescripcionErrorRegistro(),
					respuestaLinea.getCodigoErrorRegistro());
		
		return respuestaAlta;
	}

	
	private List<LRBajaExpedidasType> toFacturasExpedidasSII(List<FacturaEmitida> facturasEmitidas) {
		List<LRBajaExpedidasType> facturasEmitidasSII = new ArrayList<LRBajaExpedidasType>();
		
		for(FacturaEmitida facturaEmitida: facturasEmitidas) {
			
			// FacturaSII
			LRBajaExpedidasType facturaRecibidaSII = new LRBajaExpedidasType();
			
			// Periodo impositivo
			facturaRecibidaSII.setPeriodoImpositivo(facturaEmitida.getPeriodoImpositivo().toPeriodoImpositivoSII());
			
			// idFactura
			facturaRecibidaSII.setIDFactura(facturaEmitida.getIdFactura().getIDFacturaExpedidaBCType());
			facturasEmitidasSII.add(facturaRecibidaSII);
		}
		
		return facturasEmitidasSII;
	}
}
