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
    private final MemberRepository memberRepository;
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
        this.memberRepository = memberRepository;
    }
  

    @Override
    public boolean bookTickets(UUID movieId, String selectedSeats, int totalPrice, String schedule, String time, String username) {
        var account = accountRepository.findByUsername(username);
        var scheduletime = scheduleRepository.findByScheduleTime(schedule);
        var movie = movieRepository.findById(movieId).orElse(null);
      
        String[] seats = selectedSeats.split(",");
        // Lưu thông tin về các ghế đã chọn
        for (String seat : seats) {
            // Kiểm tra định dạng ghế (1A, 2B,...)
            if (seat.length() < 2 || !Character.isDigit(seat.charAt(0)) || !Character.isLetter(seat.charAt(1))) {
                throw new IllegalArgumentException("Invalid seat format: " + seat);
            }

            // Lấy hàng và cột từ định dạng ghế
            int seatRow = Integer.parseInt(seat.substring(0, seat.length() - 1));
            String seatColumn = seat.substring(seat.length() - 1);

            // Lưu vào bảng ScheduleSeat
            ScheduleSeat scheduleSeat = new ScheduleSeat();
            scheduleSeat.setMovieId(movieId);
            scheduleSeat.setScheduleId(scheduletime.getId());
            scheduleSeat.setSeatColumn(seatColumn);
            scheduleSeat.setSeatRow(seatRow);
            scheduleSeat.setSeatStatus(false); // Ghế đang được chọn
            scheduleSeat.setSeatType(true); //check them
            
            scheduleSeatRepository.save(scheduleSeat);
   
        
        }

        // Tạo đối tượng Ticket và lưu vào cơ sở dữ liệu
        Ticket ticket = new Ticket();
        ticket.setPrice(totalPrice);
        ticket.setTicketType(true); 
        ticketRepository.save(ticket);

        // Tạo đối tượng Invoice và lưu vào cơ sở dữ liệu
        Invoice invoice = new Invoice();
        invoice.setBookingDate(LocalDate.now());
        invoice.setMovieName(movie.getMovieNameEnglish());
        invoice.setScheduleShow(LocalDate.now()); 
        invoice.setScheduleShowTime(time); 
        invoice.setTotalMoney(BigDecimal.valueOf(totalPrice));
        invoice.setSeat(selectedSeats); 
        invoice.setAccount(account);
        
        invoiceRepository.save(invoice);

        return true;

    }


    @Override
    public List<ScheduleSeat> getSeatsByScheduleId(UUID scheduleId) {
        return scheduleSeatRepository.findByScheduleId(scheduleId);
    }


}
