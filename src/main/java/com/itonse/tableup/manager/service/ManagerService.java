package com.itonse.tableup.manager.service;

import com.itonse.tableup.manager.domain.Partnership;
import com.itonse.tableup.manager.dto.AddPartnership;
import com.itonse.tableup.manager.dto.AddRestaurantInput;
import com.itonse.tableup.manager.dto.DeletePartnership;

import java.util.Optional;

public interface ManagerService {
    boolean getIsRegisteredPartnership(String ownerName, String phone);
    // 이미 파트너쉽에 등록된 회원인지 여부 반환

    void addPartnership(AddPartnership addPartnership);
    // 파트너쉽 가입

    Optional<Partnership> getPartnershipMember(String email, String password);
    // 입력값에 해당 파트너쉽 멤버정보 반환

    int getRestaurantCount(Partnership partnership);
    // 해당 파트너쉽으로 현재 등록되어 있는 매장의 개수 가져오기

    void deletePartnership(DeletePartnership deletePartnership);
    // 파트너쉽 해지 진행

    boolean getIsRegisteredRestaurant(String restaurantName, String restaurantLocation);
    // 이미 등록된 매장인지 확인

    void addRestaurant(AddRestaurantInput addRestaurantInput, Partnership partnership);
    // 매장 등록

    boolean checkAuthorization(Long id, String partnershipEmail, String partnershipPassword);
    // 매장정보 수정 권한 확인 (정보 일치 여부 확인)

    void updateRestaurant(String restaurantName, String restaurantLocation, String restaurantDescription, Long id);
    // 매장정보 수정 진행 (매장명, 매장위치, 매장설명 변경 가능)

    void deleteRestaurant(Long id);
    // 매장정보 삭제 진행
}
