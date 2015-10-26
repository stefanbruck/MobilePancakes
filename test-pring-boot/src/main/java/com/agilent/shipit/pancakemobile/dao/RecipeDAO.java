package com.agilent.shipit.pancakemobile.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agilent.shipit.pancakemobile.entity.Recipe;

@Repository
public interface RecipeDAO extends CrudRepository<Recipe, Integer> {
	Recipe findOneByName(String name);

	int countByName(String name);
}