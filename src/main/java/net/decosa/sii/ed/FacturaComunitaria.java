package net.decosa.sii.ed;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class FacturaComunitaria extends Factura {
	
	private String tipoOperacion;
	private String claveDeclarado;
	private String direccionOperador;
	private String descripcionBienes;
	private String estadoMiembro;
}
