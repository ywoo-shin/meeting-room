package com.pay.room.domain.room.repository;

import com.pay.room.domain.room.MeetingRoom;
import com.pay.room.domain.room.MeetingRoomUuid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, MeetingRoomUuid> {
}
