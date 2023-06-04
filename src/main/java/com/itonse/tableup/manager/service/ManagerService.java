package com.itonse.tableup.manager.service;

import com.itonse.tableup.manager.domain.Partnership;
import com.itonse.tableup.manager.dto.PartnershipInputDto;
import com.itonse.tableup.manager.dto.AddRestaurantInputDto;
import com.itonse.tableup.manager.dto.UpdateRestaurantInputDto;

import java.util.Optional;

public interface ManagerService {
    Boolean getIsRegisteredPartnership(PartnershipInputDto partnershipInputDto);
    Optional<Partnership> getPartnershipMember(String email, String password);
    Boolean getIsRegisteredRestaurant(AddRestaurantInputDto addRestaurantInputDto);
    void addPartnership(PartnershipInputDto partnershipInputDto);
    void addRestaurant(AddRestaurantInputDto addRestaurantInputDto, Partnership partnership);
    boolean checkAuthorization(Long id, String partnershipEmail, String partnershipPassword);
    void updateRestaurant(UpdateRestaurantInputDto updateRestaurantInputDto, Long id);
    void deleteRestaurant(Long id);
}
