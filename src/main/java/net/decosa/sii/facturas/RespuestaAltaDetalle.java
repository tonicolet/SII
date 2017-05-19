package net.decosa.sii.facturas;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespuestaAltaDetalle {
	
	private String numSerieFactura;
	private String estadoRegistro;
	private String descripcionError;
	private Integer codigoError;
	private String csv;
	
	
	public Boolean erroneo() {
		return (codigoError != null);
	}
}
