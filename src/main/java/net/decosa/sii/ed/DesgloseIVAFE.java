package net.decosa.sii.ed;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class DesgloseIVAFE extends DesgloseIVA {

	private Double cuotaRepercutida;
	private Integer tipoRecargoEquivalencia = 0;
	private Integer cuotaRecargoEquivalencia = 0;
	private Boolean isp = false;
}
