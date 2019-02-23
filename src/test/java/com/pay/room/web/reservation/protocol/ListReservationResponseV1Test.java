package com.pay.room.web.reservation.protocol;

import com.pay.room.TestBoot;
import com.pay.room.web.protocol.PayResponse;
import com.pay.room.web.protocol.PayResponseHeader;
import com.pay.room.web.reservation.protocol.v1.ListReservationResponseV1;
import org.junit.Test;

import static com.pay.room.common.exception.PayResultCode.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;

public class ListReservationResponseV1Test extends TestBoot {

	@Test
	public void toProtocol() throws Exception {
		ListReservationResponseV1 response = PayResponse.success(ListReservationResponseV1.class)
														.toProtocol(testDBConfig.getReservationList());

		assertThat(response).isNotNull();
		assertThat(response.getRooms().size()).isEqualTo(1);
		assertThat(response.getHeader()).isNotNull()
										.extracting(PayResponseHeader::getMessage).isEqualTo(SUCCESS.getMessage());
	}
}