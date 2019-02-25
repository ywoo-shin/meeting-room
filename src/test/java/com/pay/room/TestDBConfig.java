package com.pay.room;

import com.pay.room.domain.member.Member;
import com.pay.room.domain.member.MemberUuid;
import com.pay.room.domain.member.repository.MemberRepository;
import com.pay.room.domain.reservation.Reservation;
import com.pay.room.domain.reservation.ReserveUuid;
import com.pay.room.domain.reservation.repository.ReservationRepository;
import com.pay.room.domain.room.MeetingRoom;
import com.pay.room.domain.room.repository.MeetingRoomRepository;
import com.pay.room.web.reservation.protocol.UpdateReservationRequestV1;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "fixture-db")
@Getter
public class TestDBConfig {
	@Autowired private MemberRepository memberRepository;
	@Autowired private MeetingRoomRepository meetingRoomRepository;
	@Autowired private ReservationRepository reservationRepository;

	private UpdateReservationRequestV1 updateReservationRequestV1 = new UpdateReservationRequestV1();

	private MemberUuid memberUuid1 = new MemberUuid();
	private MemberUuid memberUuid2 = new MemberUuid();
	private Member member1;
	private Member member2;
	private List<MeetingRoom> meetingRoomList;

	private ReserveUuid reserveUuid1 = new ReserveUuid();
	private Reservation reservation1 = new Reservation();
	private Reservation reservation2DB;
	private List<Reservation> reservationList;

	@PostConstruct
	public void initData() {
		member1 = memberRepository.findById(memberUuid1).get();
		member2 = memberRepository.findById(memberUuid2).get();
		meetingRoomList = meetingRoomRepository.findAll();

		reservation2DB = reservationRepository.findById(reserveUuid1).get();
		reservationList = reservationRepository.findAll();
	}
}
