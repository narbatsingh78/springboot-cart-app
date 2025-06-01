package com.springbootcartapp.Dto;

import java.util.Arrays;

import com.springbootcartapp.entities.CardItem;
import com.springbootcartapp.entities.CustomerCartItems;

public class RequestDto {
	
	  	private Integer id;
	    private String title;
	    private String description;
	    private Integer stockQuantity;
	    private Double price;
	    private Double userRating;
	    private Integer priority=0;
	    private String imageName;
	    private String imageType;
	    private String imageData;
	    private CardItem cardItem;
	    private CustomerCartItems customerCartItems;
		public RequestDto() {
			super();
			// TODO Auto-generated constructor stub
		}
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Integer getStockQuantity() {
			return stockQuantity;
		}
		public void setStockQuantity(Integer stockQuantity) {
			this.stockQuantity = stockQuantity;
		}
		public Double getPrice() {
			return price;
		}
		public void setPrice(Double price) {
			this.price = price;
		}
		public Double getUserRating() {
			return userRating;
		}
		public void setUserRating(Double userRating) {
			this.userRating = userRating;
		}
		public Integer getPriority() {
			return priority;
		}
		public void setPriority(Integer priority) {
			this.priority = priority;
		}
		public String getImageName() {
			return imageName;
		}
		public void setImageName(String imageName) {
			this.imageName = imageName;
		}
		public String getImageType() {
			return imageType;
		}
		public void setImageType(String imageType) {
			this.imageType = imageType;
		}
		public String getImageData() {
			return imageData;
		}
		public void setImageData(String imageData) {
			this.imageData = imageData;
		}
		public CardItem getCardItem() {
			return cardItem;
		}
		public void setCardItem(CardItem cardItem) {
			this.cardItem = cardItem;
		}
		public CustomerCartItems getCustomerCartItems() {
			return customerCartItems;
		}
		public void setCustomerCartItems(CustomerCartItems customerCartItems) {
			this.customerCartItems = customerCartItems;
		}
		@Override
		public String toString() {
			return "RequestDto [id=" + id + ", title=" + title + ", description=" + description + ", stockQuantity="
					+ stockQuantity + ", price=" + price + ", userRating=" + userRating + ", priority=" + priority
					+ ", imageName=" + imageName + ", imageType=" + imageType + ", imageData="
					 + ", cardItem=" + cardItem + ", customerCartItems=" + customerCartItems.getCustomer_ItemId()
					+ "]";
		}
	    
	    
	    
    
    
	
	
	
	
    
    

}
