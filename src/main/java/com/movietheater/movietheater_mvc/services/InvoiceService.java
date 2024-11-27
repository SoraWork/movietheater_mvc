package com.movietheater.movietheater_mvc.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.movietheater.movietheater_mvc.dtos.invoice.InvoiceDTO;
import com.movietheater.movietheater_mvc.entities.Invoice;

public interface InvoiceService {
    List<InvoiceDTO> findAll();

    Page<Invoice> findAll(Pageable pageable);
    
    Page<Invoice> searchInvoices(String keyword, Pageable pageable);

    Page<InvoiceDTO> findByMemberIdAndKeyword(UUID memberId, String keyword, Pageable pageable);

    void updateStatus(UUID invoiceId, boolean status);

}
