package com.itonse.tableup.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateRestaurantInput {
    @Nullable
    private String restaurantName;   // 매장명

    @Nullable
    private String restaurantLocation;     // 매장위치

    @Nullable
    private String restaurantDescription;    // 매장설명

    @NotBlank(message = "파트너쉽 이메일을 입력하세요.")
    private String partnershipEmail;  // 파트너쉽 이메일

    @NotBlank(message = "파트너쉽 패스워드를 입력하세요")
    private String partnershipPassword;  // 파트너쉽 패스워드
}
