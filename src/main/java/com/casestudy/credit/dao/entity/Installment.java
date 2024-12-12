package com.casestudy.credit.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "INSTALLMENT")
public class Installment extends BaseCreatedColumns{
    @Id
    @SequenceGenerator(name = "SEQ_INSTALLMENT", sequenceName = "SEQ_INSTALLMENT")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INSTALLMENT")
    @Column(name = "ID", length = 16)
    private Long id;
    @Column(name = "AMOUNT", precision = 19, scale = 2)
    private BigDecimal amount;
    @Column(name = "PARTIAL_FLAG", length = 1)
    private Integer partialFlag;
    @Column(name = "STATUS", length = 1)
    private Integer status;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "CREDIT_ID", referencedColumnName = "ID")
    private Credit credit;
    @Column(name = "INSTALLMENT_DATE")
    private Date installmentDate;
    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;
    @OneToMany(mappedBy = "payment")
    private List<Payment> payments;
}
