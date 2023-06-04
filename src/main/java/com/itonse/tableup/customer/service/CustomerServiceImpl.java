package com.itonse.tableup.customer.service;

import com.itonse.tableup.customer.domain.Member;
import com.itonse.tableup.customer.domain.Reservation;
import com.itonse.tableup.customer.dto.MembershipInputDto;
import com.itonse.tableup.customer.dto.ReservationInputDto;
import com.itonse.tableup.customer.model.RestaurantResponse;
import com.itonse.tableup.customer.repository.MemberRepository;
import com.itonse.tableup.customer.repository.ReservationRepository;
import com.itonse.tableup.manager.domain.Restaurant;
import com.itonse.tableup.manager.repository.RestaurantRepository;
import com.itonse.tableup.util.DateTimeToLocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public Boolean getIsRegisteredMembership(MembershipInputDto membershipInputDto) {

        return memberRepository.existsMemberByPhoneAndUserName(
                membershipInputDto.getPhone(), membershipInputDto.getUserName());
    }

    @Override
    public void addMembership(MembershipInputDto membershipInputDto) {
        Member member = Member.builder()
                .email(membershipInputDto.getEmail())
                .password(membershipInputDto.getPassword())
                .userName(membershipInputDto.getUserName())
                .phone(membershipInputDto.getPhone())
                .build();

        memberRepository.save(member);
    }

    @Override
    public boolean checkDeleteAuthorization(Long id, String membershipEmail, String membershipPassword) {
        Optional<Member> optionalDeleteMember = memberRepository.findById(id);
        Optional<Member> optionalMember = memberRepository.findByEmailAndPassword(
                membershipEmail, membershipPassword);

        if (!optionalMember.isPresent() || !optionalDeleteMember.isPresent()) {
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
        Optional<Member> optionalMember = memberRepository.findById(id);

        memberRepository.delete(optionalMember.get());
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
    public boolean getLoginResult(ReservationInputDto reservationInputDto) {

        return memberRepository.existsMemberByUserNameAndPassword(
                reservationInputDto.getUserName(), reservationInputDto.getPassword()
        );
    }

    @Override
    public void reserveRestaurant(ReservationInputDto reservationInputDto, LocalDateTime localDateTime) {
        Member member = memberRepository.findByUserName(reservationInputDto.getUserName());
        Restaurant restaurant =
                restaurantRepository.findByRestaurantName(reservationInputDto.getRestaurantName());

        String phoneNumberTail = member.getPhone().substring(member.getPhone().length() - 4);

        Reservation reservation = Reservation.builder()
                .userName(reservationInputDto.getUserName())
                .phoneNumberTail(phoneNumberTail)
                .restaurantName(reservationInputDto.getRestaurantName())
                .reservationTime(localDateTime)
                .visited(false)
                .restaurant(restaurant)
                .build();

        reservationRepository.save(reservation);
    }

    @Override
    public Reservation getKioskResult(String userName, String dateTime) {

        LocalDateTime localDateTime = DateTimeToLocalDateTime.from(dateTime);

        Optional<Reservation> OptionalReservation = reservationRepository
                .findByUserNameAndReservationTime(userName, localDateTime);

        Reservation reservation = OptionalReservation.get();

        if (!LocalDateTime.now().isBefore(reservation.getReservationTime().minusMinutes(10))) {
            return reservation;
        } else {   // 예약시간 10분 전에 잘 도착한 경우
            reservation.setVisited(true);
            reservationRepository.save(reservation);

            return reservation;
        }
    }
}
