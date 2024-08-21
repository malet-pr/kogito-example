package org.acme.kogitoexample.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public class BaseTimeStamp {
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHA_CREACION")
    protected Date fechaCreacion /*= new Date()*/;

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
