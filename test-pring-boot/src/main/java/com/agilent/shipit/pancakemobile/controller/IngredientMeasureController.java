package com.agilent.shipit.pancakemobile.controller;

import static com.agilent.shipit.pancakemobile.controller.ControllerConstants.JSON_CONTENT_TYPE;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agilent.shipit.pancakemobile.dao.IngredientDAO;
import com.agilent.shipit.pancakemobile.dao.IngredientMeasureDAO;
import com.agilent.shipit.pancakemobile.dao.RecipeDAO;
import com.agilent.shipit.pancakemobile.entity.Ingredient;
import com.agilent.shipit.pancakemobile.entity.IngredientMeasure;
import com.agilent.shipit.pancakemobile.entity.Recipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@RequestMapping(value = "/measures")
public class IngredientMeasureController {

	@Autowired
	private RecipeDAO recipeDAO;

	@Autowired
	private IngredientDAO ingredientDAO;

	@Autowired
	private IngredientMeasureDAO ingredientMeasureDAO;

	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = JSON_CONTENT_TYPE)
	@ResponseStatus(value = HttpStatus.OK)
	public String save(@RequestParam(value = "recipeName") String recipeName,
			@RequestParam(value = "selectedIngredient") String selectedIngredient, @RequestParam Double value,
			@RequestParam String unit, HttpServletResponse response) {

		Recipe recipe = recipeDAO.findOneByName(recipeName);
		if (recipe == null) {
			throw new HTTPException(HttpStatus.NOT_FOUND.value());
		}

		IngredientMeasure ingredientMeasure = new IngredientMeasure();

		Ingredient ingredient = ingredientDAO.findOneByName(selectedIngredient);
		if (ingredient == null) {
			throw new HTTPException(HttpStatus.NOT_FOUND.value());
		}
		ingredientMeasure.setIngredientId(ingredient.getId());
		ingredientMeasure.setRecipeId(recipe.getId());
		ingredientMeasure.setValue(value);
		ingredientMeasure.setUnit(unit);
		ingredientMeasureDAO.save(ingredientMeasure);

		response.addHeader("Access-Control-Allow-Origin", "*");

		JsonObject result = new JsonObject();
		result.addProperty("success", Boolean.TRUE);
		return result.toString();
	}

	@RequestMapping(value = "/load/{recipeName}", method = RequestMethod.GET, produces = JSON_CONTENT_TYPE)
	private String load(@PathVariable String recipeName) {
		JsonObject jsonMeasures = new JsonObject();

		Recipe recipe = recipeDAO.findOneByName(recipeName);
		if (recipe == null) {
			throw new HTTPException(HttpStatus.NOT_FOUND.value());
		}

		JsonArray measureList = new JsonArray();
		List<IngredientMeasure> measures = ingredientMeasureDAO.findManyByRecipeId(recipe.getId());
		for (IngredientMeasure measure : measures) {
			if (measure.getIngredient() == null) {
				throw new HTTPException(HttpStatus.NOT_FOUND.value());
			}
			JsonObject jsonMeasure = new JsonObject();
			jsonMeasure.addProperty("ingredientName", measure.getIngredient().getName());
			jsonMeasure.addProperty("value", measure.getValue());
			jsonMeasure.addProperty("unit", measure.getUnit());
			measureList.add(jsonMeasure);
		}

		jsonMeasures.add("measures", measureList);
		return jsonMeasures.toString();
	}
}
