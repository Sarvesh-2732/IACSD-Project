package com.swasth.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.swasth.pojos.Tests;


public interface TestDao extends JpaRepository<Tests, Integer> {

}
