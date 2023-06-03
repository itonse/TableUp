package com.itonse.tableup.customer.model;

import com.itonse.tableup.manager.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RestaurantResponse {

    private Long id;
    private String restaurantName;   // 매장명
    private String restaurantLocation;     // 매장위치
    private String restaurantDescription;    // 매장설명
    private Double star;  // 매장평점

    public static RestaurantResponse of(Restaurant restaurant) {
        return RestaurantResponse.builder()
                .id(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
                .restaurantLocation(restaurant.getRestaurantLocation())
                .restaurantDescription(restaurant.getRestaurantDescription())
                .build();
    }
}
