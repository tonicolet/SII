package net.decosa.sii.ed;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class Factura {
	
	public static final String TIPO_FACTURA_F1		= "F1";
	public static final String TIPO_NO_EXENTA_S1	= "S1";
	
	private PeriodoImpositivo periodoImpositivo;
	private IDFactura idFactura;
	private Contraparte contraparte;
}
