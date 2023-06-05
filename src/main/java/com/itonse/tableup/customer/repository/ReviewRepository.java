package com.itonse.tableup.customer.repository;

import com.itonse.tableup.customer.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    int countByRestaurantName(String restaurantName);

    boolean existsByPhoneNumberTailAndVisitDate(String phoneNumberTail, String visitDate);
}
