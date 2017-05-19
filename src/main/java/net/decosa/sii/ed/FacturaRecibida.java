package net.decosa.sii.ed;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.decosa.sii.util.DateUtils;


@Getter @Setter
public class FacturaRecibida extends Factura {
	
	private Double importeTotal;
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
}
