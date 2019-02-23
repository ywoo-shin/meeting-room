package com.pay.room.web.navigation;

import com.pay.room.common.util.DateUtil;
import com.pay.room.domain.member.service.MemberService;
import com.pay.room.domain.reservation.service.ReservationService;
import com.pay.room.domain.room.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class NavigatorController {
	@Autowired private MeetingRoomService meetingRoomService;
	@Autowired private MemberService memberService;
	@Autowired private ReservationService reservationService;

	@RequestMapping("/meeting")
	public String meetingForm(Model model) {
		model.addAttribute("meetingRooms", meetingRoomService.getList());
		model.addAttribute("members", memberService.getList());
		model.addAttribute("reservations", reservationService.list(DateUtil.getNow()));
		return "meetingRoom";
	}
}
