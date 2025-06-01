package com.springbootcartapp.Dto;

import com.springbootcartapp.entities.CardItem;
import com.springbootcartapp.entities.Category;
import com.springbootcartapp.entities.CustomerCartItems;
import com.springbootcartapp.entities.SubCategory;

public class ResponseDto {
	private Integer id;
    private String title;
    private String description;
    private Integer stockQuantity;
    private Double price;
    private Double userRating;
    private Integer priority;
    private String imageName;
    private String imageType;
    private String imageData;
    private Integer customer_ItemId; 
    private Integer quantity;
    private CardItem cardItem;
    private CustomerCartItems customerCartItems;
    private Category category;
    private SubCategory subCategory;
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
	public Integer getCustomer_ItemId() {
		return customer_ItemId;
	}
	public void setCustomer_ItemId(Integer customer_ItemId) {
		this.customer_ItemId = customer_ItemId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public ResponseDto() {
		super();
	}
	public CardItem getCardItem() {
		return cardItem;
	}
	public void setCardItem(CardItem cardItem) {
		this.cardItem = cardItem;
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
	
	public CustomerCartItems getCustomerCartItems() {
		return customerCartItems;
	}
	public void setCustomerCartItems(CustomerCartItems customerCartItems) {
		this.customerCartItems = customerCartItems;
	}
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public SubCategory getSubCategory() {
		return subCategory;
	}
	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}
	public ResponseDto(Integer id, String title, String description, Integer stockQuantity, Double price,
			Double userRating, Integer priority, String imageName, String imageType, String imageData,
			Integer customer_ItemId, Integer quantity, CardItem cardItem, CustomerCartItems customerCartItems,
			Category category, SubCategory subCategory) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.stockQuantity = stockQuantity;
		this.price = price;
		this.userRating = userRating;
		this.priority = priority;
		this.imageName = imageName;
		this.imageType = imageType;
		this.imageData = imageData;
		this.customer_ItemId = customer_ItemId;
		this.quantity = quantity;
		this.cardItem = cardItem;
		this.customerCartItems = customerCartItems;
		this.category = category;
		this.subCategory = subCategory;
	}
	
    

}
