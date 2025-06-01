package com.springbootcartapp.entities;

import javax.persistence.*;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

@Entity
@Table(name = "cardItem")
public class CardItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ExcelProperty("id")
	@CsvBindByName(column = "id")
    private Integer id;
    @ExcelProperty("title")
	@CsvBindByName(column = "title")
    private String title;
    
    @ExcelProperty("description")
	@CsvBindByName(column = "description")
    private String description;
    
    @ExcelProperty("stock_quantity")
	@CsvBindByName(column ="stock_quantity")
    private Integer stockQuantity;
    
    @ExcelProperty("price")
	@CsvBindByName(column = "price")
    private Double price;
    
    @ExcelProperty("user_rating")
	@CsvBindByName(column = "user_rating")
    private Double userRating=1.0;
    
    @ExcelProperty("priority")
	@CsvBindByName(column =  "priority")
    private Integer priority=0;
    
	@ExcelProperty("image_name")
	@CsvBindByName(column = "image_name")
    private String imageName;
	
	@ExcelProperty("image_path")
	@CsvBindByName(column = "image_path")
    private String imagePath;
	
	@ExcelProperty("product_launch_date")
	@CsvCustomBindByName(column = "product_launch_date", converter = LocalDateConverter.class)
    private LocalDate productLaunchDate;
	
//	@Transient
//	@ExcelProperty("subcategory_id")  // This is what gets read from Excel
//	 private Integer subCategoryId;
 
    @OneToMany(mappedBy = "cardItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<CustomerCartItems> customerCartItems; 
    
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", referencedColumnName = "id")
    @JsonIgnore
    @ExcelIgnore
    private SubCategory subCategory;
    
        
    public CardItem() {
        super();
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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Set<CustomerCartItems> getCustomerCartItems() {
        return customerCartItems;
    }

    public void setCustomerCartItems(Set<CustomerCartItems> customerCartItems) {
        this.customerCartItems = customerCartItems;
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


	public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}


	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public LocalDate getProductLaunchDate() {
		return productLaunchDate;
	}


	public void setProductLaunchDate(LocalDate productLaunchDate) {
		this.productLaunchDate = productLaunchDate;
	}
//	 public Integer getSubCategoryId() {
//	        return subCategoryId;
//	    }
//
//	    public void setSubCategoryId(Integer subCategoryId) {
//	        this.subCategoryId = subCategoryId;
//	    }

	
	
}
