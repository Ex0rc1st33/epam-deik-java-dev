package com.epam.training.ticketservice.backend.booking.service.impl;

import com.epam.training.ticketservice.backend.booking.model.BookingDto;
import com.epam.training.ticketservice.backend.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.backend.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.backend.booking.service.BookingService;
import com.epam.training.ticketservice.backend.room.persistence.entity.Room;
import com.epam.training.ticketservice.backend.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.backend.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.backend.screening.persistence.repository.ScreeningRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private Integer basePrice = 1500;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final ScreeningRepository screeningRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              RoomRepository roomRepository,
                              ScreeningRepository screeningRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.screeningRepository = screeningRepository;
    }

    @Override
    public String createBooking(BookingDto bookingDto) {
        Objects.requireNonNull(bookingDto, "Booking cannot be null");
        Objects.requireNonNull(bookingDto.getMovieTitle(), "Booking movieTitle cannot be null");
        Objects.requireNonNull(bookingDto.getRoomName(), "Booking roomName cannot be null");
        Objects.requireNonNull(bookingDto.getStartedAt(), "Booking startedAt cannot be null");
        Objects.requireNonNull(bookingDto.getSeats(), "Booking seats cannot be null");
        Objects.requireNonNull(bookingDto.getUser(), "Booking user cannot be null");
        Optional<Screening> screening = screeningRepository
                .findByMovieTitleAndRoomNameAndStartedAt(bookingDto.getMovieTitle(),
                        bookingDto.getRoomName(),
                        bookingDto.getStartedAt());
        if (screening.isEmpty()) {
            return "Screening does not exist";
        }
        String seat = getTakenSeat(bookingDto);
        if (seat != null) {
            return "Seat (" + seat + ") is already taken";
        }
        seat = getNonExistantSeat(bookingDto);
        if (seat != null) {
            return "Seat (" + seat + ") does not exist in this room";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append("Seats booked:");
        String[] seats = bookingDto.getSeats().split(" ");
        for (String s : seats) {
            buffer.append(" (").append(s).append("),");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append("; the price for this booking is ").append(basePrice * seats.length).append(" HUF");
        Booking booking = new Booking(bookingDto.getMovieTitle(),
                bookingDto.getRoomName(),
                bookingDto.getStartedAt(),
                bookingDto.getSeats(),
                bookingDto.getUser(),
                basePrice);
        bookingRepository.save(booking);
        return buffer.toString();
    }

    @Override
    public List<BookingDto> getBookingsByUser(String user) {
        return bookingRepository.findAllByUser(user).stream().map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public String updateBasePrice(Integer basePrice) {
        this.basePrice = basePrice;
        return "Base price changed to " + this.basePrice;
    }

    @Override
    public Integer getBasePriceOfExistingBooking(BookingDto bookingDto) {
        Optional<Booking> booking = bookingRepository
                .findByMovieTitleAndRoomNameAndStartedAtAndUserAndSeats(bookingDto.getMovieTitle(),
                bookingDto.getRoomName(),
                bookingDto.getStartedAt(),
                bookingDto.getUser(),
                bookingDto.getSeats());
        return booking.orElseThrow().getBasePrice();
    }

    public Integer getBasePrice() {
        return basePrice;
    }

    private BookingDto convertEntityToDto(Booking booking) {
        return BookingDto.builder()
                .withMovieTitle(booking.getMovieTitle())
                .withRoomName(booking.getRoomName())
                .withStartedAt(booking.getStartedAt())
                .withSeats(booking.getSeats())
                .withUser(booking.getUser())
                .build();
    }

    private String getTakenSeat(BookingDto bookingDto) {
        String[] newSeats = bookingDto.getSeats().split(" ");
        List<Booking> bookings = bookingRepository
                .findAllByMovieTitleAndRoomNameAndStartedAt(bookingDto.getMovieTitle(),
                        bookingDto.getRoomName(),
                        bookingDto.getStartedAt());
        for (String newSeat : newSeats) {
            for (Booking booking : bookings) {
                String[] takenSeats = booking.getSeats().split(" ");
                for (String takenSeat : takenSeats) {
                    if (newSeat.equals(takenSeat)) {
                        return newSeat;
                    }
                }
            }
        }
        return null;
    }

    private String getNonExistantSeat(BookingDto bookingDto) {
        String[] newSeats = bookingDto.getSeats().split(" ");
        Optional<Room> roomOptional = roomRepository.findById(bookingDto.getRoomName());
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            for (String newSeat : newSeats) {
                String[] rowAndCol = newSeat.split(",");
                if (Integer.parseInt(rowAndCol[0]) > room.getRowCount()
                        || Integer.parseInt(rowAndCol[0]) == 0
                        || Integer.parseInt(rowAndCol[1]) > room.getColCount()
                        || Integer.parseInt(rowAndCol[1]) == 0) {
                    return newSeat;
                }
            }
        }
        return null;
    }

}
