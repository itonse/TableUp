package com.itonse.tableup.customer.controller;

import com.itonse.tableup.customer.dto.DeleteMembershipInputDto;
import com.itonse.tableup.customer.dto.KioskDto;
import com.itonse.tableup.customer.dto.MembershipInputDto;
import com.itonse.tableup.customer.dto.ReservationInputDto;
import com.itonse.tableup.customer.model.RestaurantResponse;
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

    // 회원가입
    @PostMapping("/customer/membership/new")
    public ResponseEntity<?> SignUpMembership(@RequestBody @Valid MembershipInputDto membershipInputDto, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 이미 멤버쉽에 등록되어있는 고객인지 확인
        Boolean registered = customerService.getIsRegisteredMembership(membershipInputDto);

        if (registered) {
            return new ResponseEntity("이미 가입된 회원입니다.", HttpStatus.BAD_REQUEST);
        }

        // 회원가입 진행
        customerService.addMembership(membershipInputDto);

        return ResponseEntity.ok().body("멤버쉽에 가입되었습니다. 바로 이용해 주세요!");
    }

    // 회원탈퇴
    @DeleteMapping("/customer/membership/{id}/delete")
    public ResponseEntity<?> deleteMembership(@PathVariable Long id, @RequestBody DeleteMembershipInputDto deleteMembershipInputDto, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 삭제권한 확인
        boolean authorization =
                customerService.checkDeleteAuthorization(
                        id, deleteMembershipInputDto.getEmail(), deleteMembershipInputDto.getPassword());

        if (!authorization) {
            return new ResponseEntity<>("멤버 삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 회원탈퇴 진행
        customerService.deleteMembership(id);

        return new ResponseEntity<>("회원 탈퇴가 완료되었습니다.", HttpStatus.OK);
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
    public ResponseEntity<?> reserveRestaurant(@RequestBody @Valid ReservationInputDto reservationInputDto, Errors errors) {
        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 멤버쉽 로그인
        boolean loginResult = customerService.getLoginResult(reservationInputDto);

        if (loginResult == false) {
            return new ResponseEntity<>("멤버쉽 회원이 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        // 예약 진행
        LocalDateTime dateTime = DateTimeToLocalDateTime.from(reservationInputDto.getDateTime());

        if (!LocalDateTime.now().plusMinutes(30).isBefore(dateTime)) {
            return new ResponseEntity<>("현재 시간으로부터 30분 이후의 타임부터 예약이 가능합니다.", HttpStatus.BAD_REQUEST);
        }

        customerService.reserveRestaurant(reservationInputDto, dateTime);

        return ResponseEntity.ok().body("예약이 완료되었습니다. 예약 시간 10분 전까지 매장에 도착하여 키오스크를 통해 도착확인을 진행해 주세요!");
    }

    // 키오스크에 도착확인 진행
    @PatchMapping("/customer/reservation/location/arrival")
    public KioskDto.Response arrivalReservedRestaurant(@RequestBody @Valid KioskDto.Request request) {

        // 방문체크가 정상적으로 완료되었으면 true, 예약시간 10분전 이후에 방문하였으면 false
        return KioskDto.Response.from(
                customerService.getKioskResult(
                        request.getUserName(),
                        request.getDateTime()
                )
        );
    }

}
