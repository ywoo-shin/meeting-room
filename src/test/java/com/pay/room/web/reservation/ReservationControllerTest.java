package com.pay.room.web.reservation;

import com.pay.room.TestBoot;
import com.pay.room.domain.reservation.Reservation;
import com.pay.room.domain.reservation.ReserveUuid;
import com.pay.room.domain.reservation.service.ReservationService;
import com.pay.room.web.protocol.PayResponse;
import com.pay.room.web.protocol.PayResponseHeader;
import com.pay.room.web.reservation.protocol.v1.ListReservationResponseV1;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.Date;

import static com.pay.room.web.ApiMap.ROOMS_MEMBERS_RESERVATIONS;
import static com.pay.room.web.ApiMap.ROOMS_MEMBERS_RESERVATIONS_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

public class ReservationControllerTest extends TestBoot {
	@MockBean private ReservationService mockService;

	@Before
	public void setUp() {
	}

	@Test
	public void getList() throws Exception {
		given(mockService.list(any(Date.class))).willReturn(testDBConfig.getReservationList());
		testClient.get()
				  .uri(ROOMS_MEMBERS_RESERVATIONS + "?reserveDate={reserveDate}", "2019-02-23")
				  .exchange()
				  .expectStatus().isOk()
				  .expectBody(ListReservationResponseV1.class)
				  .consumeWith(result -> {
					  assertThat(result.getResponseBody()
									   .getRooms()).isNotNull()
												   .isEqualTo(PayResponse.success(ListReservationResponseV1.class)
																		 .toProtocol(testDBConfig.getReservationList()).getRooms());
				  });
	}

	@Test
	public void register() throws Exception {
		Reservation inputData = testDBConfig.getReservation1();
		given(mockService.register(any(Reservation.class), anyInt())).willReturn(inputData);

		testClient.post()
				  .uri(ROOMS_MEMBERS_RESERVATIONS)
				  .contentType(MediaType.APPLICATION_JSON_UTF8)
				  .body(BodyInserters.fromObject(testConfig.getAddReservationRequestV1()))
				  .exchange()
				  .expectStatus().isOk()
				  .expectBody(PayResponse.class)
				  .consumeWith(result -> assertThat(result.getResponseBody()
														  .getHeader()).isNotNull()
																	   .extracting(PayResponseHeader::getCode)
																	   .isEqualTo(0));
	}

	@Test
	public void update() throws Exception {
		Reservation updateData = testDBConfig.getReservation1();
		given(mockService.update(any(Reservation.class))).willReturn(updateData);

		testClient.put()
				  .uri(ROOMS_MEMBERS_RESERVATIONS_UUID, updateData.getReserveUuid())
				  .contentType(MediaType.APPLICATION_JSON_UTF8)
				  .body(BodyInserters.fromObject(testDBConfig.getUpdateReservationRequestV1()))
				  .exchange()
				  .expectStatus().isOk()
				  .expectBody(PayResponse.class)
				  .consumeWith(result -> assertThat(result.getResponseBody()
														  .getHeader()).isNotNull()
																	   .extracting(PayResponseHeader::getCode)
																	   .isEqualTo(0));
	}

	@Test
	public void delete() throws Exception {
		doNothing().when(mockService).delete(any(ReserveUuid.class));

		testClient.delete()
				  .uri(ROOMS_MEMBERS_RESERVATIONS_UUID, testDBConfig.getReservation1())
				  .exchange()
				  .expectStatus().isOk()
				  .expectBody(PayResponse.class)
				  .consumeWith(result -> assertThat(result.getResponseBody()
														  .getHeader()).isNotNull()
																	   .extracting(PayResponseHeader::getCode)
																	   .isEqualTo(0));
	}
}