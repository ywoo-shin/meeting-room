package com.pay.room.domain.member.repository;

import com.pay.room.domain.member.Member;
import com.pay.room.domain.member.MemberUuid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, MemberUuid> {
}
