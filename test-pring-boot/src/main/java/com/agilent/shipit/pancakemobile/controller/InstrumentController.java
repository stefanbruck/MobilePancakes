package com.agilent.shipit.pancakemobile.controller;

import static com.agilent.shipit.pancakemobile.controller.ControllerConstants.BASE64_PREFIX;
import static com.agilent.shipit.pancakemobile.controller.ControllerConstants.JSON_CONTENT_TYPE;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.agilent.shipit.pancakemobile.dao.InstrumentDAO;
import com.agilent.shipit.pancakemobile.entity.Instrument;
import com.agilent.shipit.pancakemobile.util.QRCodeUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
@RequestMapping(value = "instrument")
public class InstrumentController {

	@Autowired
	private InstrumentDAO dao;

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = JSON_CONTENT_TYPE)
	@ResponseStatus(value = HttpStatus.OK)
	public String list(HttpServletResponse response) {
		response.addHeader("Access-Control-Allow-Origin", "*");

		JsonArray jsonList = new JsonArray();
		Iterable<Instrument> allInstruments = dao.findAll();
		for (Instrument instrument : allInstruments) {
			JsonObject jsonItem = new JsonObject();
			jsonItem.addProperty("name", instrument.getName());
			jsonItem.addProperty("qrCode", "data:image/png;base64," + instrument.getQrCode());
			jsonList.add(jsonItem);
		}

		return jsonList.toString();
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public String register(@RequestParam(value = "name") String instrumentName, HttpServletResponse response) {
		if (dao.countByName(instrumentName) > 0) {
			throw new HTTPException(HttpStatus.CONFLICT.value());
		} else {
			try {
				Instrument instrument = new Instrument();
				instrument.setName(instrumentName);
				instrument.setQrCode(Base64.encodeBase64String(QRCodeUtils.generateQRCodeImage(instrumentName)));
				dao.save(instrument);

				response.addHeader("Access-Control-Allow-Origin", "*");

				JsonObject json = new JsonObject();
				json.addProperty("qrCode", BASE64_PREFIX + instrument.getQrCode());
				return json.toString();
			} catch (Exception e) {
				throw new HTTPException(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
		}
	}

}
