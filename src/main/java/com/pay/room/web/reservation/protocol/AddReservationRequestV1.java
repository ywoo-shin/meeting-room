package com.pay.room.web.reservation.protocol;

import com.pay.room.domain.member.Member;
import com.pay.room.domain.member.MemberUuid;
import com.pay.room.domain.reservation.Reservation;
import com.pay.room.domain.room.MeetingRoom;
import com.pay.room.domain.room.MeetingRoomUuid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Accessors(chain = true)
public class AddReservationRequestV1 {

	@NotNull private String meetingRoomUuid;
	@NotNull private String memberUuid;

	@ApiModelProperty(value = "예약일자", example = "2019-02-23", required = true)
	@NotNull
	private Date reserveDate;

	@ApiModelProperty(value = "시작 예약시간", example = "09:00", required = true)
	@NotNull
	private String startReserveTime;

	@ApiModelProperty(value = "종료 예약시간", example = "14:00", required = true)
	@NotNull
	private String endReserveTime;

	@ApiModelProperty(value = "예약 반속 횟수", example = "5")
	private int repeatCount = 1;

	public Reservation toModel() {
		return Reservation.builder()
						  .meetingRoom(new MeetingRoom(MeetingRoomUuid.of(meetingRoomUuid)))
						  .member(new Member(MemberUuid.of(memberUuid)))
						  .reserveDate(reserveDate)
						  .startReserveTime(startReserveTime)
						  .endReserveTime(endReserveTime)
						  .build();
	}
}
