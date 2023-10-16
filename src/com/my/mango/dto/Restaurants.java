package com.my.mango.dto;

public class Restaurants {

	private int business_number;
	private String restaurants_type;
	private String restaurants_name;
	private String status;
	private String address;
	private String user_id;
	public int getBusiness_number() {
		return business_number;
	}
	public void setBusiness_number(int business_number) {
		this.business_number = business_number;
	}
	public String getRestaurants_type() {
		return restaurants_type;
	}
	public void setRestaurants_type(String restaurants_type) {
		this.restaurants_type = restaurants_type;
	}
	public String getRestaurants_name() {
		return restaurants_name;
	}
	public void setRestaurants_name(String restaurants_name) {
		this.restaurants_name = restaurants_name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String adress) {
		this.address = adress;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}
