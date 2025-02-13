package com.swasth.services;

import com.swasth.pojos.Reports;

public interface ReportService{
	    
	boolean addReports(Reports report);
	
	Reports readReportsDetails(int id);
		
	Reports updateReportsDetails(Reports report);
	
		public boolean deleteReports(int id) ;
	
}
