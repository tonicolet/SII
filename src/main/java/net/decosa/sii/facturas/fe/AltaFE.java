package net.decosa.sii.facturas.fe;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.decosa.sii.aeat.DetalleIVAEmitidaType;
import net.decosa.sii.aeat.FacturaExpedidaType;
import net.decosa.sii.aeat.FacturaExpedidaType.TipoDesglose;
import net.decosa.sii.aeat.LRfacturasEmitidasType;
import net.decosa.sii.aeat.RespuestaExpedidaType;
import net.decosa.sii.aeat.RespuestaLRFEmitidasType;
import net.decosa.sii.aeat.SujetaType;
import net.decosa.sii.aeat.SujetaType.NoExenta;
import net.decosa.sii.aeat.SujetaType.NoExenta.DesgloseIVA;
import net.decosa.sii.aeat.SuministroLRFacturasEmitidas;
import net.decosa.sii.aeat.TipoOperacionSujetaNoExentaType;
import net.decosa.sii.aeat.TipoSinDesgloseType;
import net.decosa.sii.ed.DesgloseIVAFE;
import net.decosa.sii.ed.FacturaEmitida;
import net.decosa.sii.facturas.AbstractAltaFacturasSII;
import net.decosa.sii.facturas.RespuestaAlta;
import net.decosa.sii.util.NumberUtils;
import net.decosa.sii.ws.WSSIIRequest;


@Component
public class AltaFE extends AbstractAltaFacturasSII {
	
	@Autowired
	private WSSIIRequest wsSIIRequest;
	
	@Value("${empresa.cif}")
	private String cif;
	
	private SuministroLRFacturasEmitidas suministroFE;
	

	
	@SuppressWarnings("unchecked")
	protected void procesarDatos() throws Exception {
		// Se prepara la información
		suministroFE = new SuministroLRFacturasEmitidas();
		
		// Cabecera
		suministroFE.setCabecera(getCabeceraAltaSII());
		
		// Facturas
		List<LRfacturasEmitidasType> facturasEmitidasSII = toFacturasEmitidasSII((List<FacturaEmitida>) facturas);
		suministroFE.getRegistroLRFacturasEmitidas().addAll(facturasEmitidasSII);
	}

	
	@Override
	public String getXML(boolean pretty) throws Exception {
		return getXML(suministroFE, pretty);
	}
	
	
	@Override
	public String getXML() throws Exception {
		return getXML(suministroFE);
	}
	
	
	@Override
	public RespuestaAlta send(boolean simularEnvio) throws Exception {
		// Envío
		RespuestaLRFEmitidasType respuestaFR = wsSIIRequest.altaFE(suministroFE, simularEnvio);
		
		// Guardar XML de respuesta?
		
		// Procesar respuesta
		return procesarRespuesta(respuestaFR, simularEnvio);
	}
	
	
	private RespuestaAlta procesarRespuesta(RespuestaLRFEmitidasType respuestaFE, boolean simularEnvio) {
		if (simularEnvio) return null;
		
		// Procesar cabecera
		RespuestaAlta respuestaAlta = procesarRespuestaCabecera(respuestaFE.getEstadoEnvio(), respuestaFE.getDatosPresentacion(), respuestaFE.getCSV());
		
		// Procesar detalles
		for(RespuestaExpedidaType respuestaLinea: respuestaFE.getRespuestaLinea())
			procesarRespuestaDetalles(respuestaAlta, respuestaLinea.getIDFactura().getNumSerieFacturaEmisor(), respuestaLinea.getEstadoRegistro(),
					respuestaLinea.getCSV(), respuestaLinea.getDescripcionErrorRegistro(), respuestaLinea.getCodigoErrorRegistro());
		
		return respuestaAlta;
	}
	
	
	private List<LRfacturasEmitidasType> toFacturasEmitidasSII(List<FacturaEmitida> facturasEmitidas) {
		List<LRfacturasEmitidasType> facturasEmitidasSII = new ArrayList<LRfacturasEmitidasType>();
		
		for(FacturaEmitida facturaEmitida: facturasEmitidas) {
			
			// Factura
			LRfacturasEmitidasType facturaEmitidaSII = new LRfacturasEmitidasType();
			
			// Periodo impositivo
			facturaEmitidaSII.setPeriodoImpositivo(facturaEmitida.getPeriodoImpositivo().toPeriodoImpositivoSII());
			
			// idFactura
			facturaEmitidaSII.setIDFactura(facturaEmitida.getIdFactura().toIDFacturaExpedidaType());
						
			FacturaExpedidaType facturaExpedidaType = new FacturaExpedidaType();
			facturaExpedidaType.setTipoFactura(facturaEmitida.getTipoFacturaSII());
			facturaExpedidaType.setClaveRegimenEspecialOTrascendencia(facturaEmitida.getClaveRegimenEspecialOTrascendencia());
			facturaExpedidaType.setImporteTotal("" + NumberUtils.round(facturaEmitida.getImporteTotal()));
			facturaExpedidaType.setDescripcionOperacion(facturaEmitida.getDescripcionOperacion());
			
			// Contraparte
			facturaExpedidaType.setContraparte(facturaEmitida.getContraparte().toPersonaFisicaJuridicaType());
			
			// Tipo desglose
			TipoDesglose tipoDesglose = new TipoDesglose();
			
			// Tipo sin (?) desglose
			TipoSinDesgloseType tipoSinDesglose = new TipoSinDesgloseType();

			SujetaType sujeta = new SujetaType();

			// No exenta
			NoExenta noExenta = new NoExenta();

			// Tipo No exenta
			noExenta.setTipoNoExenta(TipoOperacionSujetaNoExentaType.S_1);
			
			// Desglose IVA
			DesgloseIVA desgloseIVA = new DesgloseIVA();
			
			
			for(DesgloseIVAFE desgloseIVAFE: facturaEmitida.getDesglosesIVA()) {
				
				// Detalle IVA 
				DetalleIVAEmitidaType detalleIVAEmitida = new DetalleIVAEmitidaType();
				detalleIVAEmitida.setTipoImpositivo("" + desgloseIVAFE.getTipoImpositivo());
				detalleIVAEmitida.setBaseImponible("" + desgloseIVAFE.getBaseImponible());
				detalleIVAEmitida.setCuotaRepercutida("" + desgloseIVAFE.getCuotaRepercutida());
				
				// ???
				detalleIVAEmitida.setTipoRecargoEquivalencia("" + desgloseIVAFE.getCuotaRecargoEquivalencia());
				detalleIVAEmitida.setCuotaRecargoEquivalencia("" + desgloseIVAFE.getCuotaRecargoEquivalencia());
				
				desgloseIVA.getDetalleIVA().add(detalleIVAEmitida);
			}
			
			noExenta.setDesgloseIVA(desgloseIVA);
			sujeta.setNoExenta(noExenta);

			tipoSinDesglose.setSujeta(sujeta);
			tipoDesglose.setDesgloseFactura(tipoSinDesglose);
			facturaExpedidaType.setTipoDesglose(tipoDesglose);
			
			facturaEmitidaSII.setFacturaExpedida(facturaExpedidaType);
			facturasEmitidasSII.add(facturaEmitidaSII);
		}
		
		return facturasEmitidasSII;
	}

}
