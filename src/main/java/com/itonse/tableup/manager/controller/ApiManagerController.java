package com.itonse.tableup.manager.controller;

import com.itonse.tableup.manager.domain.Partnership;
import com.itonse.tableup.manager.model.*;
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


    // 파트너쉽 가입 (회원 중복 체크)
    @PostMapping("/manager/partnership/new")
    public ResponseEntity<?> SignUp(@RequestBody @Valid PartnershipInput partnershipInput, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 이미 파트너쉽에 등록되어있는 고객인지 확인
        Boolean registered = managerService.getIsRegisteredPartnership(partnershipInput);

        if (registered) {
            return new ResponseEntity("이미 가입된 회원입니다.", HttpStatus.BAD_REQUEST);
        }

        // 파트너쉽 등록
        managerService.addPartnership(partnershipInput);

        return ResponseEntity.ok().body("파트너쉽에 가입되었습니다. 바로 이용해 주세요!");
    }

    // 매장 신규 등록
    @PostMapping("/manager/restaurant/register")
    public ResponseEntity<?> AddRestaurant(@RequestBody @Valid AddRestaurantInput addRestaurantInput
            , Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 이미 등록된 매장인지 확인
        Boolean registered = managerService.getIsRegisteredRestaurant(addRestaurantInput);

        if (registered) {
            return new ResponseEntity<>("이미 등록된 매장입니다.", HttpStatus.BAD_REQUEST);
        }

        // 파트너쉽에 가입된 회원인지 확인
        Optional<Partnership> partnership = managerService.getPartnershipMember(
                addRestaurantInput.getPartnershipEmail(), addRestaurantInput.getPartnershipPassword());

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
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id
                                              ,@RequestBody @Valid UpdateRestaurantInput updateRestaurantInput, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 수정 권한 확인
        boolean authorization =
        managerService.CheckAuthorization(
                id, updateRestaurantInput.getPartnershipEmail(), updateRestaurantInput.getPartnershipPassword()
        );

        if (!authorization) {
            return new ResponseEntity<>("매장정보 수정 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 매장정보 수정 진행
        managerService.UpdateRestaurant(updateRestaurantInput, id);

        return new ResponseEntity<>("매장정보 수정을 완료했습니다.", HttpStatus.OK);
    }

    @DeleteMapping("/manager/restaurant/{id}/delete")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id
            ,@RequestBody @Valid DeleteRestaurantInput deleteRestaurantInput, Errors errors) {

        if (errors.hasErrors()) {
            ResponseError responseError = new ResponseError();
            return responseError.ResponseErrorList(errors);
        }

        // 삭제권한 확인
        boolean authorization =
                managerService.CheckAuthorization(
                        id, deleteRestaurantInput.getPartnershipEmail(), deleteRestaurantInput.getPartnershipPassword()
                );

        if (!authorization) {
            return new ResponseEntity<>("매장정보 삭제 권한이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 매장정보 삭제 진행
        managerService.DeleteRestaurant(id);

        return new ResponseEntity<>("매장정보를 삭제하였습니다.", HttpStatus.OK);
    }
}