package com.springboot.ecommerce.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/*this is Training type*/
@Entity
@Table(name = "TrainingType")
public class TrainingType {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "name")
	private String name;

	@Column(name = "status")
	private boolean status;

	@Column(name = "discriptions")
	private String discriptions;

	@Column(name = "trainingDate")
	private String trainingDate;

	@OneToMany(fetch = FetchType.EAGER)
	@JsonBackReference
	@JsonIgnore
	@OrderBy("name")
	@JoinColumn(name = "id")
	private Set<TrainingVenue> venue = new HashSet<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getDiscriptions() {
		return discriptions;
	}

	public void setDiscriptions(String discriptions) {
		this.discriptions = discriptions;
	}

	public String getTrainingDate() {
		return trainingDate;
	}

	public void setTrainingDate(String trainingDate) {
		this.trainingDate = trainingDate;
	}

	public Set<TrainingVenue> getVenue() {
		return venue;
	}

	public void setVenue(Set<TrainingVenue> venue) {
		this.venue = venue;
	}
}
