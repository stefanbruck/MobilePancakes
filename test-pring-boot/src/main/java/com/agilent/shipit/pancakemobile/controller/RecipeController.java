package com.agilent.shipit.pancakemobile.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	@RequestMapping(value = "/list", method = GET, produces = "application/json;charset=UTF-8")
	@ResponseStatus(value = HttpStatus.OK)
	public String list(HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");

		JsonArray jsonList = new JsonArray();

		for (Recipe recipe : dao.findAll()) {
			JsonObject jsonItem = new JsonObject();
			jsonItem.addProperty("name", recipe.getName());
			jsonItem.addProperty("qrCode", "data:image/png;base64," + recipe.getQrCode());
			jsonList.add(jsonItem);
		}

		return jsonList.toString();
	}

	@RequestMapping(value = "/load", method = POST, produces = "application/json;charset=UTF-8")
	@ResponseStatus(value = HttpStatus.OK)
	public String load(@RequestBody String qrCode, HttpServletResponse response) {
		try {
			String name = QRCodeUtils.readQRCode(Base64.decodeBase64(qrCode));
			Recipe recipe = dao.findOneByName(name);

			response.addHeader("Access-Control-Allow-Origin", "*");

			JsonObject jsonItem = new JsonObject();
			jsonItem.addProperty("name", recipe.getName());
			jsonItem.addProperty("content", recipe.getText());
			return jsonItem.toString();
		} catch (Exception e) {
			throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}

	@RequestMapping(value = "/save", method = POST)
	@ResponseStatus(value = HttpStatus.OK)
	public String save(@RequestBody String payload, HttpServletResponse response) {
		JsonParser parser = new JsonParser();
		JsonObject jsonRecipe = parser.parse(payload).getAsJsonObject();
		String name = jsonRecipe.get("name").getAsString();

		if (dao.countByName(name) > 0) {
			throw new HTTPException(HttpStatus.CONFLICT.value());
		} else {
			try {
				Recipe recipe = new Recipe();
				recipe.setName(name);
				recipe.setText("Directions");
				String encodeBase64String = Base64.encodeBase64String(QRCodeUtils.generateQRCodeImage(name));
				recipe.setQrCode(encodeBase64String);

				JsonArray ingredients = jsonRecipe.get("ingredients").getAsJsonArray();
				List<Ingredient> list = new ArrayList<Ingredient>();
				for (int i = 0, iL = ingredients.size(); i < iL; i++) {
					JsonObject jsonIngredient = ingredients.get(i).getAsJsonObject();
					Ingredient ingredient = new Ingredient();
					ingredient.setName(jsonIngredient.get("name").getAsString());
					String qrCode = Base64.encodeBase64String(QRCodeUtils.generateQRCodeImage(ingredient.getName()));
					ingredient.setQrCode(qrCode);
					list.add(ingredientDAO.save(ingredient));
				}
				recipe.setIngredients(list);

				dao.save(recipe);

				response.addHeader("Access-Control-Allow-Origin", "*");

				JsonObject json = new JsonObject();
				json.addProperty("qrCode", "data:image/png;base64," + recipe.getQrCode());
				return json.toString();
			} catch (Exception e) {
				e.printStackTrace();
				throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
		}
	}
}