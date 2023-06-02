package com.itonse.tableup.customer.controller;

import com.itonse.tableup.customer.model.DeleteMembershipInput;
import com.itonse.tableup.customer.model.MembershipInput;
import com.itonse.tableup.customer.service.CustomerService;
import com.itonse.tableup.manager.model.ResponseError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ApiCustomerController {

    private final CustomerService customerService;

    // 회원가입
    @PostMapping("/customer/membership/new")
    public ResponseEntity<?> SignUpMembership(@RequestBody @Valid MembershipInput membershipInput, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 이미 멤버쉽에 등록되어있는 고객인지 확인
        Boolean registered = customerService.getIsRegisteredMembership(membershipInput);

        if (registered) {
            return new ResponseEntity("이미 가입된 회원입니다.", HttpStatus.BAD_REQUEST);
        }

        // 회원가입 진행
        customerService.addMembership(membershipInput);

        return ResponseEntity.ok().body("멤버쉽에 가입되었습니다. 바로 이용해 주세요!");
    }

    // 회원탈퇴 API
    @DeleteMapping("/customer/membership/{id}/delete")
    public ResponseEntity<?> deleteMembership(@PathVariable Long id, @RequestBody DeleteMembershipInput deleteMembershipInput, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 삭제권한 확인
        boolean authorization =
                customerService.checkDeleteAuthorization(
                        id, deleteMembershipInput.getEmail(), deleteMembershipInput.getPassword());

        if (!authorization) {
            return new ResponseEntity<>("멤버 삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 회원탈퇴 진행
        customerService.deleteMembership(id);

        return new ResponseEntity<>("회원 탈퇴가 완료되었습니다.", HttpStatus.OK);
    }

}
