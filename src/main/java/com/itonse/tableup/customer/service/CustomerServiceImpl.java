package com.itonse.tableup.customer.service;

import com.itonse.tableup.customer.domain.Member;
import com.itonse.tableup.customer.domain.Reservation;
import com.itonse.tableup.customer.domain.Review;
import com.itonse.tableup.customer.dto.MembershipInput;
import com.itonse.tableup.customer.dto.ReservationInput;
import com.itonse.tableup.customer.dto.RestaurantResponse;
import com.itonse.tableup.customer.repository.MemberRepository;
import com.itonse.tableup.customer.repository.ReservationRepository;
import com.itonse.tableup.customer.repository.ReviewRepository;
import com.itonse.tableup.manager.domain.Restaurant;
import com.itonse.tableup.manager.repository.RestaurantRepository;
import com.itonse.tableup.util.DateTimeToLocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public boolean getIsRegisteredMembership(String phone, String userName) {

        return memberRepository.existsMemberByPhoneAndUserName(
                phone, userName);
    }

    @Override
    public void addMembership(MembershipInput membershipInput) {
        Member member = Member.builder()
                .email(membershipInput.getEmail())
                .password(membershipInput.getPassword())
                .userName(membershipInput.getUserName())
                .phone(membershipInput.getPhone())
                .build();

        memberRepository.save(member);
    }

    @Override
    public boolean checkDeleteAuthorization(
            Long id, String membershipEmail, String membershipPassword) {

        Optional<Member> optionalDeleteMember = memberRepository.findById(id);
        Optional<Member> optionalMember = memberRepository.findByEmailAndPassword(
                membershipEmail, membershipPassword);

        if (optionalMember.isEmpty() || optionalDeleteMember.isEmpty()) {
            return false;
        }

        String deleteMemberEmail = optionalDeleteMember.get().getEmail();
        String memberEmail = optionalMember.get().getEmail();

        if (!deleteMemberEmail.equals(memberEmail)) {
            return false;
        }

        return true;
    }

    @Override
    public void deleteMembership(Long id) {
        Member member = memberRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("멤버쉽 정보가 없습니다."));

        memberRepository.delete(member);
    }

    @Override
    public RestaurantResponse getRestaurant(String restaurantName) {
        Restaurant restaurant = restaurantRepository.findByRestaurantName(restaurantName)
                .orElseThrow(() -> new NoSuchElementException("해당 명의 매장은 등록되어 있지 않습니다."));

        return RestaurantResponse.of(restaurant);
    }

    @Override
    public Page<Restaurant> getRestaurantPageSortedByName(int page) {
        Pageable pageable = PageRequest.of(page, 5);

        return restaurantRepository.findAllByOrderByRestaurantNameAsc(pageable);
    }

    @Override
    public Page<Restaurant> getRestaurantPageSortedByStar(int page) {
        Pageable pageable = PageRequest.of(page, 5);

        return restaurantRepository.findAllByOrderByStarDesc(pageable);
    }

    @Override
    public List<RestaurantResponse> PageToList(Page<Restaurant> paging) {
        List<Restaurant> restaurantList = paging.toList();
        List<RestaurantResponse> responseList = new ArrayList<>();

        restaurantList.stream().forEach((e) -> {
            responseList.add(RestaurantResponse.of(e));
        });

        return responseList;
    }

    @Override
    public boolean getLoginResult(String userName, String password) {

        return memberRepository.existsMemberByUserNameAndPassword(
                userName, password);
    }

    @Override
    public boolean checkAlreadyReservedTime(String restaurantName, LocalDateTime localDateTime) {
        return reservationRepository.existsByRestaurantNameAndReservationTime(
                restaurantName, localDateTime);
    }

    @Override
    public void reserveRestaurant(
            ReservationInput reservationInput, LocalDateTime localDateTime) {

        Member member = memberRepository.findByUserName(reservationInput.getUserName())
                .orElseThrow(() -> new NoSuchElementException("멤버쉽 정보가 없습니다."));
        Restaurant restaurant =
                restaurantRepository.findByRestaurantName(reservationInput.getRestaurantName())
                        .orElseThrow(() -> new NoSuchElementException("매장 정보가 없습니다."));

        String phoneNumberTail =
                member.getPhone().substring(member.getPhone().length() - 4);

        Reservation reservation = Reservation.builder()
                .userName(reservationInput.getUserName())
                .phoneNumberTail(phoneNumberTail)
                .restaurantName(reservationInput.getRestaurantName())
                .reservationTime(localDateTime)
                .visited(false)
                .restaurant(restaurant)
                .build();

        reservationRepository.save(reservation);
    }

    @Override
    public boolean CancelReservation(String userName, LocalDateTime localDateTime) {
        Optional<Reservation> optionalReservation =
                reservationRepository.findByUserNameAndReservationTime(
                        userName, localDateTime
                );

        if (optionalReservation.isEmpty()) {
            return false;
        } else {
            reservationRepository.delete(optionalReservation.get());
            return true;
        }
    }

    @Override
    public Reservation KioskVisitService(String userName, String dateTime) {

        LocalDateTime localDateTime = DateTimeToLocalDateTime.from(dateTime);

        Reservation reservation = reservationRepository
                .findByUserNameAndReservationTime(userName, localDateTime)
                .orElseThrow(() -> new NoSuchElementException("예약 정보가 없습니다."));

        if (!LocalDateTime.now().isBefore(reservation.getReservationTime().minusMinutes(10))) {
            return reservation;
        } else {   // 예약시간 10분 전에 잘 도착한 경우
            reservation.setVisited(true);
            reservationRepository.save(reservation);

            return reservation;
        }
    }

    @Override
    public boolean checkReviewWriteAuthorization(
            String phone, String password, String dateTime) {

        LocalDateTime localDateTime = DateTimeToLocalDateTime.from(dateTime);

        // 멤버쉽 회원이 아닌경우 false
        boolean checkMember =
                memberRepository.existsByPhoneAndPassword(phone, password);

        if (!checkMember) {
            return false;
        }

        // 예약한 정보가 없는 경우 false
        String phoneNumberTail = phone.substring(phone.length() - 4);

        Optional<Reservation> optionalReservation = reservationRepository
                .findByPhoneNumberTailAndReservationTime(phoneNumberTail, localDateTime);

        if (optionalReservation.isEmpty()) {
            return false;
        }

        // 방문하지 않은 경우 false
        Reservation reservation = optionalReservation.get();

        if (!reservation.getVisited()) {
            return false;
        }

        // 리뷰작성 권한 있음.
        return true;
    }

    @Override
    public boolean isReviewWritten(String phone, String dateTime) {
        String phoneNumberTail = phone.substring(phone.length() - 4);
        LocalDateTime localDateTime = DateTimeToLocalDateTime.from(dateTime);
        String visitedDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        boolean isWritten =
                reviewRepository.existsByPhoneNumberTailAndVisitDate(phoneNumberTail, visitedDate);

        if (isWritten) {
            return true;
        }

        return false;
    }

    @Override
    public int writeReview(
            String restaurantName, String phone, String reviewContent, String dateTime, int star) {

        // 리뷰작성에 필요한 객체들 변환 및 가져오기
        String phoneNumberTail = phone.substring(phone.length() - 4);
        LocalDateTime localDateTime = DateTimeToLocalDateTime.from(dateTime);
        String visitedDate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Reservation reservation =
                reservationRepository.findByPhoneNumberTailAndReservationTime(
                        phoneNumberTail, localDateTime
                ).orElseThrow(() -> new NoSuchElementException("예약 정보가 없습니다."));

        Restaurant restaurant = reservation.getRestaurant();

        // 리뷰작성
        Review review = Review.builder()
                .restaurant(restaurant)
                .restaurantName(restaurantName)
                .phoneNumberTail(phoneNumberTail)
                .reviewContent(reviewContent)
                .visitDate(visitedDate)
                .star(star)
                .build();

        reviewRepository.save(review);

        return star;  // 별점 반환하기
    }

    @Override
    public void updateStar(int star, String restaurantName) {

        Restaurant restaurant = restaurantRepository.findByRestaurantName(restaurantName)
                .orElseThrow(() -> new NoSuchElementException("매장 정보가 없습니다."));

        int restaurantReviewCount = reviewRepository.countByRestaurantName(restaurantName);

        // 최초 리뷰 등록이면
        if (restaurantReviewCount == 1) {

            restaurant.setStar((double)star);

            restaurantRepository.save(restaurant);
            return;
        }

        // 최초 리뷰 등록이 아닐 시
        double getRating =
                ( restaurant.getStar() * (restaurantReviewCount - 1) + (double)star )
                        / restaurantReviewCount;

        restaurant.setStar(getRating);
        restaurantRepository.save(restaurant);
    }


}
