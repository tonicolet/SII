package net.decosa.sii.ed;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class FacturaEmitida extends Factura {
	
	public static final String TIPO_FACTURA_F1		= "F1";
	public static final String TIPO_NO_EXENTA_S1	= "S1";
	
	private Double importeTotal;
	private Boolean exenta;
	private String tipoNoExenta;
	
	private List<DesgloseIVAFE> desglosesIVA;
	
	
	public void setDesgloseIVANoExenta(String tipoNoExenta, List<DesgloseIVAFE> desglosesIVA) {
		this.tipoNoExenta = tipoNoExenta;
		this.desglosesIVA = desglosesIVA;
		this.exenta = false;
	}
	
}
