package com.pay.room;

import com.pay.room.domain.reservation.Reservation;
import com.pay.room.web.reservation.protocol.AddReservationRequestV1;
import lombok.Getter;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
@ConfigurationProperties(prefix = "fixture")
@Getter
public class TestConfig {
	private AddReservationRequestV1 addReservationRequestV1 = new AddReservationRequestV1();

	private Reservation reservationSuccess_1 = new Reservation();
	private Reservation reservationSuccess_2 = new Reservation();
	private Reservation reservationFail_1 = new Reservation();
	private Reservation reservationFail_2 = new Reservation();
	private Reservation reservationFail_3 = new Reservation();

	@Profile("test")
	public static class FixtureConfig {
		@Bean
		public PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
			PropertySourcesPlaceholderConfigurer placeholderConfigurer = new PropertySourcesPlaceholderConfigurer();
			YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
			yaml.setResources(new ClassPathResource("fixture.yml"),
							  new ClassPathResource("fixture-db.yml"));
			placeholderConfigurer.setProperties(yaml.getObject());
			return placeholderConfigurer;
		}
	}
}
