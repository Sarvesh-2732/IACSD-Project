package com.swasth.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swasth.dao.ReportDao;
import com.swasth.pojos.Reports;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {
    
    private final ReportDao reportDao;

    @Autowired
    public ReportServiceImpl(ReportDao reportDao) {
        this.reportDao = reportDao;
    }

    @Override
    public boolean addReports(Reports report) {
        try {
            validateReport(report);
            reportDao.save(report);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Report details violate database constraints", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add report", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Reports readReportsDetails(int id) {
        return reportDao.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Report not found with ID: " + id));
    }

    @Override
    public Reports updateReportsDetails(Reports report) {
        try {
            validateReport(report);
            
            if (!reportDao.existsById(report.getReportId())) {
                throw new EntityNotFoundException("Report not found with ID: " + report.getReportId());
            }

            Reports existingReport = reportDao.findById(report.getReportId()).get();
            updateReportFields(existingReport, report);
            
            return reportDao.save(existingReport);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Report details violate database constraints", e);
        }
    }

    @Override
    public boolean deleteReports(int id) {
        try {
            if (!reportDao.existsById(id)) {
                return false;
            }
            reportDao.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete report with ID: " + id, e);
        }
    }

    private void validateReport(Reports report) {
        if (report == null) {
            throw new IllegalArgumentException("Report cannot be null");
        }
        
        if (isNullOrEmpty(report.getNotes())) {
            throw new IllegalArgumentException("Report description is required");
        }
        
    }

    private void updateReportFields(Reports existingReport, Reports updatedReport) {
        if (!isNullOrEmpty(updatedReport.getNotes())) {
            existingReport.setNotes(updatedReport.getNotes());
        }        
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}