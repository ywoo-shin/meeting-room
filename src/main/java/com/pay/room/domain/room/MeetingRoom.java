package com.pay.room.domain.room;

import com.pay.room.domain.reservation.Reservation;
import lombok.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter @Setter
@NoArgsConstructor
@ToString(exclude = "reservationList")
@Entity
public class MeetingRoom {
    @Id
    @Embedded
    private MeetingRoomUuid roomUuid;
    private String roomName;

    public MeetingRoom(MeetingRoomUuid roomUuid) {
        this.roomUuid = roomUuid;
    }

    @OneToMany(mappedBy = "meetingRoom")
    private List<Reservation> reservationList = new ArrayList<>();

    public MeetingRoomUuid getRoomUuidObject() {
        return roomUuid;
    }

    public String getRoomUuid() {
        return Optional.ofNullable(roomUuid)
                       .filter(MeetingRoomUuid::isNotEmpty)
                       .map(MeetingRoomUuid::getId)
                       .orElse(null);
    }
}
