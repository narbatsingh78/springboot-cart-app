package com.springbootcartapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springbootcartapp.entities.CardItem;
import com.springbootcartapp.entities.CustomerCartItems;
import com.springbootcartapp.entities.User;

@Repository
public interface AddInCartItemRepository extends JpaRepository<CustomerCartItems, Integer> {

	Optional<CustomerCartItems> findByUserAndCardItem(User user, CardItem cardItem);

	List<CustomerCartItems> findCartItemsByUserId(int userId);

}

//@Query("SELECT cci FROM CustomerCartItems cci JOIN cci.cardItem ci")
//List<CustomerCartItems> findAllCustomerCartItemsWithCardItems();
//Optional<CustomerCartItems> findByCardItem(CardItem cardItem);