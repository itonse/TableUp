package com.itonse.tableup.customer.repository;

import com.itonse.tableup.customer.domain.Reservation;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByUserNameAndReservationTime(String userName, LocalDateTime localDateTime);

    Optional<Reservation> findByPhoneNumberTailAndReservationTime(String phoneNumberTail, LocalDateTime localDateTime);

    boolean existsByRestaurantNameAndReservationTime(String restaurantName, LocalDateTime reservationTime);
}
