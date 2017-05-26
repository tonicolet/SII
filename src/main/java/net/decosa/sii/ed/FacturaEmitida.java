package net.decosa.sii.ed;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.decosa.sii.SiiConfig;
import net.decosa.sii.aeat.ClaveTipoFacturaType;
import net.decosa.sii.util.NumberUtils;
import net.decosa.sii.util.StringUtils;


@Getter @Setter
public class FacturaEmitida extends Factura {
	
	private String tipoFactura = TIPO_FACTURA_F1;
	private String claveRegimenEspecialOTrascendencia;
	
	private String descripcionOperacion;
	private Double importeTotal;
	private Boolean exenta;
	private String tipoNoExenta;
	
	private List<DesgloseIVAFE> desglosesIVA;
	
	
	public void setDesgloseIVANoExenta(String tipoNoExenta, List<DesgloseIVAFE> desglosesIVA) {
		this.tipoNoExenta = tipoNoExenta;
		this.desglosesIVA = desglosesIVA;
		this.exenta = false;
	}
	
	
	public String getImporteTotalSII() {
		return "" + NumberUtils.round(importeTotal);
	}
	
	
	public ClaveTipoFacturaType getTipoFacturaSII() {
		if (ClaveTipoFacturaType.F_1.value().compareTo(tipoFactura) == 0)
			return ClaveTipoFacturaType.F_1;
		
		return null;
	}
	
	
	public String getDescripcionOperacionSII() {
		return StringUtils.limit(claveRegimenEspecialOTrascendencia, SiiConfig.MAX_LONG_DESCRIPCION_FACTURA);
	}
	
}
