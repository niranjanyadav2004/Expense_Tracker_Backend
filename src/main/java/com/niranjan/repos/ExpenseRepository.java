package com.niranjan.repos;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.niranjan.entities.Expense;
import com.niranjan.entities.User;
import com.niranjan.entities.BankAccount;


@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    
    List<Expense> findByBankAccount_BankNameAndUser(String bankName, User user);
    
    Optional<Expense> findFirstByBankAccount_BankNameAndUserOrderByDateDesc(String bankName, User user);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user")
    Optional<Double> sumAllAmountByUser(User user);
    
    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.bankAccount.bankName = :bankName AND e.user = :user")
    Optional<Double> sumAmountByBankName(String bankName, User user);

    Optional<Expense> findFirstByUserOrderByDateDesc(User user);
    
    List<Expense> findAllByUserAndBankAccount(User user, BankAccount bankAccount);
    
    Optional<Expense> findByIdAndUser(Long id, User user);

}
