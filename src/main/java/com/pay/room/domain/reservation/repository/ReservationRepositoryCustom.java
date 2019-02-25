package com.pay.room.domain.reservation.repository;

import com.pay.room.domain.reservation.Reservation;
import com.querydsl.core.types.OrderSpecifier;

import java.util.Date;
import java.util.List;

public interface ReservationRepositoryCustom {
	List<Reservation> findReservationBySearch(ReservationSearch search);
	List<Reservation> findByReserveDate(Date reserveDate, OrderSpecifier... orders);
}
