package com.itonse.tableup.manager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddRestaurantInput {

    @NotBlank(message = "매장명은 필수 입력 항목입니다.")
    private String restaurantName;   // 매장명

    @NotBlank(message = "매장위치는 필수 입력 항목입니다.")
    private String restaurantLocation;     // 매장위치

    private String restaurantDescription;    // 매장설명

    private String partnershipEmail;  // 파트너쉽 이메일

    private String partnershipPassword;  // 파트너쉽 패스워드
}
