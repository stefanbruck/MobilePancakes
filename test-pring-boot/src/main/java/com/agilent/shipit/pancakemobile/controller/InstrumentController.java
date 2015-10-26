package com.agilent.shipit.pancakemobile.controller;

import java.nio.charset.StandardCharsets;

import javax.xml.ws.http.HTTPException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agilent.shipit.pancakemobile.dao.InstrumentDAO;
import com.agilent.shipit.pancakemobile.entity.Instrument;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	@ResponseStatus(value=HttpStatus.CREATED)
	public String register(@RequestParam(value="name") String instrumentName) {
		if (dao.countByName(instrumentName) > 0) {
			throw new HTTPException(HttpStatus.CONFLICT.value());
		} else {
			Instrument instrument = new Instrument();
			instrument.setName(instrumentName);
			instrument.setQrCode("qrCode".getBytes(StandardCharsets.UTF_8));
			return Base64.encodeBase64String(instrument.getQrCode());
		}
	}

}
