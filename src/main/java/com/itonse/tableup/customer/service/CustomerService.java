package com.itonse.tableup.customer.service;

import com.itonse.tableup.customer.model.MembershipInput;
import com.itonse.tableup.customer.model.ReservationInput;
import com.itonse.tableup.customer.model.RestaurantResponse;
import com.itonse.tableup.manager.domain.Restaurant;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerService {

    Boolean getIsRegisteredMembership(MembershipInput membershipInput);

    void addMembership(MembershipInput membershipInput);

    boolean checkDeleteAuthorization(Long id, String membershipEmail, String membershipPassword);

    void deleteMembership(Long id);

    Page<Restaurant> getRestaurantPageSortedByName(int page);

    Page<Restaurant> getRestaurantPageSortedByStar(int page);

    List<RestaurantResponse> PageToList(Page<Restaurant> paging);

    boolean getLoginResult(ReservationInput reservationInput);

    void reserveRestaurant(ReservationInput reservationInput, LocalDateTime localDateTime);
}
