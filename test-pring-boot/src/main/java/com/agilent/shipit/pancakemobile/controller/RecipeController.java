package com.agilent.shipit.pancakemobile.controller;

import static com.agilent.shipit.pancakemobile.controller.ControllerConstants.BASE64_PREFIX;
import static com.agilent.shipit.pancakemobile.controller.ControllerConstants.JSON_CONTENT_TYPE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agilent.shipit.pancakemobile.dao.IngredientDAO;
import com.agilent.shipit.pancakemobile.dao.RecipeDAO;
import com.agilent.shipit.pancakemobile.entity.Ingredient;
import com.agilent.shipit.pancakemobile.entity.Recipe;
import com.agilent.shipit.pancakemobile.util.QRCodeUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
@RequestMapping("/recipe")
public class RecipeController {
	@Autowired
	private RecipeDAO dao;
	@Autowired
	private IngredientDAO ingredientDAO;

	@RequestMapping(value = "/list", method = GET, produces = JSON_CONTENT_TYPE)
	@ResponseStatus(value = HttpStatus.OK)
	public String list(HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");

		JsonArray jsonList = new JsonArray();

		for (Recipe recipe : dao.findAll()) {
			JsonObject jsonItem = new JsonObject();
			jsonItem.addProperty("name", recipe.getName());
			jsonItem.addProperty("qrCode", BASE64_PREFIX + recipe.getQrCode());
			jsonList.add(jsonItem);
		}

		return jsonList.toString();
	}

	@RequestMapping(value = "/{recipeName}/load", method = GET, produces = JSON_CONTENT_TYPE)
	@ResponseStatus(value = HttpStatus.OK)
	public String loadFromName(@PathVariable String recipeName, HttpServletResponse response) {
		try {
			return loadRecipeByName(response, recipeName);
		} catch (Exception e) {
			throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	@RequestMapping(value = "/load", method = POST, produces = JSON_CONTENT_TYPE)
	@ResponseStatus(value = HttpStatus.OK)
	public String load(@RequestBody String qrCode, HttpServletResponse response) {
		try {
			String name = extractRecipeName(parseRecipePayload(QRCodeUtils.readQRCode(Base64.decodeBase64(qrCode))));
			return loadRecipeByName(response, name);
		} catch (Exception e) {
			throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	@RequestMapping(value = "/save", method = POST, produces = JSON_CONTENT_TYPE)
	@ResponseStatus(value = HttpStatus.OK)
	public String save(@RequestBody String payload, HttpServletResponse response) {
		JsonObject jsonRecipe = parseRecipePayload(payload);
		String name = extractRecipeName(jsonRecipe);

		if (dao.countByName(name) > 0) {
			throw new HTTPException(HttpStatus.CONFLICT.value());
		} else {
			try {
				Recipe recipe = new Recipe();
				recipe.setName(name);
				recipe.setText(jsonRecipe.get("content").getAsString());
				recipe.setQrCode(Base64.encodeBase64String(QRCodeUtils.generateQRCodeImage(payload)));

				JsonArray ingredients = jsonRecipe.get("ingredients").getAsJsonArray();
				List<Ingredient> list = new ArrayList<Ingredient>();
				for (int i = 0, iL = ingredients.size(); i < iL; i++) {
					JsonObject jsonIngredient = ingredients.get(i).getAsJsonObject();
					Ingredient ingredient = new Ingredient();
					ingredient.setName(jsonIngredient.get("name").getAsString());
					ingredient.setQrCode(
							Base64.encodeBase64String(QRCodeUtils.generateQRCodeImage(ingredient.getName())));
					list.add(ingredientDAO.save(ingredient));
				}
				recipe.setIngredients(list);

				dao.save(recipe);

				response.addHeader("Access-Control-Allow-Origin", "*");

				JsonObject json = new JsonObject();
				json.addProperty("qrCode", BASE64_PREFIX + recipe.getQrCode());
				return json.toString();
			} catch (Exception e) {
				e.printStackTrace();
				throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
		}
	}

	private String extractRecipeName(JsonObject jsonRecipe) {
		return jsonRecipe.get("name").getAsString();
	}

	private JsonObject parseRecipePayload(String payload) {
		return new JsonParser().parse(payload).getAsJsonObject();
	}

	private String loadRecipeByName(HttpServletResponse response, String name) {
		Recipe recipe = dao.findOneByName(name);

		response.addHeader("Access-Control-Allow-Origin", "*");

		JsonObject jsonItem = new JsonObject();
		jsonItem.addProperty("name", recipe.getName());
		jsonItem.addProperty("content", recipe.getText());

		return jsonItem.toString();
	}
}