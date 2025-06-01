package com.springbootcartapp.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CustomerCartItems {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customer_ItemId;
    private Integer quantity;
    @Column(nullable = false)
    private Integer cartItemStatus=0;
    
 
	@ManyToOne
    @JoinColumn(name = "card_item_id", nullable = false) 
    private CardItem cardItem;
	@ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;
    
    public CustomerCartItems() {
        super();
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

    public CardItem getCardItem() {
        return cardItem;
    }

    public void setCardItem(CardItem cardItem) {
        this.cardItem = cardItem;
    }
    public Integer getCartItemStatus() {
		return cartItemStatus;
	}

	public void setCartItemStatus(Integer cartItemStatus) {
		this.cartItemStatus = cartItemStatus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "CustomerCartItems [customer_ItemId=" + customer_ItemId + ", quantity=" + quantity + ", cartItemStatus="
				+ cartItemStatus + ", cardItem=" + cardItem + ", user=" + user + "]";
	}
	
}
