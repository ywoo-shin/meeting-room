package com.pay.room.domain.reservation.repository;

import com.pay.room.domain.reservation.Reservation;
import com.pay.room.domain.reservation.ReserveUuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, ReserveUuid>, ReservationRepositoryCustom {
}
