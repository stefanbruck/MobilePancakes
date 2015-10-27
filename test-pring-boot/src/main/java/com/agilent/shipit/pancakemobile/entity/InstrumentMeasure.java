package com.agilent.shipit.pancakemobile.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class InstrumentMeasure {
	@Id
	@Column(name = "instrument_values_id")
	@GeneratedValue
	private Integer id;

	@Column(name = "instrument_name")
	private String instrument;

	@Column(name = "value")
	private String value;

	@Column(name = "value_date")
	private Timestamp timestamp;

	@Column(name = "read")
	private Boolean read;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Boolean isRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}
}