package net.decosa.sii.ed;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.decosa.sii.SiiConfig;
import net.decosa.sii.aeat.ClaveTipoFacturaType;
import net.decosa.sii.util.DateUtils;
import net.decosa.sii.util.StringUtils;


@Getter @Setter
public class FacturaRecibida extends Factura {
	
	private String tipoFactura = TIPO_FACTURA_F1;
	private String claveRegimenEspecialOTrascendencia;
	
	private String descripcionOperacion;
	private Double cuotaDeducible;
	private Date fechaRegContable;
	private Date fechaOperacion;
	private List<DesgloseIVAFR> detallesIVA;
	
	
	public String getFechaOperacionSII() {
		return DateUtils.format(fechaOperacion);
	}
	
	
	public String getFechaRegContableSII() {
		return DateUtils.format(fechaRegContable);
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
