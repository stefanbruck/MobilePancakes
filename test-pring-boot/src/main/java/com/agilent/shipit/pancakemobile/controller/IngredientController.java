package com.agilent.shipit.pancakemobile.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agilent.shipit.pancakemobile.dao.IngredientDAO;
import com.agilent.shipit.pancakemobile.entity.Ingredient;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
	@Autowired
	private IngredientDAO dao;

	@RequestMapping(value = "/list/{recipeId}", method = GET)
	public List<Ingredient> list(@PathVariable Integer recipeId) {
		return dao.findAllByRecipeId(recipeId);
	}
}
