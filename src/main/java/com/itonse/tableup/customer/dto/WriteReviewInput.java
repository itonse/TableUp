package com.itonse.tableup.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WriteReviewInput {

    @NotBlank(message = "연락처는 필수 항목 입니다.")
    private String phone;

    @Size(min = 8, message = "비밀번호는 8자 이상으로 입력해주세요.")
    @NotBlank(message = "비밀번호는 필수 항목 입니다.")
    private String password;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", message = "올바른 날짜와 시간 형식 (yyyy-MM-dd HH:mm) 으로 입력해주세요!")
    @NotBlank(message = "예약타임을 입력해주세요")
    private String dateTime;

    @NotBlank(message = "매장명은 필수항목 입니다.")
    private String restaurantName;

    @Size(min = 10, message = "내용은 10자 이상 입력해주세요.")
    private String reviewContent;

    @Min(value = 1, message = "별점은 1에서 5까지 입력해주세요.")
    @Max(value = 5, message = "별점은 1에서 5까지 입력해주세요.")
    private int star;
}
