package com.casestudy.credit.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

import static com.casestudy.credit.constant.CreditConstants.NOT_PARTIAL;

@Entity
@Getter
@Setter
@Table(name = "PAYMENT")
public class Payment extends BaseCreatedColumns{
    @Id
    @SequenceGenerator(name = "SEQ_PAYMENT", sequenceName = "SEQ_PAYMENT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAYMENT")
    @Column(name = "ID", length = 16)
    private Long id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "INSTALLMENT_ID", referencedColumnName = "ID")
    private Installment installment;
    @Column(name = "PAID_AMOUNT", precision = 19, scale = 2)
    private BigDecimal paidAmount;
    @Column(name = "PARTIAL_FLAG")
    private Boolean partialFlag = NOT_PARTIAL;
    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;
}
