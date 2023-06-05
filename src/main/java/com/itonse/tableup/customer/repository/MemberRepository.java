package com.itonse.tableup.customer.repository;

import com.itonse.tableup.customer.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsMemberByPhoneAndUserName(String phone, String userName);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmailAndPassword(String email, String password);

    Boolean existsMemberByUserNameAndPassword(String userName, String password);

    Optional<Member> findByUserName(String userName);

    boolean existsByPhoneAndPassword(String phone, String password);
}
