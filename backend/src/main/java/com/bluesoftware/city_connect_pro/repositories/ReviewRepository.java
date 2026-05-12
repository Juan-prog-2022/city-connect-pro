package com.bluesoftware.city_connect_pro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluesoftware.city_connect_pro.entities.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}