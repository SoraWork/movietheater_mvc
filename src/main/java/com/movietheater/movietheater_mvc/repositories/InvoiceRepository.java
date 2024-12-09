package com.movietheater.movietheater_mvc.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.movietheater.movietheater_mvc.entities.Account;
import com.movietheater.movietheater_mvc.entities.Invoice;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID>,JpaSpecificationExecutor<Invoice>{
    List<Invoice> findByAccount_Id(UUID id);

    Page<Invoice> findByAccount(Account account, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE " +
           "LOWER(i.account.identityCard) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.account.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.account.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Invoice> searchInvoices(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE i.seat = CONCAT(:seatColumn, :seatRow) " +
           "AND i.scheduleShowTime = :scheduleTime AND i.scheduleShow = :date")
    Invoice findInvoiceBySeatAndSchedule(
        @Param("seatColumn") String seatColumn,
        @Param("seatRow") int seatRow,
        @Param("scheduleTime") String scheduleTime,
        @Param("date") LocalDate date);
}
