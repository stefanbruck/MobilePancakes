package com.agilent.shipit.pancakemobile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class IngredientMeasure {

	@Id
	@Column(name = "ingredient_measure_id")
	@GeneratedValue
	private Integer id;

	@Column(name = "recipe_id")
	private Integer recipeId;

	@Column(name = "ingredient_id")
	private Integer ingredientId;

	@Column(name = "value")
	private Double value;

	@Column(name = "unit")
	private String unit;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id", insertable = false, updatable = false)
	private Ingredient ingredient;

	public Ingredient getIngredient() {
		return ingredient;
	}

	public Integer getId() {
		return id;
	}

	public Integer getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Integer recipeId) {
		this.recipeId = recipeId;
	}

	public Integer getIngredientId() {
		return ingredientId;
	}

	public void setIngredientId(Integer ingredientId) {
		this.ingredientId = ingredientId;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
