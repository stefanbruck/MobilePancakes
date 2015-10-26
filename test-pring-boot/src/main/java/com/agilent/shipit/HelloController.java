package com.agilent.shipit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agilent.shipit.pancakemobile.dao.InstrumentDAO;
import com.agilent.shipit.pancakemobile.entity.Instrument;

@RestController
public class HelloController {
	
	@Autowired
	private InstrumentDAO dao;

	@RequestMapping("/")
	public String index() {
		Iterable<Instrument> findAll = dao.findAll();
		
		System.out.println(findAll);
		
		for (Instrument instrument : findAll) {
			System.out.println(instrument.getName());
		}
		
		return "Greetings from Spring Boot!";
	}

}