package com.pay.room.domain.member;

import com.pay.room.common.model.BaseDateTime;
import com.pay.room.domain.reservation.Reservation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class Member extends BaseDateTime {
    @Id
    @Embedded
    private MemberUuid memberUuid;
    private String memberName;

    public Member(MemberUuid memberUuid) {
        this.memberUuid = memberUuid;
    }

    @OneToMany(mappedBy = "member")
    private List<Reservation> reservationList = new ArrayList<>();

    public MemberUuid getMemberUuidObject() {
        return memberUuid;
    }

    public String getMemberUuid() {
        return Optional.ofNullable(memberUuid)
                       .filter(MemberUuid::isNotEmpty)
                       .map(MemberUuid::getId)
                       .orElse(null);
    }
}
