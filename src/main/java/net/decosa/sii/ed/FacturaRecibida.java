package net.decosa.sii.ed;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class FacturaRecibida extends Factura {
	
	private Double importeTotal;
	private Double cuotaDeducible;
	private Date fechaRegContable;
	private Date fechaOperacion;
	private List<DesgloseIVAFR> detallesIVA;
}
