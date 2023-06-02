package com.itonse.tableup.manager.service;

import com.itonse.tableup.manager.domain.Partnership;
import com.itonse.tableup.manager.domain.Restaurant;
import com.itonse.tableup.manager.model.PartnershipInput;
import com.itonse.tableup.manager.model.AddRestaurantInput;
import com.itonse.tableup.manager.model.UpdateRestaurantInput;
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

    @Override
    public boolean checkAuthorization(Long id, String partnershipEmail, String partnershipPassword) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);
        Optional<Partnership> optionalPartnership =
                partnershipRepository.findPartnershipByEmailAndPassword(partnershipEmail, partnershipPassword);

        if (!optionalRestaurant.isPresent() || !optionalPartnership.isPresent()) {
            return false;
        }

        String restaurantPartnerEmail = optionalRestaurant.get().getPartnership().getEmail();
        String partnerEmail = optionalPartnership.get().getEmail();

        if (!restaurantPartnerEmail.equals(partnerEmail)) {
            return false;
        }

        return true;
    }

    @Override
    public void updateRestaurant(UpdateRestaurantInput updateRestaurantInput, Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

        Restaurant restaurant = optionalRestaurant.get();

        if (updateRestaurantInput.getRestaurantName() != null) {
            restaurant.setRestaurantName(updateRestaurantInput.getRestaurantName());
        }

        if (updateRestaurantInput.getRestaurantLocation() != null) {
            restaurant.setRestaurantLocation(updateRestaurantInput.getRestaurantLocation());
        }

        if (updateRestaurantInput.getRestaurantDescription() != null) {
            restaurant.setRestaurantDescription(updateRestaurantInput.getRestaurantDescription());
        }

        restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long id) {
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

        restaurantRepository.delete(optionalRestaurant.get());
    }
}
