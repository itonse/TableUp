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
public class Restaurant {    // 매장정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // id는 PK 이며, 자동증가한다.
    private long id;  // 매장 고유번호

    @ManyToOne
    @JoinColumn
    private Partnership partnership;

    private String restaurantName;   // 매장명

    private String restaurantLocation;     // 매장위치

    private String restaurantDescription;    // 매장설명

    private Double star;  // 매장평점
}
