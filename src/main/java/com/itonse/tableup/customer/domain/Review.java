package com.itonse.tableup.customer.domain;

import com.itonse.tableup.manager.domain.Restaurant;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Review {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long no;

    @ManyToOne // 양방향 연관관계(restaurant 의 id 참조)
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)   // 매장 데이터를 삭제할때, 해당 매장의 리뷰들도 자동 삭제
    private Restaurant restaurant;

    private String restaurantName;

    private String phoneNumberTail;

    private String reviewContent;

    private String visitDate;

    private int star;
}
