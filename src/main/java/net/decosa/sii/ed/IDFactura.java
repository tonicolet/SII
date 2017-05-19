package net.decosa.sii.ed;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import net.decosa.sii.SiiConfig;
import net.decosa.sii.aeat.IDFacturaExpedidaType;
import net.decosa.sii.aeat.IDFacturaRecibidaType;
import net.decosa.sii.util.DateUtils;


@Getter @Setter
public class IDFactura {
	
	private String numSerieFactura;
	private Date fechaExpedicionFactura;
	private String nif;
	
	
	
	public IDFactura(String numSerieFactura, String fechaExpedicionFactura, String nif) {
		this(numSerieFactura, DateUtils.parse(fechaExpedicionFactura), nif);
	}
	
	
	public IDFactura(String numSerieFactura, Date fechaExpedicionFactura, String nif) {
		this.numSerieFactura = numSerieFactura;
		this.fechaExpedicionFactura = fechaExpedicionFactura;
		this.nif = nif;
	}
	
	
	public IDFacturaExpedidaType toIDFacturaExpedidaType() {
		IDFacturaExpedidaType idFactura = new IDFacturaExpedidaType();
		idFactura.setFechaExpedicionFacturaEmisor(DateUtils.format(fechaExpedicionFactura, SiiConfig.DEFAULT_DATE_FORMAT));
		idFactura.setNumSerieFacturaEmisor(numSerieFactura);
		
		net.decosa.sii.aeat.IDFacturaExpedidaType.IDEmisorFactura emisorFactura = new net.decosa.sii.aeat.IDFacturaExpedidaType.IDEmisorFactura();
		emisorFactura.setNIF(nif);
		
		idFactura.setIDEmisorFactura(emisorFactura);
		return idFactura;
	}
	
	
	public IDFacturaRecibidaType toIDFacturaRecibidaSII() {
		IDFacturaRecibidaType idFactura = new IDFacturaRecibidaType();
		idFactura.setFechaExpedicionFacturaEmisor(DateUtils.format(fechaExpedicionFactura, SiiConfig.DEFAULT_DATE_FORMAT));
		idFactura.setNumSerieFacturaEmisor(numSerieFactura);
		
		net.decosa.sii.aeat.IDFacturaRecibidaType.IDEmisorFactura emisorFactura = new net.decosa.sii.aeat.IDFacturaRecibidaType.IDEmisorFactura();
		emisorFactura.setNIF(nif);
		
		idFactura.setIDEmisorFactura(emisorFactura);
		return idFactura;
	}
	
}
