package org.acme.kogitoexample.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.acme.kogitoexample.util.GsonExclude;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.util.Date;

import static org.acme.kogitoexample.util.Constante.SI;

@Entity
@Table(name = "CT_OT_ACTIVIDAD")
public class OrdenTrabajoActividadModel implements BaseObject<Long, String> {

	@Id
	@Column(name = "ID_OT_ACTIVIDAD", nullable=false)
	@SequenceGenerator(name="seq_orden_trabajo_activid", sequenceName="CTS_OT_ACTIVIDAD", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_orden_trabajo_activid")
	private Long id;

	@ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ID_ORDEN_TRABAJO")
	@JsonBackReference
	@GsonExclude
	@JsonIgnore
	private OrdenTrabajoModel ordenTrabajo;

	@ManyToOne
	@JoinColumn(name = "ID_ACTIVIDAD")
	private ActividadModel actividadModel;
	
	@Column(name = "CANTIDAD")
	private Long cantidad;
	
	@ManyToOne
	@JoinColumn(name = "ID_ACTIVIDAD_TIPO")
	private ActividadTipo actividadTipo;

	@Column(name = "ACTIVO")
	private Character activo;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_CREACION")
	private Date fechaCreacion;

	@ManyToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "ID_REGLA_TIPO")
	private ReglaTipo reglaTipo;

	@ManyToOne
	@JoinColumn(name = "ID_REGLA_APLICADA")
	private Regla reglaAplicada;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public OrdenTrabajoModel getOrdenTrabajo() {
		return ordenTrabajo;
	}

	public void setOrdenTrabajo(OrdenTrabajoModel ordenTrabajo) {
		this.ordenTrabajo = ordenTrabajo;
	}

	public ActividadModel getActividadModel() {
		return actividadModel;
	}

	public void setActividadModel(ActividadModel actividadModel) {
		this.actividadModel = actividadModel;
	}

	public Long getCantidad() {
		return cantidad;
	}

	public void setCantidad(Long cantidad) {
		this.cantidad = cantidad;
	}

	public ActividadTipo getActividadTipo() {
		return actividadTipo;
	}

	public void setActividadTipo(ActividadTipo actividadTipo) {
		this.actividadTipo = actividadTipo;
	}

	public Character getActivo() {
		return activo;
	}

	public void setActivo(Character activo) {
		this.activo = activo;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacionCT) {
		this.fechaCreacion = fechaCreacion;
	}

	public ReglaTipo getReglaTipo() {
		return reglaTipo;
	}

	public void setReglaTipo(ReglaTipo reglaTipo) {
		this.reglaTipo = reglaTipo;
	}

	public Regla getReglaAplicada() {
		return reglaAplicada;
	}

	public void setReglaAplicada(Regla reglaAplicada) {
		this.reglaAplicada = reglaAplicada;
	}

	@Override
	public OrdenTrabajoActividadModel clone() throws CloneNotSupportedException {
		OrdenTrabajoActividadModel clon = new OrdenTrabajoActividadModel();
		Date fechaClon = new Date();
		clon.setActivo(SI);
		clon.setId(null);
		clon.setActividadModel(this.actividadModel);
		clon.setActividadTipo(this.actividadTipo);
		clon.setCantidad(this.cantidad);
		clon.setFechaCreacion(fechaClon);
		clon.setOrdenTrabajo(this.ordenTrabajo);
		clon.setReglaTipo(this.reglaTipo);
		clon.setReglaAplicada(this.reglaAplicada);
		return clon;
	}

}