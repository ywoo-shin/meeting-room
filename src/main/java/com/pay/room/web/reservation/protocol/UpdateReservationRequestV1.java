package com.pay.room.web.reservation.protocol;

import com.pay.room.domain.reservation.Reservation;
import com.pay.room.domain.reservation.ReserveUuid;
import com.pay.room.domain.room.MeetingRoom;
import com.pay.room.domain.room.MeetingRoomUuid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class UpdateReservationRequestV1 {
	@NotNull private String meetingRoomUuid;

	@ApiModelProperty(value = "시작 예약시간", example = "09:00", required = true)
	@NotNull
	private String startReserveTime;

	@ApiModelProperty(value = "종료 예약시간", example = "14:00", required = true)
	@NotNull
	private String endReserveTime;

	public Reservation toModel(String reserveUuid) {
		return Reservation.builder()
						  .reserveUuid(ReserveUuid.of(reserveUuid))
						  .meetingRoom(new MeetingRoom(MeetingRoomUuid.of(meetingRoomUuid)))
						  .startReserveTime(startReserveTime)
						  .endReserveTime(endReserveTime)
						  .build();
	}
}
