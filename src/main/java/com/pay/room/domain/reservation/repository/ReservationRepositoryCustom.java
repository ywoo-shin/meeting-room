package com.pay.room.domain.reservation.repository;

import com.pay.room.domain.reservation.Reservation;
import com.querydsl.core.types.OrderSpecifier;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ReservationRepositoryCustom {
	Optional<List<Reservation>> findReservationBySearch(ReservationSearch search);
	Optional<List<Reservation>> findByReserveDate(Date reserveDate, OrderSpecifier... orders);
}
