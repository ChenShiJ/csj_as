package com.hzyc.csj.demo_10;



public class Goods {
	private int id;
	private String name;
	private double price;
	private double ratingBar;
	private String shops;
	private String photo;
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
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getRatingBar() {
		return ratingBar;
	}
	public void setRatingBar(double ratingBar) {
		this.ratingBar = ratingBar;
	}
	public String getShops() {
		return shops;
	}
	public void setShops(String shops) {
		this.shops = shops;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	@Override
	public String toString() {
		return "Goods [id=" + id + ", name=" + name + ", price=" + price + ", ratingBar=" + ratingBar + ", shops="
				+ shops + ", photo=" + photo + "]";
	}
	
}
