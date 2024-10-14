package com.paymentmanagement.payment.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymentmanagement.payment.models.User;

public interface UsersRepository extends JpaRepository<User, Integer>{

}
