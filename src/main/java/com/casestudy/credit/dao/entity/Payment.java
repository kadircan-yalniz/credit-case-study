package com.casestudy.credit.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "PAYMENT")
public class Payment extends BaseCreatedColumns{
    @Id
    @SequenceGenerator(name = "SEQ_PAYMENT", sequenceName = "SEQ_PAYMENT")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAYMENT")
    @Column(name = "ID", length = 16)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "INSTALLMENT_ID", referencedColumnName = "ID")
    private Installment installment;
    @Column(name = "PAID_AMOUNT", precision = 19, scale = 2)
    private BigDecimal paidAMount;
    @Column(name = "PARTIAL_FLAG", precision = 19, scale = 2)
    private Integer partialFlag;
    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;
}
