package com.swasth.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swasth.dao.TestDao;
import com.swasth.pojos.Tests;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class TestServiceImpl implements TestService {

    private final TestDao testDao;

    @Autowired
    public TestServiceImpl(TestDao testDao) {
        this.testDao = testDao;
    }

    @Override
    public boolean addTests(Tests test) {
        try {
            validateTest(test);
            testDao.save(test);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Test details violate database constraints", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add test", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Tests readTestsDetails(int id) {
        return testDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Test not found with ID: " + id));
    }

    @Override
    public Tests updateTestsDetails(Tests test) {
        try {
            validateTest(test);
            if (!testDao.existsById(test.getTestId())) {
                throw new EntityNotFoundException("Test not found with ID: " + test.getTestId());
            }
            Tests existingTest = testDao.findById(test.getTestId()).get();
            updateTestFields(existingTest, test);
            return testDao.save(existingTest);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Test details violate database constraints", e);
        }
    }

    @Override
    public boolean deleteTests(int id) {
        try {
            if (!testDao.existsById(id)) {
                return false;
            }
            testDao.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete test with ID: " + id, e);
        }
    }

    private void validateTest(Tests test) {
        if (test == null) {
            throw new IllegalArgumentException("Test cannot be null");
        }
        if (isNullOrEmpty(test.getTestName())) {
            throw new IllegalArgumentException("Test name is required");
        }
        if (test.getTestCost() == null || test.getTestCost().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Test cost must be a non-negative value");
        }
    }

    private void updateTestFields(Tests existingTest, Tests updatedTest) {
        if (!isNullOrEmpty(updatedTest.getTestName())) {
            existingTest.setTestName(updatedTest.getTestName());
        }
        if (!isNullOrEmpty(updatedTest.getTestDescription())) {
            existingTest.setTestDescription(updatedTest.getTestDescription());
        }
        if (updatedTest.getTestCost() != null) {
            existingTest.setTestCost(updatedTest.getTestCost());
        }
        if (!isNullOrEmpty(updatedTest.getTestCategory())) {
            existingTest.setTestCategory(updatedTest.getTestCategory());
        }
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
