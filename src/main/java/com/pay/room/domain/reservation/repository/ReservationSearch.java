package com.pay.room.domain.reservation.repository;

import com.pay.room.common.exception.PayException;
import com.pay.room.domain.reservation.ReserveUuid;
import com.pay.room.domain.room.MeetingRoomUuid;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import static com.pay.room.common.exception.PayResultCode.INVALID_PARAMETER;
import static com.pay.room.domain.reservation.QReservation.reservation;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSearch {
	private ReserveUuid reserveUuid;

	private MeetingRoomUuid roomUuid;
	private Date reserveDate;
	private String startReserveTime;
	private String endReserveTime;

	public Predicate getCondition() {
		BooleanBuilder builder = new BooleanBuilder();

		// 예약 시간 질의
		if (StringUtils.isNotEmpty(startReserveTime) && StringUtils.isNotEmpty(endReserveTime)) {
			// start < {start} and end > {start}
			BooleanExpression e1 = reservation.startReserveTime.lt(startReserveTime).and(reservation.endReserveTime.gt(startReserveTime));
			// start < {end} and end > {end}
			BooleanExpression e2 = reservation.startReserveTime.lt(endReserveTime).and(reservation.endReserveTime.gt(endReserveTime));
			// start > {start} and start < {end}
			BooleanExpression e3 = reservation.startReserveTime.gt(startReserveTime).and(reservation.startReserveTime.lt(endReserveTime));
			// end > {start} and end < {end}
			BooleanExpression e4 = reservation.endReserveTime.gt(startReserveTime).and(reservation.endReserveTime.lt(endReserveTime));
			// start = {start} and end = {end}
			BooleanExpression e5 = reservation.startReserveTime.eq(startReserveTime).and(reservation.endReserveTime.eq(endReserveTime));

			builder.and(e1.or(e2)
						  .or(e3)
						  .or(e4)
						  .or(e5));
		}

		if (reserveUuid != null && reserveUuid.isNotEmpty()) {
			builder.and(reservation.reserveUuid.eq(reserveUuid));
		}

		if (roomUuid != null && roomUuid.isNotEmpty()) {
			builder.and(reservation.meetingRoom.roomUuid.eq(roomUuid));
		}
		if (reserveDate != null) {
			builder.and(reservation.reserveDate.eq(reserveDate));
		}

		if (StringUtils.isEmpty(startReserveTime) && StringUtils.isNotEmpty(endReserveTime)
			|| StringUtils.isNotEmpty(startReserveTime) && StringUtils.isEmpty(endReserveTime)) {
			throw PayException.getInstance(INVALID_PARAMETER);
		}
		return builder;
	}
}