package com.movietheater.movietheater_mvc.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.movietheater.movietheater_mvc.dtos.invoice.InvoiceDTO;
import com.movietheater.movietheater_mvc.entities.Invoice;
import com.movietheater.movietheater_mvc.repositories.InvoiceRepository;
import com.movietheater.movietheater_mvc.services.InvoiceService;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<InvoiceDTO> findAll() {
        // get list of entities
        var invoices = invoiceRepository.findAll();
        // convert list of entities to list of dtos
        var invoiceDTOs = invoices.stream().map(invoice -> {
            var invoiceDTO = new InvoiceDTO();
            invoiceDTO.setId(invoice.getId());
            invoiceDTO.setBookingDate(invoice.getBookingDate());
            invoiceDTO.setMovieName(invoice.getMovieName());
            invoiceDTO.setScheduleShow(invoice.getScheduleShow());
            invoiceDTO.setScheduleShowTime(invoice.getScheduleShowTime());
            invoiceDTO.setStatus(invoice.getStatus());
            invoiceDTO.setAccountId(invoice.getAccount().getId());
            invoiceDTO.setTotalMoney(invoice.getTotalMoney());
            return invoiceDTO;
        }).toList();
        return invoiceDTOs;
    }

    @Override
    public Page<InvoiceDTO> findByMemberIdAndKeyword(UUID memberId, String keyword, Pageable pageable) {

        Specification<Invoice> specification = (root, query, criteriaBuilder) -> {

            Predicate memberPredicate = criteriaBuilder.equal(root.get("account").get("id"), memberId);

            if (keyword != null && !keyword.trim().isEmpty()) {

                Predicate keywordPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("movieName")),
                        "%" + keyword.toLowerCase() + "%");
                return criteriaBuilder.and(memberPredicate, keywordPredicate);
            }

            return memberPredicate;
        };

        Page<Invoice> invoices = invoiceRepository.findAll(specification, pageable);

        System.out.println(invoices);

        return invoices.map(invoice -> {
            var dto = new InvoiceDTO();
            dto.setId(invoice.getId());
            dto.setTotalMoney(invoice.getTotalMoney());
            dto.setBookingDate(invoice.getBookingDate());
            dto.setMovieName(invoice.getMovieName());
            dto.setScheduleShow(invoice.getScheduleShow());
            dto.setScheduleShowTime(invoice.getScheduleShowTime());
            dto.setStatus(invoice.getStatus());
            dto.setSeat(invoice.getSeat());
            return dto;
        });
    }

    @Override
    public Page<Invoice> findAll(Pageable pageable) {
        return invoiceRepository.findAll(pageable);
    }

    @Override
    public Page<Invoice> searchInvoices(String keyword, Pageable pageable) {
        return invoiceRepository.searchInvoices(keyword, pageable);
    }

    @Override
    public void updateStatus(UUID invoiceId, boolean status) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        invoice.setStatus(status);
        invoiceRepository.save(invoice);
    }

    @Override
    public boolean deleteById(UUID id) {
         // Check if entity exists
         var invoice = invoiceRepository.findById(id).orElse(null);
         // Check if entity is null then return false
         if (invoice == null) {
             return false;
         }
         // Delete entity
         invoiceRepository.delete(invoice);
         // Check if entity is deleted
         var isDeleted = invoiceRepository.findById(id).orElse(null) == null;
         return isDeleted;
    }
}
