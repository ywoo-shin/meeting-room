package com.pay.room.web.reservation.protocol.v1;

import com.pay.room.domain.member.Member;
import com.pay.room.domain.reservation.Reservation;
import com.pay.room.web.protocol.PayResponse;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Data
@EqualsAndHashCode(callSuper = true)
public class ListReservationResponseV1 extends PayResponse {
	private List<MeetingRoomProtocol> rooms;

	@ApiModel("ListReservationResponseV1@MeetingRoomProtocol")
	@Data
	@Builder
	@NoArgsConstructor(access = PROTECTED)
	@AllArgsConstructor
	private static class MeetingRoomProtocol {
		private String meetingRoomUuid;
		private String meetingRoomName;
		private List<ReservationProtocol> reservations;
	}

	@ApiModel("ListReservationResponseV1@ReservationProtocol")
	@Data
	@Builder
	@NoArgsConstructor(access = PROTECTED)
	@AllArgsConstructor
	private static class ReservationProtocol {
		private String memberUuid;
		private String memberName;
		private List<ReservationTimeProtocol> reservedTimes;
	}

	@ApiModel("ListReservationResponseV1@ReservationTimeProtocol")
	@Data
	@Builder
	@NoArgsConstructor(access = PROTECTED)
	@AllArgsConstructor
	private static class ReservationTimeProtocol {
		private String reserveUuid;
		private int repeatCount;
		private String reserveTime;
	}

	public ListReservationResponseV1 toProtocol(List<Reservation> list) {
		this.rooms = list.stream()
						 .collect(Collectors.groupingBy(Reservation::getMeetingRoom))
						 .entrySet()
						 .stream()
						 .map(roomEntry -> {
							 Function<Map.Entry<Member, List<Reservation>>, ReservationProtocol> reservedProtocol =
								 memberEntry -> {
									 Function<Reservation, ReservationTimeProtocol> timeProtocol =
										 reservation -> ReservationTimeProtocol.builder()
																			   .reserveUuid(reservation.getReserveUuid())
																			   .repeatCount(reservation.getRepeatCount())
																			   .reserveTime(reservation.getStartReserveTime() + "~" + reservation.getEndReserveTime())
																			   .build();

									 List<ReservationTimeProtocol> timeProtocolList = memberEntry.getValue()
																								 .stream()
																								 .map(timeProtocol)
																								 .collect(Collectors.toList());

									 return ReservationProtocol.builder()
															   .memberUuid(memberEntry.getKey().getMemberUuid())
															   .memberName(memberEntry.getKey().getMemberName())
															   .reservedTimes(timeProtocolList)
															   .build();
								 };

							 return MeetingRoomProtocol.builder()
													   .meetingRoomUuid(roomEntry.getKey().getRoomUuid())
													   .meetingRoomName(roomEntry.getKey().getRoomName())
													   .reservations(roomEntry.getValue()
																			  .stream()
																			  .collect(Collectors.groupingBy(Reservation::getMember))
																			  .entrySet()
																			  .stream()
																			  .map(reservedProtocol)
																			  .collect(Collectors.toList()))
													   .build();
						 })
						 .collect(Collectors.toList());
		return this;
	}
}
