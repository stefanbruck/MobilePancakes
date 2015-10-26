package com.agilent.shipit.pancakemobile.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Recipe {
	@Id
	@GeneratedValue
	@Column(name = "recipe_id")
	private Integer id;

	@Column(name = "recipe_name")
	private String name;

	@Column(name = "recipe_text")
	private String text;

	@Column(name = "qr_code")
	private byte[] qrCode;

	@OneToMany
	@PrimaryKeyJoinColumn(name = "recipe_id")
	private List<Ingredient> ingredients;

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public byte[] getQrCode() {
		return qrCode;
	}

	public void setQrCode(byte[] qrCode) {
		this.qrCode = qrCode;
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
}
