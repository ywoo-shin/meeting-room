package com.pay.room.domain.member.service;

import com.pay.room.common.exception.PayException;
import com.pay.room.domain.member.Member;
import com.pay.room.domain.member.MemberUuid;
import com.pay.room.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.pay.room.common.exception.PayResultCode.REPOSITORY_DATA_NOT_EXIST;

@Service
@Transactional(readOnly = true)
public class MemberService {
	@Autowired private MemberRepository repository;

	public Member getMember(MemberUuid memberUuid) {
		return repository.findById(memberUuid)
						 .orElseThrow(() -> PayException.getInstance(REPOSITORY_DATA_NOT_EXIST));
	}

	public List<Member> getList() {
		return repository.findAll();
	}
}
