package com.itonse.tableup.customer.service;

import com.itonse.tableup.customer.domain.Member;
import com.itonse.tableup.customer.domain.Reservation;
import com.itonse.tableup.customer.model.MembershipInput;
import com.itonse.tableup.customer.model.ReservationInput;
import com.itonse.tableup.customer.model.RestaurantResponse;
import com.itonse.tableup.customer.repository.MemberRepository;
import com.itonse.tableup.customer.repository.ReservationRepository;
import com.itonse.tableup.manager.domain.Restaurant;
import com.itonse.tableup.manager.repository.RestaurantRepository;
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
    public Boolean getIsRegisteredMembership(MembershipInput membershipInput) {

        return memberRepository.existsMemberByPhoneAndUserName(
                membershipInput.getPhone(), membershipInput.getUserName());
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
    public boolean getLoginResult(ReservationInput reservationInput) {

        return memberRepository.existsMemberByUserNameAndPassword(
                reservationInput.getUserName(), reservationInput.getPassword()
        );
    }

    @Override
    public void reserveRestaurant(ReservationInput reservationInput, LocalDateTime localDateTime) {
        Member member = memberRepository.findByUserName(reservationInput.getUserName());
        Restaurant restaurant =
                restaurantRepository.findByRestaurantName(reservationInput.getRestaurantName());

        String phoneNumberTail = member.getPhone().substring(member.getPhone().length() - 4);

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
}
