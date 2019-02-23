package com.pay.room.domain.room;

import com.pay.room.common.model.ID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Embeddable
public class MeetingRoomUuid implements ID, Serializable {
	private static final long serialVersionUID = 1354450497394892515L;

	@Column(name = "roomUuid")
	private String id;

	public static MeetingRoomUuid of(String id) {
		return new MeetingRoomUuid(id);
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
