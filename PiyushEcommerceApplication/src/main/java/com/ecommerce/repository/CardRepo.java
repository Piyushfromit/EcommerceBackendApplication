package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Card;

//import com.raghavEcomm.model.Card;

@Repository
public interface CardRepo extends JpaRepository<Card, Integer>{
	
	
}
