package org.acme.kogitoexample.repository;

import org.acme.kogitoexample.model.ActividadModel;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ActividadCustomDAOImpl implements ActividadCustomDAO {
	
	@PersistenceContext
	EntityManager entityManager;

	/*
	@Override
	public List<ActividadModel> findByTelefonia(Character activo) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ActividadModel> criteria = builder.createQuery(ActividadModel.class);
		Root<ActividadModel> actividad = criteria.from(ActividadModel.class);
		List<Predicate> predicados = new ArrayList<Predicate>();
		predicados.add(builder.equal(actividad.get("telefonia"), activo));
		criteria.select(actividad);
		criteria.where(builder.and(predicados.toArray(new Predicate[predicados.size()])));
		return entityManager.createQuery(criteria).getResultList();	
	}
	*/

	@Override
	public List<ActividadModel> findByCodAndNombreActivo(String codDesc, Character activo) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ActividadModel> criteria = builder.createQuery(ActividadModel.class);
		Root<ActividadModel> actividad = criteria.from(ActividadModel.class);
		List<Predicate> predicados = new ArrayList<Predicate>();
		Predicate predicateCodigo = builder.like(actividad.get("codigo"), "%" + codDesc.toUpperCase() + "%");
		Predicate predicateNombre = builder.like(actividad.get("nombre"), "%"  + codDesc.toUpperCase() + "%");

		predicados.add(builder.or(predicateCodigo,predicateNombre));
		predicados.add(builder.equal(actividad.get("activo"), activo));
		criteria.select(actividad);
		criteria.where(builder.and(predicados.toArray(new Predicate[predicados.size()])));
		return entityManager.createQuery(criteria).getResultList();	
	}

	@Override
	public List<ActividadModel> findByCodOrNombreAndActivo(String phrase, Character activo) {
		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ActividadModel> criteria = builder.createQuery(ActividadModel.class);
		Root<ActividadModel> root = criteria.from(ActividadModel.class);
		
		List<Predicate> predicados = new ArrayList<Predicate>();
		
		Predicate predicateCodigo = (builder.like(builder.upper(root.get("codigo")), "%" + phrase.toUpperCase() + "%"));
		Predicate predicateNombre = (builder.like(builder.upper(root.get("nombre")), "%" + phrase.toUpperCase() + "%"));
		
		//predicados.add(predicateCodigo);
		predicados.add(builder.or(predicateCodigo,predicateNombre));
		predicados.add(builder.equal(root.get("activo"), activo));
		criteria.select(root);
		criteria.where(builder.and(predicados.toArray(new Predicate[predicados.size()])));

		return entityManager.createQuery(criteria).getResultList();
	}

}
