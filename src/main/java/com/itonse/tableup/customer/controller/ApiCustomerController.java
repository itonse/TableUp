package com.itonse.tableup.customer.controller;

import com.itonse.tableup.customer.dto.*;
import com.itonse.tableup.customer.dto.RestaurantResponse;
import com.itonse.tableup.customer.service.CustomerService;
import com.itonse.tableup.manager.domain.Restaurant;
import com.itonse.tableup.manager.model.ResponseError;
import com.itonse.tableup.util.DateTimeToLocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ApiCustomerController {

    private final CustomerService customerService;

    // 멤버쉽 가입
    @PostMapping("/customer/membership/new")
    public ResponseEntity<?> SignUpMembership(
            @RequestBody @Valid MembershipInput membershipInput, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 이미 멤버쉽에 등록되어있는 고객인지 확인
        boolean registered =
                customerService.getIsRegisteredMembership(
                        membershipInput.getPhone(),
                        membershipInput.getUserName()
                );

        if (registered) {
            return new ResponseEntity<>("이미 가입된 회원입니다.", HttpStatus.BAD_REQUEST);
        }

        // 회원가입 진행
        customerService.addMembership(membershipInput);

        return ResponseEntity.ok().body("멤버쉽에 가입되었습니다. 바로 이용해 주세요!");
    }

    // 멤버쉽 탈퇴
    @DeleteMapping("/customer/membership/{id}/delete")
    public ResponseEntity<?> deleteMembership(
            @PathVariable Long id,
            @RequestBody DeleteMembershipInput deleteMembershipInput, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 탈퇴 권한 확인
        boolean authorization =
                customerService.checkDeleteAuthorization(
                        id, deleteMembershipInput.getEmail(), deleteMembershipInput.getPassword());

        if (!authorization) {
            return new ResponseEntity<>("회원 탈퇴 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 멤버쉽 탈퇴 진행
        customerService.deleteMembership(id);

        return new ResponseEntity<>("회원 탈퇴가 완료되었습니다.", HttpStatus.OK);
    }

    // 매장 검색하여 상세정보 보기
    @GetMapping("/customer/restaurant/search")
    public RestaurantResponse searchRestaurant(@RequestParam String restaurantName) {
        return customerService.getRestaurant(restaurantName);
    }

    // 매장의 상세 목록 보기 -> 정렬(매장명 가나다순), 페이징처리(5개씩)
    @GetMapping("/customer/restaurant/view/restaurantName/Asc")
    public List<RestaurantResponse> getRestaurantListSortedByName(@RequestParam int page) {
        Page<Restaurant> paging = customerService.getRestaurantPageSortedByName(page);

        return customerService.PageToList(paging);
    }

    // 매장의 상세 목록 보기 -> 정렬(별점높은순), 페이징처리(5개씩)
    @GetMapping("/customer/restaurant/view/star/Decs")
    public List<RestaurantResponse> getRestaurantListSortedByStar(@RequestParam int page) {
        Page<Restaurant> paging = customerService.getRestaurantPageSortedByStar(page);

        return customerService.PageToList(paging);
    }

    // 식당 예약
    @PostMapping("/customer/reservation")
    public ResponseEntity<?> reserveRestaurant(
            @RequestBody @Valid ReservationInput reservationInput, Errors errors) {
        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 멤버쉽 로그인
        boolean loginResult = customerService.getLoginResult(
                reservationInput.getUserName(),
                reservationInput.getPassword()
        );

        if (loginResult == false) {
            return new ResponseEntity<>("멤버쉽 회원이 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        // 예약 진행 (예약시간은 현재시간 기준 30분 이후부터 가능)
        LocalDateTime localDateTime = DateTimeToLocalDateTime.from(reservationInput.getDateTime());

        boolean isAlreadyReservedTime = customerService.checkAlreadyReservedTime(
                reservationInput.getRestaurantName(), localDateTime);

        if (isAlreadyReservedTime) {
            return new ResponseEntity<>("해당 시간는 이미 예약이 존재합니다.", HttpStatus.BAD_REQUEST);
        }

        if (!LocalDateTime.now().plusMinutes(30).isBefore(localDateTime)) {
            return new ResponseEntity<>(
                    "현재 시간으로부터 30분 이후의 타임부터 예약이 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        customerService.reserveRestaurant(reservationInput, localDateTime);

        return ResponseEntity.ok()
                .body("예약이 완료되었습니다. 예약 시간 10분 전까지 매장에 도착하여 키오스크를 통해 도착확인을 진행해 주세요!");
    }

    // 식당예약 취소
    @DeleteMapping("/customer/reservation/cancel")
    public ResponseEntity<?> CancelReservation(
            @RequestBody @Valid ReservationInput reservationInput, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 멤버쉽 로그인
        boolean loginResult = customerService.getLoginResult(
                reservationInput.getUserName(),
                reservationInput.getPassword()
        );

        if (loginResult == false) {
            return new ResponseEntity<>("멤버쉽 회원이 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        // 식당예약 취소 (예약된 시간이 30분도 안 남았을 시에는 취소 불가능)
        LocalDateTime localDateTime = DateTimeToLocalDateTime.from(reservationInput.getDateTime());

        if (LocalDateTime.now().isAfter(localDateTime.minusMinutes(30))) {
            return new ResponseEntity<>("예약 시간 30분 전까지만 취소가 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        boolean serviceResult =
                customerService.CancelReservation(reservationInput.getUserName(), localDateTime);

        if (!serviceResult) {
            return new ResponseEntity<>("예약 정보가 존재하지 않습니다", HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok().body("예약이 취소되었습니다.");
        }
    }

    // 키오스크에서 도착확인 진행
    @PatchMapping("/customer/reservation/location/arrival")
    public KioskDto.Response arrivalReservedRestaurant(@RequestBody @Valid KioskDto.Request request) {

        // 방문체크가 정상적으로 완료되었으면 true, 예약기록이 없거나 예약시간 10분전 이후에 방문하였으면 false
        return KioskDto.Response.from(
                customerService.KioskVisitService(
                        request.getUserName(),
                        request.getDateTime()
                )
        );
    }

    // 리뷰 작성
    @PostMapping("/customer/review/write")
    public ResponseEntity<?> addReview(
            @RequestBody @Valid WriteReviewInput writeReviewInput, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 리뷰 작성 권한 확인
        boolean Authorization = customerService.checkReviewWriteAuthorization(
                writeReviewInput.getPhone(),
                writeReviewInput.getPassword(),
                writeReviewInput.getDateTime()
        );

        if (!Authorization) {
            return new ResponseEntity<>("리뷰작성 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 이미 리뷰를 작성했는지 확인
        boolean AlreadyWritten = customerService.isReviewWritten(
                writeReviewInput.getPhone(), writeReviewInput.getDateTime()
        );

        if (AlreadyWritten) {
            return new ResponseEntity<>("\n" +
                    "이미 해당 예약에 대해 리뷰를 작성하였습니다.", HttpStatus.BAD_REQUEST);
        }

        // 리뷰작성
        int star = customerService.writeReview(
                writeReviewInput.getRestaurantName(),
                writeReviewInput.getPhone(),
                writeReviewInput.getReviewContent(),
                writeReviewInput.getDateTime(),
                writeReviewInput.getStar()
                );

        customerService.updateStar(star, writeReviewInput.getRestaurantName());

        return ResponseEntity.ok().body("리뷰작성을 완료하였습니다.");
    }
}
