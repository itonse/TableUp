package com.itonse.tableup.customer.service;

import com.itonse.tableup.customer.domain.Reservation;
import com.itonse.tableup.customer.dto.MembershipInput;
import com.itonse.tableup.customer.dto.ReservationInput;
import com.itonse.tableup.customer.dto.RestaurantResponse;
import com.itonse.tableup.manager.domain.Restaurant;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerService {

    boolean getIsRegisteredMembership(String phone, String userName);

    void addMembership(MembershipInput membershipInput);

    boolean checkDeleteAuthorization(Long id, String membershipEmail, String membershipPassword);

    void deleteMembership(Long id);

    RestaurantResponse getRestaurant(String restaurantName);

    Page<Restaurant> getRestaurantPageSortedByName(int page);

    Page<Restaurant> getRestaurantPageSortedByStar(int page);

    List<RestaurantResponse> PageToList(Page<Restaurant> paging);

    boolean getLoginResult(String userName, String password);

    boolean checkAlreadyReservedTime(String restaurantName, LocalDateTime localDateTime);

    void reserveRestaurant(ReservationInput reservationInput, LocalDateTime localDateTime);

    boolean CancelReservation(String userName, LocalDateTime localDateTime);

    Reservation KioskVisitService(String userName, String dateTime);

    boolean checkReviewWriteAuthorization(String phone, String password, String dateTime);

    int writeReview(String restaurantName, String phone, String reviewContent, String dateTime, int star);

    void updateStar(int star, String restaurantName);

    boolean isReviewWritten(String phone, String dateTime);
}
