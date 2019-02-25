package com.pay.room.domain.reservation.service;

import com.pay.room.TestBoot;
import com.pay.room.common.exception.PayException;
import com.pay.room.common.util.DateUtil;
import com.pay.room.domain.reservation.Reservation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

import static com.pay.room.common.exception.PayResultCode.RESERVE_FAIL_ALREADY_RESERVATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
public class ReservationServiceTest extends TestBoot {
	@Autowired private ReservationService service;

	@Test
	public void bulkRegister() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		int repeatCount = 2;
		List<Reservation> actual = service.bulkRegister(testConfig.getReservationSuccess_1(), repeatCount);
		assertThat(actual.size()).isEqualTo(repeatCount);
	}

	@Test
	public void list() {
		List<Reservation> list = service.list(DateUtil.convertLocalDateToDate(LocalDate.of(2019, 2, 23)));
		assertThat(list).isNotEmpty();
		assertThat(list.get(0).getStartReserveTime()).isEqualTo(testDBConfig.getReservationList().get(0).getStartReserveTime());
	}

	@Test
	public void register_성공() throws Exception {
		Reservation successData = testConfig.getReservationSuccess_2();
		success(successData);
	}

	@Test
	public void register_실패_이미_예약된_시간_1() throws Exception {
		Reservation failData = testConfig.getReservationFail_1();
		fail(failData);
	}

	@Test
	public void register_실패_이미_예약된_시간_2() throws Exception {
		Reservation failData = testConfig.getReservationFail_2();
		fail(failData);
	}

	@Test
	public void register_실패_이미_예약된_시간_3() throws Exception {
		Reservation failData = testConfig.getReservationFail_3();
		fail(failData);
	}

	@Test
	public void modify_성공_1() throws Exception {
		Reservation successData = testDBConfig.getReservation1();
		successData.setStartReserveTime("09:00")
				   .setEndReserveTime("10:30");
		success(successData);
	}

	@Test
	public void modify_성공_2() throws Exception {
		Reservation successData = testDBConfig.getReservation1();
		successData.setStartReserveTime("15:00")
				   .setEndReserveTime("17:30");
		success(successData);
	}

	@Test
	public void modify_실패_이미_예약된_시간_1() throws Exception {
		Reservation failData = testDBConfig.getReservation1();
		failData.setStartReserveTime("09:00")
				.setEndReserveTime("14:30");
		fail(failData);
	}

	@Test
	public void modify_실패_이미_예약된_시간_2() throws Exception {
		Reservation failData = testDBConfig.getReservation1();
		failData.setStartReserveTime("13:30")
				.setEndReserveTime("15:30");
		fail(failData);
	}

	private void success(Reservation successData) {
		Reservation reservationDB;
		if (successData.isModifiedData()) {
			reservationDB = service.update(successData);
		} else {
			reservationDB = service.register(successData, 1);
		}

		assertThat(reservationDB).isNotNull();
		assertThat(reservationDB.getStartReserveTime()).isEqualTo(successData.getStartReserveTime());
		assertThat(reservationDB.getEndReserveTime()).isEqualTo(successData.getEndReserveTime());
	}

	private void fail(Reservation failData) {
		assertThatThrownBy(() -> {
			if (failData.isModifiedData()) {
				service.update(failData);
			} else {
				service.register(failData, 1);
			}
		}).isInstanceOf(PayException.class)
		  .hasMessage(RESERVE_FAIL_ALREADY_RESERVATION.getMessage());
	}
}