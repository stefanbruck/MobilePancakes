package com.agilent.shipit.pancakemobile.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
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
import com.google.zxing.NotFoundException;

@RestController
@RequestMapping("/recipe")
public class RecipeController {
	@Autowired
	private RecipeDAO dao;
	@Autowired
	private IngredientDAO ingredientDAO;

	@RequestMapping(value = "/list", method = GET, produces = "application/json;charset=UTF-8")
	@ResponseStatus(value = HttpStatus.OK)
	public String list() {
		JsonArray jsonList = new JsonArray();

		for (Recipe recipe : dao.findAll()) {
			JsonObject jsonItem = new JsonObject();
			jsonItem.addProperty("name", recipe.getName());
			jsonItem.addProperty("qrCode", Base64.encodeBase64String(recipe.getQrCode()));
			jsonList.add(jsonItem);
		}

		return jsonList.toString();
	}

	@RequestMapping(value = "/load", method = POST, produces = "application/json;charset=UTF-8")
	@ResponseStatus(value = HttpStatus.OK)
	public String load(@RequestBody String qrCode) throws NotFoundException, IOException {
		String name = QRCodeUtils.readQRCode(Base64.decodeBase64(qrCode));
		Recipe recipe = dao.findOneByName(name);

		JsonObject jsonItem = new JsonObject();
		jsonItem.addProperty("name", recipe.getName());
		jsonItem.addProperty("content", recipe.getText());
		return jsonItem.toString();
	}

	@RequestMapping(value = "/save", method = POST)
	@ResponseStatus(value = HttpStatus.OK)
	public String save(@RequestBody String name, @RequestBody String content, @RequestBody List<String> ingredients,
			HttpServletResponse response) {
		if (dao.countByName(name) > 0) {
			throw new HTTPException(HttpStatus.CONFLICT.value());
		} else {
			try {
				Recipe recipe = new Recipe();
				recipe.setName(name);
				recipe.setText(content);
				recipe.setQrCode(QRCodeUtils.generateQRCodeImage(name));

				List<Ingredient> list = new ArrayList<Ingredient>();
				for (String ingredient : ingredients) {
					Ingredient entity = new Ingredient();
					entity.setName(ingredient);
					entity.setQrCode(QRCodeUtils.generateQRCodeImage(ingredient));
					list.add(ingredientDAO.save(entity));
				}
				recipe.setIngredients(list);

				dao.save(recipe);

				response.addHeader("Access-Control-Allow-Origin", "*");
				JsonObject json = new JsonObject();
				json.addProperty("qrCode", "data:image/png;base64," + Base64.encodeBase64String(recipe.getQrCode()));
				return json.toString();
			} catch (Exception e) {
				throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
		}
	}
}