package org.acme.kogitoexample.model;

import javax.persistence.*;


@Entity
@Table(name = "CT_TAREA")
public class TareaModel extends BaseTimeStamp implements BaseObject<Long, String> {

	@Id
	@Column(name = "ID_TAREA", nullable = false)
	@SequenceGenerator(name="seq_tarea", sequenceName="CTS_TAREA", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tarea")
	private Long id;

	@Column(name = "CODIGO", nullable = false)
	private String codigo;

	@Column(name = "NOMBRE", nullable = false)
	private String nombre;

	@Column(name = "FULFILLMENT", nullable = true)
	private Character fulfillment;

	@Column(name = "TIPO", nullable = true)
	private String tipoTarea;

	@Column(name = "SISTEMA_ORIGEN", nullable = true)
	private String sistemaOrigen;

	@Column(name = "ACTIVO", nullable = true)
	private Character activo;

	@Column(name = "TIPO_CLIENTE")
	private String tipoCliente;


	public String getIdTipoCliente() {
		return tipoCliente;
	}

	public void seTipoCliente(String idTipoCliente) {
		this.tipoCliente = idTipoCliente;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Character getFulfillment() {
		return fulfillment;
	}

	public void setFulfillment(Character fulfillment) {
		this.fulfillment = fulfillment;
	}

	public String getTipoTarea() {
		return tipoTarea;
	}

	public void setTipoTarea(String tipoTarea) {
		this.tipoTarea = tipoTarea;
	}

	public String getSistemaOrigen() {
		return sistemaOrigen;
	}

	public void setSistemaOrigen(String sistemaOrigen) {
		this.sistemaOrigen = sistemaOrigen;
	}

	public Character getActivo() {
		return activo;
	}

	public void setActivo(Character activo) {
		this.activo = activo;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {this.id = id;
	}

}
