package com.swasth.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swasth.pojos.Reports;

public interface ReportDao extends JpaRepository<Reports, Integer> {

}
