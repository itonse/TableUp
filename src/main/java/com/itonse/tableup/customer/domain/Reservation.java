package com.itonse.tableup.customer.domain;

import com.itonse.tableup.manager.domain.Restaurant;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @GeneratedValue(strategy = GenerationType.IDENTITY)  // no는 PK 이며, 자동증가한다.
    @Id
    Long no;

    @ManyToOne
    @JoinColumn
    private Restaurant restaurant;

    private String userName;

    private String phoneNumberTail;

    private String restaurantName;

    private LocalDateTime reservationTime;

    private Boolean visited;
}
