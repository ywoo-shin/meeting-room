package com.pay.room.domain.member;

import com.pay.room.common.model.ID;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Embeddable
public class MemberUuid implements ID, Serializable {
	private static final long serialVersionUID = -1248553418027184225L;

	@Column(name = "memberUuid")
	private String id;

	public static MemberUuid of(String id) {
		return new MemberUuid(id);
	}

	@Override
	public boolean isEmpty() {
		return StringUtils.isEmpty(id);
	}

	@Override
	public boolean isNotEmpty() {
		return StringUtils.isNotEmpty(id);
	}
}
