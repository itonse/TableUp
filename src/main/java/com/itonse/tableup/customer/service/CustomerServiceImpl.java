package com.itonse.tableup.customer.service;

import com.itonse.tableup.customer.domain.Member;
import com.itonse.tableup.customer.model.MembershipInput;
import com.itonse.tableup.customer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final MemberRepository memberRepository;

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
}
