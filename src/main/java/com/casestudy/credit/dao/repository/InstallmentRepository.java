package com.casestudy.credit.dao.repository;

import com.casestudy.credit.dao.entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment,Long> {
}
