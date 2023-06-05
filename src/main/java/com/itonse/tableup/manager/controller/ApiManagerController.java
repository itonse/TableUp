package com.itonse.tableup.manager.controller;

import com.itonse.tableup.manager.domain.Partnership;
import com.itonse.tableup.manager.dto.*;
import com.itonse.tableup.manager.model.ResponseError;
import com.itonse.tableup.manager.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ApiManagerController {

    private final ManagerService managerService;


    // 파트너쉽 가입
    @PostMapping("/manager/partnership/new")
    public ResponseEntity<?> SignUpPartnership(
            @RequestBody @Valid AddPartnership addPartnership, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 이미 파트너쉽에 등록되어있는 고객인지 확인
        boolean registered = managerService.getIsRegisteredPartnership(
                addPartnership.getOwnerName(), addPartnership.getPhone());

        if (registered) {
            return new ResponseEntity("이미 가입된 회원입니다.", HttpStatus.BAD_REQUEST);
        }

        // 파트너쉽 등록
        managerService.addPartnership(addPartnership);

        return ResponseEntity.ok().body("파트너쉽에 가입되었습니다. 바로 이용해 주세요!");
    }

    // 파트너쉽 해지 (등록된 매장이 없는 상태에서만 해지 가능)
    @DeleteMapping("/manager/partnership/withdrawal")
    public ResponseEntity<?> withdrawalPartnership(
            @RequestBody @Valid DeletePartnership deletePartnershipInput, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        Optional<Partnership> optionalPartnership = managerService.getPartnershipMember(
                deletePartnershipInput.getEmail(),
                deletePartnershipInput.getPassword()
        );

        // 입려한 정보가 파트너쉽에 가입된 정보가 아닐 시
        if (!optionalPartnership.isPresent()) {
            return new ResponseEntity<>("회원 정보가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        Partnership partnership = optionalPartnership.get();

        int count = managerService.getRestaurantCount(partnership);

        // 등록된 매장이 있을 시
        if (count > 0) {
            return new ResponseEntity<>("등록한 매장 정보를 삭제한 후에 회원 탈퇴를 진행해 주세요."
                    , HttpStatus.BAD_REQUEST);
        } else {
            managerService.deletePartnership(deletePartnershipInput);

            return ResponseEntity.ok().body("회원 탈퇴가 완료되었습니다.");
        }
    }

    // 매장 신규 등록
    @PostMapping("/manager/restaurant/register")
    public ResponseEntity<?> AddRestaurant(
            @RequestBody @Valid AddRestaurantInput addRestaurantInput
            , Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 이미 등록된 매장인지 확인
        boolean registered = managerService.getIsRegisteredRestaurant(
                addRestaurantInput.getRestaurantName(), addRestaurantInput.getRestaurantLocation());

        if (registered) {
            return new ResponseEntity<>("이미 등록된 매장입니다.", HttpStatus.BAD_REQUEST);
        }

        // 파트너쉽에 가입된 회원인지 확인
        Optional<Partnership> partnership = managerService.getPartnershipMember(
                addRestaurantInput.getPartnershipEmail(),
                addRestaurantInput.getPartnershipPassword()
        );

        if (!partnership.isPresent()) {
            return new ResponseEntity<>("파트너쉽에 가입된 정보가 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 매장 등록
        Partnership partnershipMember = partnership.get();

        managerService.addRestaurant(addRestaurantInput, partnershipMember);

        return ResponseEntity.ok().body("매장이 등록되었습니다.");
    }

    // 매장 정보 수정
    @PatchMapping("/manager/restaurant/{id}/update")
    public ResponseEntity<?> updateRestaurant(
            @PathVariable Long id,
            @RequestBody @Valid UpdateRestaurantInput updateRestaurantInput, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 수정 권한 확인
        boolean authorization =
        managerService.checkAuthorization(
                id,
                updateRestaurantInput.getPartnershipEmail(),
                updateRestaurantInput.getPartnershipPassword()
        );

        if (!authorization) {
            return new ResponseEntity<>("매장정보 수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 매장정보 수정 진행
        managerService.updateRestaurant(
                updateRestaurantInput.getRestaurantName(),
                updateRestaurantInput.getRestaurantLocation(),
                updateRestaurantInput.getRestaurantDescription(),
                id);

        return new ResponseEntity<>("매장정보 수정을 완료했습니다.", HttpStatus.OK);
    }

    // 매장 정보 삭제
    @DeleteMapping("/manager/restaurant/{id}/delete")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id
            , @RequestBody @Valid DeleteRestaurantInput deleteRestaurantInput
            , Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 삭제권한 확인
        boolean authorization =
                managerService.checkAuthorization(
                        id,
                        deleteRestaurantInput.getPartnershipEmail(),
                        deleteRestaurantInput.getPartnershipPassword()
                );

        if (!authorization) {
            return new ResponseEntity<>("매장정보 삭제 권한이 없거나, 등록 되어있는 매장이 아닙니다."
                    , HttpStatus.BAD_REQUEST);
        }

        // 매장정보 삭제 진행
        managerService.deleteRestaurant(id);

        return new ResponseEntity<>("매장정보를 삭제하였습니다.", HttpStatus.OK);
    }
}