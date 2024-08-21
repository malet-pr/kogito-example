package org.acme.kogitoexample.adapter;

import java.util.List;
import org.acme.kogitoexample.model.Regla;
import org.acme.kogitoexample.model.ReglaTipo;
import org.kie.api.runtime.KieContainer;

public class ReglaAdapter {

	private ReglaTipo tipo;
	private String header;
	private List<Regla> reglas;
	private String reglaCompleta;			
	private KieContainer kieContainer;
	
	public ReglaTipo getTipo() {
		return tipo;
	}
	public void setTipo(ReglaTipo tipo) {
		this.tipo = tipo;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public List<Regla> getReglas() {
		return reglas;
	}
	public void setReglas(List<Regla> reglas) {
		this.reglas = reglas;
	}
	public String getReglaCompleta() {
		return reglaCompleta;
	}
	public void setReglaCompleta(String reglaCompleta) {
		this.reglaCompleta = reglaCompleta;
	}
	public KieContainer getKieContainer() {
		return kieContainer;
	}
	public void setKieContainer(KieContainer kieContainer) {
		this.kieContainer = kieContainer;
	}
		
		
}