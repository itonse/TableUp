package com.itonse.tableup.customer.service;

import com.itonse.tableup.customer.domain.Reservation;
import com.itonse.tableup.customer.dto.MembershipInputDto;
import com.itonse.tableup.customer.dto.ReservationInputDto;
import com.itonse.tableup.customer.model.RestaurantResponse;
import com.itonse.tableup.manager.domain.Restaurant;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerService {

    Boolean getIsRegisteredMembership(MembershipInputDto membershipInputDto);

    void addMembership(MembershipInputDto membershipInputDto);

    boolean checkDeleteAuthorization(Long id, String membershipEmail, String membershipPassword);

    void deleteMembership(Long id);

    Page<Restaurant> getRestaurantPageSortedByName(int page);

    Page<Restaurant> getRestaurantPageSortedByStar(int page);

    List<RestaurantResponse> PageToList(Page<Restaurant> paging);

    boolean getLoginResult(ReservationInputDto reservationInputDto);

    void reserveRestaurant(ReservationInputDto reservationInputDto, LocalDateTime localDateTime);

    Reservation getKioskResult(String userName, String dateTime);
}
