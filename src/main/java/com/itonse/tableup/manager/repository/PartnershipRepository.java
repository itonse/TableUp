package com.itonse.tableup.manager.repository;

import com.itonse.tableup.manager.domain.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnershipRepository extends JpaRepository<Partnership, Long> {

    Optional<Partnership> findPartnershipByEmailAndPassword(String email, String password);

    boolean existsPartnershipByPhoneAndOwnerName(String phone, String ownerName);
}
