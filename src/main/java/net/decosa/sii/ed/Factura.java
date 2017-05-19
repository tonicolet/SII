package net.decosa.sii.ed;

import lombok.Getter;
import lombok.Setter;
import net.decosa.sii.aeat.ClaveTipoFacturaType;


@Getter @Setter
public class Factura {
	
	public static final String TIPO_FACTURA_F1	= "F1";
	
	private PeriodoImpositivo periodoImpositivo;
	private IDFactura idFactura;
	private Contraparte contraparte;
	
	private String descripcionOperacion;
	private String tipoFactura = TIPO_FACTURA_F1;
	private String claveRegimenEspecialOTrascendencia;
	
	
	
	public ClaveTipoFacturaType getTipoFacturaSII() {
		if (ClaveTipoFacturaType.F_1.value().compareTo(tipoFactura) == 0)
			return ClaveTipoFacturaType.F_1;
		
		return null;
	}
}
