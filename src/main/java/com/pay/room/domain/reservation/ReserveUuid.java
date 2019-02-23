package com.pay.room.domain.reservation;

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
public class ReserveUuid implements ID, Serializable {
	private static final long serialVersionUID = -1248553418027184225L;

	@Column(name = "reserveUuid")
	private String id;

	public static ReserveUuid of(String id) {
		return new ReserveUuid(id);
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
