package net.decosa.facturas.fe;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.decosa.sii.SiiConfig;
import net.decosa.sii.ed.Contraparte;
import net.decosa.sii.ed.FacturaEmitida;
import net.decosa.sii.ed.DesgloseIVAFE;
import net.decosa.sii.ed.IDFactura;
import net.decosa.sii.ed.PeriodoImpositivo;
import net.decosa.sii.facturas.fe.AltaFE;
import net.decosa.sii.util.DateUtils;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SiiConfig.class)
public class AltaFETest {

	@Autowired
	private AltaFE altaFE;
	
	private List<FacturaEmitida> facturasEmitidas;
	
	
	@Before
	public void inicializaDatos() {
		facturasEmitidas = new ArrayList<FacturaEmitida>();
		
		// Factura 1
		FacturaEmitida f = new FacturaEmitida();
		f.setPeriodoImpositivo(new PeriodoImpositivo(2017, 1));
		f.setIdFactura(new IDFactura("01", DateUtils.parse("15-01-2105"), "A84532501"));
		f.setContraparte(new Contraparte("EMPRESAYYYYYYYYYY", "94234500B"));
		
		f.setTipoFactura(FacturaEmitida.TIPO_FACTURA_F1);
		f.setClaveRegimenEspecialOTrascendencia("01");
		f.setImporteTotal(26.7);
		f.setDescripcionOperacion("CompraXXXXXXX");
		
		// Detalles IVA
		List<DesgloseIVAFE> detallesFactura = new ArrayList<DesgloseIVAFE>();
		
		DesgloseIVAFE detalle = new DesgloseIVAFE();
		detalle.setBaseImponible(22.07);
		detalle.setTipoImpositivo(21);
		detalle.setCuotaRepercutida(4.63);
		detallesFactura.add(detalle);
		
		f.setDesgloseIVANoExenta(FacturaEmitida.TIPO_NO_EXENTA_S1, detallesFactura);
		facturasEmitidas.add(f);
	}
	
	
	@Test
	public void altaFETest() {
		try {
			altaFE.setFacturas(facturasEmitidas);
			System.out.println(altaFE.getXML());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
