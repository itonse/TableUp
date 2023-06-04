package com.itonse.tableup.customer.dto;

import com.itonse.tableup.customer.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class KioskDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Request {

        @NotBlank(message = "이름은 필수항목 입니다.")
        private String userName;

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$", message = "올바른 날짜와 시간 형식 (yyyy-MM-dd HH:mm) 으로 입력해주세요!")
        @NotBlank(message = "예약타임은 입력은 필수항목 입니다.")
        private String dateTime;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class Response {
        private String userName;
        private boolean visited;

        public static Response from(Reservation reservation) {
            return Response.builder()
                    .userName(reservation.getUserName())
                    .visited(reservation.getVisited())
                    .build();
        }
    }
}
