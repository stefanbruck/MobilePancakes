package com.agilent.shipit.pancakemobile.controller;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agilent.shipit.pancakemobile.dao.InstrumentDAO;
import com.agilent.shipit.pancakemobile.entity.Instrument;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(value = "instrument")
public class InstrumentController {

	@Autowired
	private InstrumentDAO dao;

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseStatus(value=HttpStatus.OK)
	public String list() {
		JsonArray jsonAllInstrument = new JsonArray();
		Iterable<Instrument> allInstruments = dao.findAll();
		for (Instrument instrument : allInstruments) {
			JsonObject jsonInstrument = new JsonObject();
			jsonInstrument.addProperty("name", instrument.getName());
			jsonInstrument.addProperty("qrCode", Base64.encodeBase64String(instrument.getQrCode()));
			jsonAllInstrument.add(jsonInstrument);
		}
		
		return jsonAllInstrument.toString();
	}

}
