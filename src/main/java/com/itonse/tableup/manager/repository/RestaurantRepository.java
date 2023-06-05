package com.itonse.tableup.manager.repository;

import com.itonse.tableup.manager.domain.Partnership;
import com.itonse.tableup.manager.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    boolean existsRestaurantByRestaurantNameAndRestaurantLocation(String restaurantName, String restaurantLocation);

    int countByPartnership(Partnership partnership);

    Optional<Restaurant> findById(Long id);

    Optional<Restaurant> findByRestaurantName(String restaurantName);

    Page<Restaurant> findAllByOrderByRestaurantNameAsc(Pageable pageable);

    Page<Restaurant> findAllByOrderByStarDesc(Pageable pageable);
}
