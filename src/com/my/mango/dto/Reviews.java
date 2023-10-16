package com.my.mango.dto;


public class Reviews {
	private int business_number;
	private String user_id;
	private String comments;
	private int rating;
	public Reviews(int business_number, String user_id, String comments, int rating) {
		this.business_number = business_number;
		this.user_id = user_id;
		this.comments = comments;
		this.rating = rating;
	}
	public int getBusiness_number() {
		return business_number;
	}
	public void setBusiness_number(int business_number) {
		this.business_number = business_number;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}

}
