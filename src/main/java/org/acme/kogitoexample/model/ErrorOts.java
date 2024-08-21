package org.acme.kogitoexample.model;

import javax.persistence.*;

//@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "CT_REGLA_OT_ERROR")
public class ErrorOts extends BaseTimeStamp implements BaseObject<Long, String> {
    @Id
    @Column(name = "ID_REGLA_OT_ERROR", nullable=false)
    @SequenceGenerator(name="seq_regla_ot_errorid", sequenceName="CTS_REGLA_OT_ERROR", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_regla_ot_errorid")
    private Long id;
    @Column(name = "ID_REGLA_TIPO")
    private Long idReglaTipo;
    @Column(name = "NRO_OT")
    private String nroOt;
    @Column(name = "MENSAJE_ERROR")
    private String mensajeError;
    @Column(name = "ESTADO")
    private String status;
    @Column(name = "CANTIDAD_REINTENTO")
    private Integer cantReintentos;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdReglaTipo() {
        return idReglaTipo;
    }

    public void setIdReglaTipo(Long idReglaTipo) {
        this.idReglaTipo = idReglaTipo;
    }

    public String getNroOt() {
        return nroOt;
    }

    public void setNroOt(String nroOt) {
        this.nroOt = nroOt;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCantReintentos() {
        return cantReintentos;
    }

    public void setCantReintentos(Integer cantReintentos) {
        this.cantReintentos = cantReintentos;
    }
}
