package net.decosa.sii.facturas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.decosa.sii.util.StringUtils;


@Getter @Setter @ToString
public class Respuesta {
	
	private String estadoEnvio;
	private Date fechaPresentacion;
	private String csv;
	private List<RespuestaDetalle> respuestaDetalles;
	private Integer numeroFacturasCorrectas;
	private Integer numeroFacturasIncorrectas;
	
	
	public Respuesta() {
		this.respuestaDetalles = new ArrayList<RespuestaDetalle>();
		this.numeroFacturasCorrectas = 0;
		this.numeroFacturasIncorrectas = 0;
	}
	
	
	public void addRespuestaDetalle(RespuestaDetalle respuestaDetalle) {
		if (!StringUtils.isBlank(respuestaDetalle.getDescripcionError())) numeroFacturasIncorrectas++;
		else numeroFacturasCorrectas++;
		
		respuestaDetalles.add(respuestaDetalle);
	}
}
