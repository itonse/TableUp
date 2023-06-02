package com.itonse.tableup.manager.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Partnership {    // 파트너쉽 가입

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // id는 PK 이며, 자동증가한다.
    private long id;

    private String email;  // 이메일

    private String password;   // 패스워드

    private String ownerName;  // 사장님 성함

    private String phone;   // 연락처
}
