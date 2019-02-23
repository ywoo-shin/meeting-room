package com.pay.room.web.reservation;

import com.pay.room.common.util.DateUtil;
import com.pay.room.domain.reservation.Reservation;
import com.pay.room.domain.reservation.ReserveUuid;
import com.pay.room.domain.reservation.service.ReservationService;
import com.pay.room.web.protocol.PayResponse;
import com.pay.room.web.reservation.protocol.AddReservationRequestV1;
import com.pay.room.web.reservation.protocol.UpdateReservationRequestV1;
import com.pay.room.web.reservation.protocol.v1.ListReservationResponseV1;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.pay.room.web.ApiMap.ROOMS_MEMBERS_RESERVATIONS;
import static com.pay.room.web.ApiMap.ROOMS_MEMBERS_RESERVATIONS_UUID;

@Api("회의실 예약 API")
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
public class ReservationController {
	@Autowired private ReservationService reservationService;

	@ApiOperation("회의실 에약 리스트 조회")
	@GetMapping(ROOMS_MEMBERS_RESERVATIONS)
	public ListReservationResponseV1 getList(@ApiParam("예약일자 (ex: 2019-02-25)")
											 @RequestParam String reserveDate) {
		List<Reservation> list = reservationService.list(DateUtil.convertStringToDate(reserveDate, "yyyy-MM-dd"));

		return PayResponse.success(ListReservationResponseV1.class)
						  .toProtocol(list);
	}

	@ApiOperation("회의실 에약 등록")
	@PostMapping(ROOMS_MEMBERS_RESERVATIONS)
	public PayResponse register(@Valid @RequestBody AddReservationRequestV1 request) {
		reservationService.register(request.toModel(), request.getRepeatCount());
		return PayResponse.success();
	}

	@ApiOperation("회의실 에약 수정")
	@PutMapping(ROOMS_MEMBERS_RESERVATIONS_UUID)
	public PayResponse update(@PathVariable("reserve-uuid") String reserveUuid,
							  @Valid @RequestBody UpdateReservationRequestV1 request) {
		reservationService.update(request.toModel(reserveUuid));
		return PayResponse.success();
	}

	@ApiOperation("회의실 에약 삭제")
	@DeleteMapping(ROOMS_MEMBERS_RESERVATIONS_UUID)
	public PayResponse delete(@PathVariable("reserve-uuid") String reserveUuid) {
		reservationService.delete(ReserveUuid.of(reserveUuid));
		return PayResponse.success();
	}
}
