package com.agilent.shipit.pancakemobile.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agilent.shipit.pancakemobile.entity.InstrumentMeasure;

@Repository
public interface InstrumentMeasureDAO extends CrudRepository<InstrumentMeasure, Integer> {
	List<InstrumentMeasure> findAllByRead(Boolean read);
}
