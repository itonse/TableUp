package com.itonse.tableup.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ReservationInputDto {

    @NotBlank(message = "이름은 필수항목 입니다.")
    private String userName;

    @NotBlank(message = "비밀번호는 필수항목 입니다.")
    private String password;

    @NotBlank(message = "매장명은 필수항목 입니다.")
    private String restaurantName;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", message = "올바른 날짜와 시간 형식 (yyyy-MM-dd HH:mm) 으로 입력해주세요!")
    @NotBlank(message = "원하시는 예약타임을 입력해주세요!")
    private String dateTime;
}
