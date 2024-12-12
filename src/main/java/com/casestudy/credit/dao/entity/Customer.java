package com.casestudy.credit.dao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "CUSTOMER")
public class Customer extends BaseCreatedColumns{
    @Id
    @SequenceGenerator(name = "SEQ_CUSTOMER", sequenceName = "SEQ_CUSTOMER")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CUSTOMER")
    @Column(name = "ID", length = 16)
    private Long id;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "LAST_NAME")
    private String lastName;
    @OneToMany(mappedBy = "customer")
    private List<Credit> credits;

}
