package com.itonse.tableup.customer.dto;

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

    private String restaurantName;   // 매장명
    private String restaurantLocation;     // 매장위치
    private String restaurantDescription;    // 매장설명
    private String Star;  // 매장평점

    public static RestaurantResponse of(Restaurant restaurant) {

        String strStar = "평점없음";
        if (restaurant.getStar() != null) {
            strStar = String.format("%.2f", restaurant.getStar());
        }

        return RestaurantResponse.builder()
                .restaurantName(restaurant.getRestaurantName())
                .restaurantLocation(restaurant.getRestaurantLocation())
                .restaurantDescription(restaurant.getRestaurantDescription())
                .Star(strStar)
                .build();
    }
}
