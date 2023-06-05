package com.itonse.tableup.manager.domain;

import com.itonse.tableup.customer.domain.Reservation;
import com.itonse.tableup.customer.domain.Review;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Restaurant {    // 매장정보

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // id는 PK 이며, 자동증가한다.
    private long id;  // 매장 고유번호

    @ManyToOne   // Restaurant -> Partnership(id 참조)  두 테이블은 단방향 연관관계이다.
    @JoinColumn(name = "partnership_id", referencedColumnName = "id", nullable = false)
    private Partnership partnership;  //연관 테이블

    private String restaurantName;   // 매장명

    private String restaurantLocation;     // 매장위치

    private String restaurantDescription;    // 매장설명

    private Double star;  // 매장평점

    // Restaurant 과 Reservation, Review 는 각각 양방향 연관 테이블이다.
    // 매장 정보가 삭제될 시, 예약 테이블에서 해당 매장의 예약 내역과 리뷰 목록들도 같이 삭제
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}

