package com.pay.room.domain.reservation;

import com.pay.room.common.exception.PayException;
import org.junit.Test;

import static com.pay.room.common.exception.PayResultCode.RESERVATION_TIME_INVALID;
import static com.pay.room.common.exception.PayResultCode.RESERVATION_TIME_INVALID_FORMAT;
import static com.pay.room.common.exception.PayResultCode.RESERVATION_TIME_INVALID_INTERVAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ReservationTest {
	@Test
	public void checkValidReserveTime_성공() throws Exception {
		Reservation reservation = new Reservation().setStartReserveTime("09:00")
												   .setEndReserveTime("11:00");

		boolean actual = reservation.checkValidReserveTime();
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void checkValidReserveTime_실패_종료시간이_시작시간보다_큼() throws Exception {
		Reservation reservation = new Reservation().setStartReserveTime("13:00")
												   .setEndReserveTime("11:00");

		assertThatThrownBy(() -> reservation.checkValidReserveTime()).isInstanceOf(PayException.class)
																	 .hasMessage(RESERVATION_TIME_INVALID.getMessage());
	}

	@Test
	public void checkValidReserveTime_실패_시간_포맷이_이상함() throws Exception {
		Reservation reservation = new Reservation().setStartReserveTime("3:00")
												   .setEndReserveTime("1100");

		assertThatThrownBy(() -> reservation.checkValidReserveTime()).isInstanceOf(PayException.class)
																	 .hasMessage(RESERVATION_TIME_INVALID_FORMAT.getMessage());
	}

	@Test
	public void checkValidReserveTime_실패_정각_30분_간격이_아님() throws Exception {
		Reservation reservation = new Reservation().setStartReserveTime("11:20")
												   .setEndReserveTime("12:50");

		assertThatThrownBy(() -> reservation.checkValidReserveTime()).isInstanceOf(PayException.class)
																	 .hasMessage(RESERVATION_TIME_INVALID_INTERVAL.getMessage());
	}
}