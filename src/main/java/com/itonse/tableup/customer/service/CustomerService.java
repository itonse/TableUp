package com.itonse.tableup.customer.service;

import com.itonse.tableup.customer.model.MembershipInput;

public interface CustomerService {

    Boolean getIsRegisteredMembership(MembershipInput membershipInput);

    void addMembership(MembershipInput membershipInput);

    boolean checkDeleteAuthorization(Long id, String membershipEmail, String membershipPassword);

    void deleteMembership(Long id);
}
