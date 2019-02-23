package com.pay.room.domain.reservation.repository;

import com.pay.room.domain.reservation.Reservation;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.pay.room.domain.member.QMember.member;
import static com.pay.room.domain.reservation.QReservation.reservation;
import static com.pay.room.domain.room.QMeetingRoom.meetingRoom;

public class ReservationRepositoryImpl extends QuerydslRepositorySupport implements ReservationRepositoryCustom {

	public ReservationRepositoryImpl() {
		super(Reservation.class);
	}

	@Override
	public Optional<List<Reservation>> findReservationBySearch(ReservationSearch search) {
		return Optional.ofNullable(executeQuery(search).fetch());
	}

	@Override
	public Optional<List<Reservation>> findByReserveDate(Date reserveDate, OrderSpecifier... orders) {
		return Optional.ofNullable(executeQuery(ReservationSearch.builder()
																 .reserveDate(reserveDate)
																 .build()).orderBy(orders)
																		  .fetch());
	}

	private JPQLQuery<Reservation> executeQuery(ReservationSearch search) {
		return from(reservation).join(reservation.member, member).fetchJoin()
								.join(reservation.meetingRoom, meetingRoom).fetchJoin()
								.distinct()
								.where(search.getCondition());
	}
}
