package com.niranjan.repos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.niranjan.entities.Income;
import com.niranjan.entities.User;
import com.niranjan.entities.BankAccount;


@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUser(User user);

    List<Income> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    
    List<Income> findByBankAccount_BankNameAndUser(String bankName, User user);
    
    Optional<Income> findFirstByBankAccount_BankNameAndUserOrderByDateDesc(String bankName, User user);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user = :user")
    Optional<Double> sumAllAmountByUser(User user);
    
    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.bankAccount.bankName = :bankName AND i.user = :user")
    Optional<Double> sumAmountByBankName(String bankName, User user);

    Optional<Income> findFirstByUserOrderByDateDesc(User user);

    Optional<Income> findByIdAndUser(Long id, User user);
    
    List<Income> findAllByUserAndBankAccount(User user, BankAccount bankAccount);
}