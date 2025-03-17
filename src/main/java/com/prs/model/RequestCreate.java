package com.prs.model;

import java.time.LocalDate;

public class RequestCreate {

	private int userId;
	private String description;
	private String justification;
	private String deliveryMode;
	private LocalDate DateNeeded;
	
	
	
	public RequestCreate() {
		super();
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getJustification() {
		return justification;
	}
	public void setJustification(String justification) {
		this.justification = justification;
	}
	public String getDeliveryMode() {
		return deliveryMode;
	}
	public void setDeliveryMode(String deliveryMode) {
		this.deliveryMode = deliveryMode;
	}
	public LocalDate getDateNeeded() {
		return DateNeeded;
	}
	public void setDateNeeded(LocalDate dateNeeded) {
		DateNeeded = dateNeeded;
	}
	@Override
	public String toString() {
		return "RequestCreate [userId=" + userId + ", description=" + description + ", justification=" + justification
				+ ", deliveryMode=" + deliveryMode + ", DateNeeded=" + DateNeeded + "]";
	}
	
	
	
}