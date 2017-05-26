package net.decosa.sii.facturas;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RespuestaDetalle {
	
	private String numSerieFactura;
	private String estadoRegistro;
	private String descripcionError;
	private Integer codigoError;
	
	
	public Boolean erroneo() {
		return (codigoError != null);
	}
}
