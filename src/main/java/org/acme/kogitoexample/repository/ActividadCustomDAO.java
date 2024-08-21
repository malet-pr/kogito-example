package org.acme.kogitoexample.repository;

import org.acme.kogitoexample.model.ActividadModel;

import java.util.List;

public interface ActividadCustomDAO {

    // public List<ActividadModel> findByTelefonia(Character activo);
    public List<ActividadModel> findByCodAndNombreActivo(String codDesc, Character activo);
    public List<ActividadModel> findByCodOrNombreAndActivo(String phrase, Character activo);

}
