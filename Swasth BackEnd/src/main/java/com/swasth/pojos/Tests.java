package com.swasth.pojos;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="tests")
@Getter
@Setter
@ToString
public class Tests {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="test_id")
	private int testId;
	
	@Column(name="test_name",length=100,nullable=false) 
	private String testName;
	
	@Column(name="test_description", columnDefinition = "TEXT") 
	private String testDescription;

	@Column(name="test_cost",precision = 10, scale = 2)
	private BigDecimal testCost;
	
	@Column(name="test_category",length=100)
	private String testCategory;
	
	@Column(name="created_at")
	@CreationTimestamp	
	private LocalDate createdAt;
	//	@ManyToOne
//	@JoinColumn(name="patient_id")
//	private Patient patient;

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTestDescription() {
		return testDescription;
	}

	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}

	public BigDecimal getTestCost() {
		return testCost;
	}

	public void setTestCost(BigDecimal testCost) {
		this.testCost = testCost;
	}

	public String getTestCategory() {
		return testCategory;
	}

	public void setTestCategory(String testCategory) {
		this.testCategory = testCategory;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
}
