package com.itonse.tableup.manager.service;

import com.itonse.tableup.manager.domain.Partnership;
import com.itonse.tableup.manager.domain.Restaurant;
import com.itonse.tableup.manager.dto.AddPartnership;
import com.itonse.tableup.manager.dto.AddRestaurantInput;
import com.itonse.tableup.manager.dto.DeletePartnership;
import com.itonse.tableup.manager.repository.PartnershipRepository;
import com.itonse.tableup.manager.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ManagerServiceImpl implements ManagerService {

    private final PartnershipRepository partnershipRepository;
    private final RestaurantRepository restaurantRepository;


    @Override
    public boolean getIsRegisteredPartnership(String ownerName, String phone) {

        return partnershipRepository.existsByOwnerNameAndPhone(
                ownerName, phone
        );
    }

    @Override
    public Optional<Partnership> getPartnershipMember(String email, String password) {
        Optional<Partnership> optionalPartnership =
                partnershipRepository.findPartnershipByEmailAndPassword(
                        email, password);

        return optionalPartnership;
    }

    @Override
    public boolean getIsRegisteredRestaurant(String restaurantName, String restaurantLocation) {

        return restaurantRepository.existsRestaurantByRestaurantNameAndRestaurantLocation(
                restaurantName, restaurantLocation);
    }

    @Override
    public void addPartnership(AddPartnership addPartnership) {
        Partnership partnership = Partnership.builder()
                .email(addPartnership.getEmail())
                .password(addPartnership.getPassword())
                .ownerName(addPartnership.getOwnerName())
                .phone(addPartnership.getPhone())
                .build();

        partnershipRepository.save(partnership);
    }

    @Override
    public int getRestaurantCount(Partnership partnership) {

        return restaurantRepository.countByPartnership(partnership);
    }

    @Override
    public void deletePartnership(DeletePartnership deletePartnership) {
        Partnership partnership =
                partnershipRepository.findPartnershipByEmailAndPassword(
                        deletePartnership.getEmail(),
                        deletePartnership.getPassword()
                ).orElseThrow(() -> new RuntimeException("삭제 할 파트너쉽을 찾지 못했습니다."));

        partnershipRepository.delete(partnership);
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
                partnershipRepository.findPartnershipByEmailAndPassword(
                        partnershipEmail, partnershipPassword
                );

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
    public void updateRestaurant(String restaurantName, String restaurantLocation
            , String restaurantDescription, Long id) {

        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

        Restaurant restaurant = optionalRestaurant.get();

        if (restaurantName != null) {
            restaurant.setRestaurantName(restaurantName);
        }

        if (restaurantLocation != null) {
            restaurant.setRestaurantLocation(restaurantLocation);
        }

        if (restaurantDescription != null) {
            restaurant.setRestaurantDescription(restaurantDescription);
        }

        restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                        .orElseThrow(()
                                -> new NoSuchElementException("매장 정보가 없습니다."));

        restaurantRepository.delete(restaurant);
    }
}
