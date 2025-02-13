package com.swasth.services;

import com.swasth.pojos.Tests;

public interface TestService {
	boolean addTests(Tests test);
	
	Tests readTestsDetails(int id);
	
	Tests updateTestsDetails(Tests test);
	
	public boolean deleteTests(int id) ;
	
}
