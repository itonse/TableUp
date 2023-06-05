package com.itonse.tableup.customer.service;

import com.itonse.tableup.customer.domain.Reservation;
import com.itonse.tableup.customer.dto.MembershipInput;
import com.itonse.tableup.customer.dto.ReservationInput;
import com.itonse.tableup.customer.dto.RestaurantResponse;
import com.itonse.tableup.manager.domain.Restaurant;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomerService {

    boolean getIsRegisteredMembership(String phone, String userName);
    // 이미 멤버쉽에 가입된 고객인지 여부 확인

    void addMembership(MembershipInput membershipInput);
    // 멤버쉽 등록

    boolean checkDeleteAuthorization(Long id, String membershipEmail, String membershipPassword);
    // 멤버쉽 탈퇴 권한 체크 (고유 식별자(id)와 이메일, 비밀번호를 입력)

    void deleteMembership(Long id);
    // 멤버쉽 탈퇴

    RestaurantResponse getRestaurant(String restaurantName);
    // 매장 이름으로 검색하여 매장 상세정보(매장명, 위치, 매장설명, 별점) 확인하기

    Page<Restaurant> getRestaurantPageSortedByName(int page);
    // 입력한 페이지에 해당하는 매장 목록들 가져오기 (가나다 순)

    Page<Restaurant> getRestaurantPageSortedByStar(int page);
    // 입력한 페이지에 해당하는 매장 목록들 가져오기 (별점 순)

    List<RestaurantResponse> PageToList(Page<Restaurant> paging);
    // 가져온 페이지를 리스트로 변환하기

    boolean getLoginResult(String userName, String password);
    // 매장 예약하기 위해 먼저 멤버쉽 로그인하기

    boolean checkAlreadyReservedTime(String restaurantName, LocalDateTime localDateTime);
    // 원하는 시간에 이미 예약이 존재하는지 체크

    void reserveRestaurant(ReservationInput reservationInput, LocalDateTime localDateTime);
    // 예약 진행

    boolean CancelReservation(String userName, LocalDateTime localDateTime);
    // 예약 취소 진행 (취소 결과 반환)

    Reservation KioskVisitService(String userName, String dateTime);
    // 키오스크로 방문체크 진행하고 예약정보 반환

    boolean checkReviewWriteAuthorization(String phone, String password, String dateTime);
    // 리뷰작성 권한 체크 (멤버쉽 회원이고, 예약한 정보가 있고, 방문체크가 되어있어야 권한 부여)

    boolean isReviewWritten(String phone, String dateTime);
    // 이미 리뷰 작성을 하였는지 확인

    int writeReview(String restaurantName, String phone, String reviewContent, String dateTime, int star);
    // 리뷰 작성하고, 부여한 별점 반환

    void updateStar(int star, String restaurantName);
    // 매장 상제정보에 별점 업데이트 하기

}
