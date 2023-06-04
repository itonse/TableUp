package com.itonse.tableup.manager.service;

import com.itonse.tableup.manager.domain.Partnership;
import com.itonse.tableup.manager.domain.Restaurant;
import com.itonse.tableup.manager.dto.PartnershipInputDto;
import com.itonse.tableup.manager.dto.AddRestaurantInputDto;
import com.itonse.tableup.manager.dto.UpdateRestaurantInputDto;
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
    public Boolean getIsRegisteredPartnership(String ownerName, String phone) {

        return partnershipRepository.existsPartnershipByPhoneAndOwnerName(
                ownerName, phone
        );
    }

    @Override
    public Optional<Partnership> getPartnershipMember(String email, String password) {
        Optional<Partnership> partnership =
                partnershipRepository.findPartnershipByEmailAndPassword(
                        email, password);

        return partnership;
    }

    @Override
    public Boolean getIsRegisteredRestaurant(String restaurantName, String restaurantLocation) {

        return restaurantRepository.existsRestaurantByRestaurantNameAndRestaurantLocation(
                restaurantName, restaurantLocation);
    }

    @Override
    public void addPartnership(PartnershipInputDto partnershipInputDto) {
        Partnership partnership = Partnership.builder()
                .email(partnershipInputDto.getEmail())
                .password(partnershipInputDto.getPassword())
                .ownerName(partnershipInputDto.getOwnerName())
                .phone(partnershipInputDto.getPhone())
                .build();

        partnershipRepository.save(partnership);
    }

    @Override
    public void addRestaurant(AddRestaurantInputDto addRestaurantInputDto, Partnership partnership) {
        Restaurant restaurant = Restaurant.builder()
                .restaurantName(addRestaurantInputDto.getRestaurantName())
                .restaurantLocation(addRestaurantInputDto.getRestaurantLocation())
                .restaurantDescription(addRestaurantInputDto.getRestaurantDescription())
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
    public void updateRestaurant(String restaurantName, String restaurantLocation, String restaurantDescription, Long id) {
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
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findById(id);

        restaurantRepository.delete(optionalRestaurant.get());
    }
}
