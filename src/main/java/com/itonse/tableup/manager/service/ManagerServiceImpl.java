package com.itonse.tableup.manager.service;

import com.itonse.tableup.manager.domain.Partnership;
import com.itonse.tableup.manager.domain.Restaurant;
import com.itonse.tableup.manager.model.PartnershipInput;
import com.itonse.tableup.manager.model.AddRestaurantInput;
import com.itonse.tableup.manager.repository.PartnershipRepository;
import com.itonse.tableup.manager.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

    private final PartnershipRepository partnershipRepository;
    private final RestaurantRepository restaurantRepository;


    @Override
    public Boolean getIsRegisteredPartnership(PartnershipInput partnershipInput) {

        return partnershipRepository.existsPartnershipByPhoneAndOwnerName(
                partnershipInput.getPhone(), partnershipInput.getOwnerName());
    }

    @Override
    public Optional<Partnership> getPartnershipMember(String email, String password) {
        Optional<Partnership> partnership =
                partnershipRepository.findPartnershipByEmailAndPassword(
                        email, password);

        return partnership;
    }

    @Override
    public Boolean getIsRegisteredRestaurant(AddRestaurantInput addRestaurantInput) {

        return restaurantRepository.existsRestaurantByRestaurantNameAndRestaurantLocation(
                addRestaurantInput.getRestaurantName(), addRestaurantInput.getRestaurantLocation());
    }

    @Override
    public void addPartnership(PartnershipInput partnershipInput) {
        Partnership partnership = Partnership.builder()
                .email(partnershipInput.getEmail())
                .password(partnershipInput.getPassword())
                .ownerName(partnershipInput.getOwnerName())
                .phone(partnershipInput.getPhone())
                .build();

        partnershipRepository.save(partnership);
    }

    @Override
    public void addRestaurant(AddRestaurantInput addRestaurantInput, Partnership partnership) {
        Restaurant restaurant = Restaurant.builder()
                .restaurantName(addRestaurantInput.getRestaurantName())
                .restaurantLocation(addRestaurantInput.getRestaurantLocation())
                .restaurantDescription(addRestaurantInput.getRestaurantDescription())
                .partnership(partnership)
                .build();

        restaurantRepository.save(restaurant);
    }
}
