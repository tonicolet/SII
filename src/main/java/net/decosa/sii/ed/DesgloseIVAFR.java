package net.decosa.sii.ed;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class DesgloseIVAFR extends DesgloseIVA {

	private Double cuotaSoportada;
	private Integer tipoImpositivo;
	private Integer tipoRecargoEquivalencia = 0;
	private Integer cuotaRecargoEquivalencia = 0;
	private Boolean isp = false;
	
	
	public void setCuotaSoportada(Integer cuotaSoportada) {
		setCuotaSoportada(cuotaSoportada.doubleValue());
	}
	
	
	public void setCuotaSoportada(Double cuotaSoportada) {
		this.cuotaSoportada = cuotaSoportada;
	}
}
