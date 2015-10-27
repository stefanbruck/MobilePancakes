package com.agilent.shipit.pancakemobile.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agilent.shipit.pancakemobile.entity.Ingredient;

@Repository
public interface IngredientDAO extends CrudRepository<Ingredient, Integer> {
	List<Ingredient> findAllByRecipeId(Integer recipeId);

	Ingredient findOneByName(String name);
}
