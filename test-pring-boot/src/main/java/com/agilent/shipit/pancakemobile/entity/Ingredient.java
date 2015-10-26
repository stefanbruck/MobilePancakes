package com.agilent.shipit.pancakemobile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Ingredient {
	@Id
	@GeneratedValue
	@Column(name = "intgredient_id")
	private Integer id;

	@Column(name = "intgredient_name")
	private String name;

	@Column(name = "qr_code")
	private byte[] qrCode;

	@Column(name = "recipe_id")
	private Integer recipeId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getQrCode() {
		return qrCode;
	}

	public void setQrCode(byte[] qrCode) {
		this.qrCode = qrCode;
	}

	public Integer getRecipeId() {
		return recipeId;
	}

	public void getRecipeId(Integer recipeId) {
		this.recipeId = recipeId;
	}
}