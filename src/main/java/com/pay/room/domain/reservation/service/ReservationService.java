package com.pay.room.domain.reservation.service;

import com.pay.room.common.exception.PayException;
import com.pay.room.common.exception.PayResultCode;
import com.pay.room.common.util.DateUtil;
import com.pay.room.common.util.ModelMapperUtil;
import com.pay.room.domain.member.service.MemberService;
import com.pay.room.domain.reservation.Reservation;
import com.pay.room.domain.reservation.ReserveUuid;
import com.pay.room.domain.reservation.repository.ReservationRepository;
import com.pay.room.domain.room.service.MeetingRoomService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.pay.room.common.exception.PayResultCode.REPOSITORY_DATA_NOT_EXIST;
import static com.pay.room.domain.reservation.QReservation.reservation;

@Service
@Transactional(readOnly = true)
public class ReservationService {
	@Autowired private ReservationRepository repository;
	@Autowired private MemberService memberService;
	@Autowired private MeetingRoomService meetingRoomService;

	@Value("${spring.jpa.properties.hibernate.jdbc.batch-size}")
	private int batchSize;

	public List<Reservation> list(Date reserveDate) {
		return repository.findByReserveDate(reserveDate,
											reservation.member.memberName.asc(),
											reservation.startReserveTime.asc())
						 .orElse(Collections.emptyList());
	}

	@Transactional
	public Reservation register(Reservation reservation, int repeatCount) {
		reservation.checkValidReserveTime();
		checkIsReserve(reservation);

		// 반복 예약
		if (repeatCount > 1) {
			return bulkRegister(reservation, repeatCount).get(0);
		}
		return repository.save(reservation.initReserveUuid());
	}

	protected List<Reservation> bulkRegister(Reservation reservation, int repeatCount) {
		LocalDate start = DateUtil.convertDateToLocalDate(reservation.getReserveDate());
		LocalDate end = start.plusDays(repeatCount * 7);

		List<Reservation> reservationList = new ArrayList<>();
		int count = 0;
		for (LocalDate date = start; date.isBefore(end); date = date.plusDays(7)) {
			Reservation reservationDB =  repository.save(reservation.initReserveUuid()
																	.setRepeatCount(repeatCount--)
																	.setReserveDate(DateUtil.convertLocalDateToDate(date)));
			if (count % batchSize == 0) {
				repository.flush();
			}

			reservationList.add(reservationDB);
			count++;
		}
		return reservationList;
	}

	@Transactional
	public Reservation update(Reservation reservation) {
		Reservation reservationDB = repository.findById(reservation.getReserveUuidObject())
											  .orElseThrow(() -> PayException.getInstance(REPOSITORY_DATA_NOT_EXIST));
		reservation.checkValidReserveTime();
		reservation.bindingImutableValue(reservationDB);
		checkIsReserve(reservation);

		ModelMapperUtil.mapSkipNull(reservation, reservationDB);
		return repository.save(reservationDB);
	}

	@Transactional
	public void delete(ReserveUuid reserveUuid) {
		repository.deleteById(reserveUuid);
	}

	private void checkIsReserve(Reservation reservation) {
		// check 멤버, 회의실
		memberService.getMember(reservation.getMemberUuid());
		meetingRoomService.getMeetingRoom(reservation.getMeetingRoomUuid());

		repository.findReservationBySearch(reservation.getConditionIsReservation())
				  .ifPresent(list -> {

					  if (reservation.isModifiedData()) {
						  list = list.stream()
									 .filter(data -> data.getReserveUuid() != reservation.getReserveUuid())
									 .collect(Collectors.toList());
					  }

					  if (CollectionUtils.isNotEmpty(list)) {
						  throw PayException.getInstance(PayResultCode.RESERVE_FAIL_ALREADY_RESERVATION);
					  }
				  });
	}
}
