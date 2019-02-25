package com.pay.room.domain.room.service;

import com.pay.room.common.exception.PayException;
import com.pay.room.domain.room.MeetingRoom;
import com.pay.room.domain.room.MeetingRoomUuid;
import com.pay.room.domain.room.repository.MeetingRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.pay.room.common.exception.PayResultCode.REPOSITORY_DATA_NOT_EXIST;

@Service
@Transactional(readOnly = true)
public class MeetingRoomService {
	@Autowired private MeetingRoomRepository repository;

	public MeetingRoom getMeetingRoom(MeetingRoomUuid meetingRoomUuid) {
		return repository.findById(meetingRoomUuid)
						 .orElseThrow(() -> PayException.getInstance(REPOSITORY_DATA_NOT_EXIST));
	}

	public List<MeetingRoom> getList() {
		return repository.findAll();
	}
}
