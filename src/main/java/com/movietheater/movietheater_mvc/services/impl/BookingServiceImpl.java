package com.movietheater.movietheater_mvc.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.movietheater.movietheater_mvc.entities.Invoice;
import com.movietheater.movietheater_mvc.entities.ScheduleSeat;
import com.movietheater.movietheater_mvc.entities.Ticket;
import com.movietheater.movietheater_mvc.repositories.AccountRepository;
import com.movietheater.movietheater_mvc.repositories.InvoiceRepository;
import com.movietheater.movietheater_mvc.repositories.MemberRepository;
import com.movietheater.movietheater_mvc.repositories.MovieRepository;
import com.movietheater.movietheater_mvc.repositories.ScheduleRepository;
import com.movietheater.movietheater_mvc.repositories.ScheduleSeatRepository;
import com.movietheater.movietheater_mvc.repositories.TicketRepository;
import com.movietheater.movietheater_mvc.services.BookingService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {
    private final ScheduleSeatRepository scheduleSeatRepository;
    private final TicketRepository ticketRepository;
    private final InvoiceRepository invoiceRepository;
    private final AccountRepository accountRepository;
    private final MovieRepository movieRepository;
    private final ScheduleRepository scheduleRepository;

    public BookingServiceImpl(ScheduleSeatRepository scheduleSeatRepository, TicketRepository ticketRepository,
            InvoiceRepository invoiceRepository, MemberRepository memberRepository, AccountRepository accountRepository,
            MovieRepository movieRepository, ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
        this.movieRepository = movieRepository;
        this.accountRepository = accountRepository;
        this.scheduleSeatRepository = scheduleSeatRepository;
        this.ticketRepository = ticketRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public boolean bookTickets(UUID movieId, String selectedSeats, int totalPrice, String schedule, String time,
            String username) {
        var account = accountRepository.findByUsername(username);
        var scheduletime = scheduleRepository.findByScheduleTime(schedule);
        var movie = movieRepository.findById(movieId).orElse(null);

        String[] seats = selectedSeats.split(",");

        for (String seat : seats) {

            if (seat.length() < 2 || !Character.isDigit(seat.charAt(0)) || !Character.isLetter(seat.charAt(1))) {
                throw new IllegalArgumentException("Invalid seat format: " + seat);
            }

            int seatRow = Integer.parseInt(seat.substring(0, seat.length() - 1));
            String seatColumn = seat.substring(seat.length() - 1);

            ScheduleSeat scheduleSeat = new ScheduleSeat();
            scheduleSeat.setMovieId(movieId);
            scheduleSeat.setScheduleId(scheduletime.getId());
            scheduleSeat.setSeatColumn(seatColumn);
            scheduleSeat.setSeatRow(seatRow);
            LocalDate date = LocalDate.parse(time);
            scheduleSeat.setSeatDate(date);
            scheduleSeat.setSeatStatus(false);
            scheduleSeat.setSeatType(true);
            scheduleSeatRepository.save(scheduleSeat);

        }

        Ticket ticket = new Ticket();
        ticket.setPrice(totalPrice);
        ticket.setTicketType(true);
        ticketRepository.save(ticket);

        Invoice invoice = new Invoice();
        invoice.setBookingDate(LocalDate.now());
        invoice.setMovieName(movie.getMovieNameEnglish());
        invoice.setScheduleShow(LocalDate.now());
        invoice.setScheduleShowTime(schedule);
        invoice.setTotalMoney(BigDecimal.valueOf(totalPrice));
        invoice.setSeat(selectedSeats);
        invoice.setStatus(false);
        invoice.setAccount(account);

        invoiceRepository.save(invoice);

        return true;

    }

    @Override
    public List<ScheduleSeat> findBookedSeatsByMovieAndScheduleAndDate(UUID movieId, String scheduleTime,
            LocalDate seatDate) {
        return scheduleSeatRepository.findBookedSeatsByMovieAndScheduleAndDate(movieId, scheduleTime, seatDate);
    }

}
