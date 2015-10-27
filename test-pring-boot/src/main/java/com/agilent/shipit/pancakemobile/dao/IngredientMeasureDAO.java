package com.agilent.shipit.pancakemobile.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agilent.shipit.pancakemobile.entity.IngredientMeasure;

@Repository
public interface IngredientMeasureDAO extends CrudRepository<IngredientMeasure, Integer> {

	List<IngredientMeasure> findManyByRecipeId(Integer recipeId);

}
