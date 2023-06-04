package com.itonse.tableup.manager.service;

import com.itonse.tableup.manager.domain.Partnership;
import com.itonse.tableup.manager.dto.PartnershipInputDto;
import com.itonse.tableup.manager.dto.AddRestaurantInputDto;

import java.util.Optional;

public interface ManagerService {
    Boolean getIsRegisteredPartnership(String ownerName, String phone);
    Optional<Partnership> getPartnershipMember(String email, String password);
    Boolean getIsRegisteredRestaurant(String restaurantName, String restaurantLocation);
    void addPartnership(PartnershipInputDto partnershipInputDto);
    void addRestaurant(AddRestaurantInputDto addRestaurantInputDto, Partnership partnership);
    boolean checkAuthorization(Long id, String partnershipEmail, String partnershipPassword);
    void updateRestaurant(String restaurantName, String restaurantLocation, String restaurantDescription, Long id);
    void deleteRestaurant(Long id);
}
