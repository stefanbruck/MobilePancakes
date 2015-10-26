package com.agilent.shipit.pancakemobile.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agilent.shipit.pancakemobile.entity.Instrument;

@Repository
public interface InstrumentDAO extends CrudRepository<Instrument, Integer>{

	@Query
	int countByName(String name);
}
