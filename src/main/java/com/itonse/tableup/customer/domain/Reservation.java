package com.itonse.tableup.customer.domain;

import com.itonse.tableup.manager.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Reservation {

    @GeneratedValue(strategy = GenerationType.IDENTITY)  // no는 PK 이며, 자동증가한다.
    @Id
    Long no;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", referencedColumnName = "id", nullable = false)  // 매장 데이터를 삭제할때, 해당 매장의 리뷰도 자동 삭제
    private Restaurant restaurant;

    private String userName;

    private String phoneNumberTail;

    private String restaurantName;

    private LocalDateTime reservationTime;

    private Boolean visited;

}
