package com.itonse.tableup.manager.service;

import com.itonse.tableup.manager.domain.Partnership;
import com.itonse.tableup.manager.dto.AddPartnership;
import com.itonse.tableup.manager.dto.AddRestaurantInput;
import com.itonse.tableup.manager.dto.DeletePartnership;

import java.util.Optional;

public interface ManagerService {
    boolean getIsRegisteredPartnership(String ownerName, String phone);

    Optional<Partnership> getPartnershipMember(String email, String password);

    boolean getIsRegisteredRestaurant(String restaurantName, String restaurantLocation);

    void addPartnership(AddPartnership addPartnership);

    int getRestaurantCount(Partnership partnership);

    void deletePartnership(DeletePartnership deletePartnership);

    void addRestaurant(AddRestaurantInput addRestaurantInput, Partnership partnership);

    boolean checkAuthorization(Long id, String partnershipEmail, String partnershipPassword);

    void updateRestaurant(String restaurantName, String restaurantLocation, String restaurantDescription, Long id);

    void deleteRestaurant(Long id);
}
