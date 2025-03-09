package com.butenov.jobplatform.companies.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.butenov.jobplatform.companies.model.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>
{
}
