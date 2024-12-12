package com.casestudy.credit.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "CREDIT")
public class Credit extends BaseCreatedColumns{
    @Id
    @SequenceGenerator(name = "SEQ_CREDIT", sequenceName = "SEQ_CREDIT")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CREDIT")
    @Column(name = "ID", length = 16)
    private Long id;
    @Column(name = "STATUS", length = 1)
    //BOOLEAN OLUR MU?
    private Integer status;
    @Column(name = "REMAINING_AMOUNT", precision = 19, scale = 2)
    private BigDecimal remainingAmount;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "ID")
    private Customer customer;
    @OneToMany(mappedBy = "credit")
    private List<Installment> installments;

}
