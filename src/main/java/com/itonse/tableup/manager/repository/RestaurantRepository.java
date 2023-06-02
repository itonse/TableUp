package com.itonse.tableup.manager.repository;

import com.itonse.tableup.manager.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    boolean existsRestaurantByRestaurantNameAndRestaurantLocation(String restaurantName, String restaurantLocation);
}
