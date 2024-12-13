package com.casestudy.credit.dao.repository;

import com.casestudy.credit.dao.entity.Credit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CreditRepository extends JpaRepository<Credit,Long> {
    List<Credit> findByCustomerId(Long customerId);

    @Query(value = "SELECT * FROM CREDIT C WHERE C.CUSTOMER_ID=:customerId AND C.STATUS=:status AND C.CREATED_AT>:openDate",nativeQuery = true)
    Page<Credit> filterCreditByStatusAndOpenDate(Pageable pageable, @Param("customerId") Long customerId, @Param("status") Integer status, @Param("openDate") Date openDate);
}
