package com.agilent.shipit.pancakemobile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Ingredient {
	@Id
	@GeneratedValue
	@Column(name = "ingredient_id")
	private Integer id;

	@Column(name = "ingredient_name")
	private String name;

	@Column(name = "qr_code", length=2000)
	private String qrCode;

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

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
}