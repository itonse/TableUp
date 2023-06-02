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
public class DeleteRestaurantInput {

    @NotBlank(message = "파트너쉽 이메일을 입력하세요.")
    private String partnershipEmail;  // 파트너쉽 이메일

    @NotBlank(message = "파트너쉽 패스워드를 입력하세요")
    private String partnershipPassword;
}
