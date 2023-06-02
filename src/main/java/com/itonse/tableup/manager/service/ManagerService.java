package com.itonse.tableup.manager.service;

import com.itonse.tableup.manager.domain.Partnership;
import com.itonse.tableup.manager.model.PartnershipInput;
import com.itonse.tableup.manager.model.AddRestaurantInput;
import com.itonse.tableup.manager.model.UpdateRestaurantInput;

import java.util.Optional;

public interface ManagerService {
    Boolean getIsRegisteredPartnership(PartnershipInput partnershipInput);
    Optional<Partnership> getPartnershipMember(String email, String password);
    Boolean getIsRegisteredRestaurant(AddRestaurantInput addRestaurantInput);
    void addPartnership(PartnershipInput partnershipInput);
    void addRestaurant(AddRestaurantInput addRestaurantInput, Partnership partnership);

    boolean CheckAuthorization(Long id, String partnershipEmail, String partnershipPassword);

    void UpdateRestaurant(UpdateRestaurantInput updateRestaurantInput, Long id);

    void DeleteRestaurant(Long id);
}
