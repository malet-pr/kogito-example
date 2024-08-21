package org.acme.kogitoexample.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.acme.util.GsonExclude;

@Entity
@Table(name = "CT_ORDEN_TRABAJO")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class OrdenTrabajoModel extends BaseTimeStamp implements BaseObject<Long, String> {

    @Id
    @Column(name = "ID_ORDEN_TRABAJO", nullable = false)
    @SequenceGenerator(name = "seq_orden_trabajo", sequenceName = "CTS_ORDEN_TRABAJO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_orden_trabajo")
    private Long id;

    @Column(name = "NRO_OT")
    private String nroOT;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ORDEN_TRABAJO")
    @JsonManagedReference
    @GsonExclude
    private List<OrdenTrabajoActividadModel> actividades;

    @Column(name = "DOMI_DESC_LOCALIDAD")
    private String domiDescLocalidad;

    @Column(name = "DOMI_DIRECCION")
    private String domiDireccion;

    @Column(name = "DOMI_PAIS")
    private String domiPais;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TAREA")
    private TareaModel tarea;

    @Column(name = "FECHA_CREACION_OT")
    private Date fechaCreacionOt;

    @Column(name = "FECHA_CIERRE_OT")
    private Date fechaCierreOt;

    @Column(name = "ID_CLIENTE")
    private String idCliente;

    @Column(name = "TIPO_CLIENTE")
    private String tipoCliente;

    @ManyToOne
    @JoinColumn(name = "ID_REGLA_FLUJO")
    private ReglaFlujo reglaFlujo;

    @Override
    public String toString() {
        return nroOT;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNroOT() {
        return nroOT;
    }

    public void setNroOT(String nroOT) {
        this.nroOT = nroOT;
    }

    public List<OrdenTrabajoActividadModel> getActividades() {
        return actividades;
    }

    public void setActividades(List<OrdenTrabajoActividadModel> actividades) {
        this.actividades = actividades;
    }

    public String getDomiDescLocalidad() {
        return domiDescLocalidad;
    }

    public void setDomiDescLocalidad(String domiDescLocalidad) {
        this.domiDescLocalidad = domiDescLocalidad;
    }

    public String getDomiDireccion() {
        return domiDireccion;
    }

    public void setDomiDireccion(String domiDireccion) {
        this.domiDireccion = domiDireccion;
    }

    public String getDomiPais() {
        return domiPais;
    }

    public void setDomiPais(String domiPais) {
        this.domiPais = domiPais;
    }

    public TareaModel getTarea() {
        return tarea;
    }

    public void setTarea(TareaModel tarea) {
        this.tarea = tarea;
    }

    public Date getFechaCreacionOt() {
        return fechaCreacionOt;
    }

    public void setFechaCreacionOt(Date fechaCreacionOt) {
        this.fechaCreacionOt = fechaCreacionOt;
    }

    public Date getFechaCierreOt() {
        return fechaCierreOt;
    }

    public void setFechaCierreOt(Date fechaCierreOt) {
        this.fechaCierreOt = fechaCierreOt;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public ReglaFlujo getReglaFlujo() {
        return reglaFlujo;
    }

    public void setReglaFlujo(ReglaFlujo reglaFlujo) {
        this.reglaFlujo = reglaFlujo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdenTrabajoModel that)) return false;
        return Objects.equals(nroOT, that.nroOT) && Objects.equals(actividades, that.actividades) && Objects.equals(domiDescLocalidad, that.domiDescLocalidad) && Objects.equals(domiDireccion, that.domiDireccion) && Objects.equals(domiPais, that.domiPais) && Objects.equals(tarea, that.tarea) && Objects.equals(idCliente, that.idCliente) && Objects.equals(tipoCliente, that.tipoCliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nroOT, domiDireccion, domiPais, idCliente);
    }
}
