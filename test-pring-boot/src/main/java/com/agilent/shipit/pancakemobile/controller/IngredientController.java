package com.agilent.shipit.pancakemobile.controller;

import static com.agilent.shipit.pancakemobile.controller.ControllerConstants.JSON_CONTENT_TYPE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agilent.shipit.pancakemobile.dao.IngredientDAO;
import com.agilent.shipit.pancakemobile.dao.RecipeDAO;
import com.agilent.shipit.pancakemobile.entity.Ingredient;
import com.agilent.shipit.pancakemobile.entity.Recipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
	@Autowired
	private IngredientDAO dao;
	@Autowired
	private RecipeDAO recipeDAO;

	@RequestMapping(value = "/list", method = GET, produces = JSON_CONTENT_TYPE)
	@ResponseStatus(value = HttpStatus.OK)
	public String list(HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");

		JsonArray jsonList = new JsonArray();

		for (Ingredient ingredient : dao.findAll()) {
			JsonObject jsonItem = new JsonObject();
			jsonItem.addProperty("name", ingredient.getName());
			jsonItem.addProperty("qrCode", ingredient.getQrCode());
			jsonList.add(jsonItem);
		}

		return jsonList.toString();
	}

	@RequestMapping(value = "/list/{recipeName}", method = GET, produces = JSON_CONTENT_TYPE)
	@ResponseStatus(value = HttpStatus.OK)
	public String list(@PathVariable String recipeName, HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");

		Recipe recipe = recipeDAO.findOneByName(recipeName);

		JsonArray jsonList = new JsonArray();

		for (Ingredient ingredient : dao.findAllByRecipeId(recipe.getId())) {
			JsonObject jsonItem = new JsonObject();
			jsonItem.addProperty("name", ingredient.getName());
			jsonItem.addProperty("qrCode", ingredient.getQrCode());
			jsonList.add(jsonItem);
		}

		return jsonList.toString();
	}
}
