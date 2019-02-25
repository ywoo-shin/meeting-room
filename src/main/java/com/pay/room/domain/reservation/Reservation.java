package com.pay.room.domain.reservation;

import com.pay.room.common.exception.PayException;
import com.pay.room.common.exception.PayResultCode;
import com.pay.room.common.model.BaseDateTime;
import com.pay.room.common.util.KeyUtil;
import com.pay.room.domain.member.Member;
import com.pay.room.domain.member.MemberUuid;
import com.pay.room.domain.reservation.repository.ReservationSearch;
import com.pay.room.domain.room.MeetingRoom;
import com.pay.room.domain.room.MeetingRoomUuid;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.pay.room.common.exception.PayResultCode.*;

@Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Slf4j
@Entity
@Data
public class Reservation extends BaseDateTime {
    @Id
    @Embedded
    private ReserveUuid reserveUuid;

    @ManyToOne
    @JoinColumn(name = "memberUuid")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "roomUuid")
    private MeetingRoom meetingRoom;

    @Temporal(TemporalType.DATE) private Date reserveDate;
    private int repeatCount;
    private String startReserveTime;    // ex: format (09:00)
    private String endReserveTime;      // ex: format (13:30)

    public MemberUuid getMemberUuid() {
        return Optional.ofNullable(member)
                       .map(Member::getMemberUuidObject)
                       .orElse(null);
    }

    public MeetingRoomUuid getMeetingRoomUuid() {
        return Optional.ofNullable(meetingRoom)
                       .map(MeetingRoom::getRoomUuidObject)
                       .orElse(null);
    }

    public Reservation setMember(Member member) {
        if (this.member != null) {
            this.member.getReservationList().remove(this);
        }
        this.member = member;

        if(this.member != null) {
            this.member.getReservationList().add(this);
        }
        return this;
    }

    public Reservation setMeetingRoom(MeetingRoom meetingRoom) {
        if (this.meetingRoom != null) {
            this.meetingRoom.getReservationList().remove(this);
        }
        this.meetingRoom = meetingRoom;

        if(this.meetingRoom != null) {
            this.meetingRoom.getReservationList().add(this);
        }
        return this;
    }

    public ReserveUuid getReserveUuidObject() {
        return reserveUuid;
    }

    public String getReserveUuid() {
        return Optional.ofNullable(reserveUuid)
                       .filter(ReserveUuid::isNotEmpty)
                       .map(ReserveUuid::getId)
                       .orElse(null);
    }

    public Reservation initReserveUuid() {
        this.reserveUuid = new ReserveUuid(KeyUtil.createUuidTo16());
        return this;
    }

    public boolean isModifiedData() {
        return reserveUuid != null && reserveUuid.isNotEmpty();
    }

    public ReservationSearch getConditionIsReservation() {
        if (meetingRoom == null) {
            throw PayException.getInstance(PayResultCode.INVALID_PARAMETER);
        }
        return ReservationSearch.builder()
                                .roomUuid(meetingRoom.getRoomUuidObject())
                                .reserveDate(reserveDate)
                                .startReserveTime(startReserveTime)
                                .endReserveTime(endReserveTime)
                                .build();
    }

    public boolean checkValidReserveTime() {
        Pattern timePattern = Pattern.compile("\\d{2}:\\d{2}");
        if (timePattern.matcher(startReserveTime).matches() == false
            || timePattern.matcher(endReserveTime).matches() == false) {
            throw PayException.getInstance(RESERVATION_TIME_INVALID_FORMAT);
        }

        if (endReserveTime.compareTo(startReserveTime) <= 0) {
            throw PayException.getInstance(RESERVATION_TIME_INVALID);
        }
        // 정시 30분 간곅 단위 예약인지 체크
        if (isValidInterval() == false) {
            throw PayException.getInstance(RESERVATION_TIME_INVALID_INTERVAL);
        }
        return true;
    }

    public void bindingImutableValue(Reservation reservationDB) {
        this.member = reservationDB.getMember();
        this.reserveDate = reservationDB.getReserveDate();

        if (this.startReserveTime.equals(reservationDB.getStartReserveTime()) && this.endReserveTime.equals(reservationDB.getEndReserveTime())) {
            this.repeatCount = reservationDB.getRepeatCount();
        }
    }

    private boolean isValidInterval() {
        try {
            int startMinute = Integer.valueOf(startReserveTime.split(":")[1]);
            int endMinute = Integer.valueOf(endReserveTime.split(":")[1]);

            return (startMinute == 0 || startMinute == 30) && (endMinute == 0 || endMinute == 30);

        } catch (IndexOutOfBoundsException e) {
            log.error("check the isValidInterval(): ", e);
            return false;
        }
    }
}
