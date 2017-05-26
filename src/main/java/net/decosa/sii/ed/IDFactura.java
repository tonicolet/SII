package net.decosa.sii.ed;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import net.decosa.sii.SiiConfig;
import net.decosa.sii.aeat.IDFacturaComunitariaType;
import net.decosa.sii.aeat.IDFacturaExpedidaBCType;
import net.decosa.sii.aeat.IDFacturaExpedidaType;
import net.decosa.sii.aeat.IDFacturaRecibidaNombreBCType;
import net.decosa.sii.aeat.IDFacturaRecibidaType;
import net.decosa.sii.util.DateUtils;
import net.decosa.sii.util.StringUtils;


@Getter @Setter
public class IDFactura {
	
	private String numSerieFactura;
	private Date fechaExpedicionFactura;
	private String nif;
	private String nombreRazon;
	
	
	
	public IDFactura(String numSerieFactura, String fechaExpedicionFactura, String nif, String nombreRazon) {
		this(numSerieFactura, DateUtils.parse(fechaExpedicionFactura), nif, nombreRazon);
	}

	
	public IDFactura(String numSerieFactura, Date fechaExpedicionFactura, String nif) {
		this(numSerieFactura, fechaExpedicionFactura, nif, null);
	}
	
	
	public IDFactura(String numSerieFactura, Date fechaExpedicionFactura, String nif, String nombreRazon) {
		this.numSerieFactura = numSerieFactura;
		this.fechaExpedicionFactura = fechaExpedicionFactura;
		this.nif = nif;
		this.nombreRazon = nombreRazon;
	}
	
	
	public IDFacturaExpedidaType getIDFacturaExpedidaType() {
		IDFacturaExpedidaType idFactura = new IDFacturaExpedidaType();
		idFactura.setFechaExpedicionFacturaEmisor(preparaFechaExpedicionFacturaEmisor());
		idFactura.setNumSerieFacturaEmisor(preparaNumSerieFacturaEmisorSII());
		
		net.decosa.sii.aeat.IDFacturaExpedidaType.IDEmisorFactura emisorFactura = new net.decosa.sii.aeat.IDFacturaExpedidaType.IDEmisorFactura();
		emisorFactura.setNIF(preparaNif());
		
		idFactura.setIDEmisorFactura(emisorFactura);
		return idFactura;
	}
	
	
	public IDFacturaRecibidaType getIDFacturaRecibidaType() {
		IDFacturaRecibidaType idFactura = new IDFacturaRecibidaType();
		idFactura.setFechaExpedicionFacturaEmisor(preparaFechaExpedicionFacturaEmisor());
		idFactura.setNumSerieFacturaEmisor(preparaNumSerieFacturaEmisorSII());
		
		net.decosa.sii.aeat.IDFacturaRecibidaType.IDEmisorFactura emisorFactura = new net.decosa.sii.aeat.IDFacturaRecibidaType.IDEmisorFactura();
		emisorFactura.setNIF(preparaNif());
		
		idFactura.setIDEmisorFactura(emisorFactura);
		return idFactura;
	}
	
	
	public IDFacturaComunitariaType getIDFacturaComunitariaType() {
		IDFacturaComunitariaType idFactura = new IDFacturaComunitariaType();
		idFactura.setFechaExpedicionFacturaEmisor(preparaFechaExpedicionFacturaEmisor());
		idFactura.setNumSerieFacturaEmisor(preparaNumSerieFacturaEmisorSII());
		
		net.decosa.sii.aeat.IDFacturaComunitariaType.IDEmisorFactura emisorFactura = new net.decosa.sii.aeat.IDFacturaComunitariaType.IDEmisorFactura();
		emisorFactura.setNIF(preparaNif());
		
		idFactura.setIDEmisorFactura(emisorFactura);
		return idFactura;
	}
	
	
	public IDFacturaRecibidaNombreBCType getIDFacturaRecibidaNombreBCType() {
		IDFacturaRecibidaNombreBCType idFactura = new IDFacturaRecibidaNombreBCType();
		idFactura.setNumSerieFacturaEmisor(preparaNumSerieFacturaEmisorSII());
		idFactura.setFechaExpedicionFacturaEmisor(preparaFechaExpedicionFacturaEmisor());
		
		net.decosa.sii.aeat.IDFacturaRecibidaNombreBCType.IDEmisorFactura emisorFactura = new net.decosa.sii.aeat.IDFacturaRecibidaNombreBCType.IDEmisorFactura();
		emisorFactura.setNIF(preparaNif());
		emisorFactura.setNombreRazon(preparaNombreRazon());
		
		idFactura.setIDEmisorFactura(emisorFactura);
		return idFactura;
	}
	
	
	public IDFacturaExpedidaBCType getIDFacturaExpedidaBCType() {
		IDFacturaExpedidaBCType idFactura = new IDFacturaExpedidaBCType();
		idFactura.setNumSerieFacturaEmisor(preparaNumSerieFacturaEmisorSII());
		idFactura.setFechaExpedicionFacturaEmisor(preparaFechaExpedicionFacturaEmisor());
		
		net.decosa.sii.aeat.IDFacturaExpedidaBCType.IDEmisorFactura emisorFactura = new net.decosa.sii.aeat.IDFacturaExpedidaBCType.IDEmisorFactura();
		emisorFactura.setNIF(preparaNif());
		
		idFactura.setIDEmisorFactura(emisorFactura);
		return idFactura;
	}
	
	
	private String preparaNumSerieFacturaEmisorSII() {
		return StringUtils.limit(numSerieFactura, SiiConfig.MAX_LONG_NUM_SERIE_FACTURA);
	}
	
	
	private String preparaFechaExpedicionFacturaEmisor() {
		return DateUtils.format(fechaExpedicionFactura);
	}
	
	
	private String preparaNif() {
		return StringUtils.limit(nif, SiiConfig.MAX_LONG_NIF);
	}
	
	
	private String preparaNombreRazon() {
		return nombreRazon;
	}
}
