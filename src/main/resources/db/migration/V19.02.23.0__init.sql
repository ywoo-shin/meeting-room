CREATE TABLE `member` (
  `member_uuid` varchar(16) NOT NULL,
  `member_name` varchar(20) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`member_uuid`)
);

CREATE TABLE `meeting_room` (
  `room_uuid` varchar(16) NOT NULL,
  `room_name` varchar(20) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`room_uuid`)
);

CREATE TABLE `reservation` (
  `reserve_uuid` varchar(16) NOT NULL,
  `member_uuid` varchar(16) NOT NULL,
  `room_uuid` varchar(16) NOT NULL,
  `reserve_date` DATE NOT NULL,
  `repeat_count` INT(2) NOT NULL DEFAULT 1,
  `start_reserve_time` varchar(5) NOT NULL,
  `end_reserve_time` varchar(5) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `ix_reservation_01` (`member_uuid`),
  KEY `ix_reservation_02` (`room_uuid`),
  KEY `ix_reservation_03` (`start_reserve_time`),
  KEY `ix_reservation_04` (`end_reserve_time`),
  PRIMARY KEY (`reserve_uuid`)
);

INSERT INTO member (member_uuid, member_name) VALUES ('YxBz38BgjyhcxKR0', '신용우');
INSERT INTO member (member_uuid, member_name) VALUES ('6WERwjK2rGkIOtUY', '홍길동');

INSERT INTO meeting_room (room_uuid, room_name) VALUES ('yTDxb0WaZc4Vpswv', '회의실 A');
INSERT INTO meeting_room (room_uuid, room_name) VALUES ('XdrM645PBcjrTPKe', '회의실 B');
INSERT INTO meeting_room (room_uuid, room_name) VALUES ('IdMzvaqmuQEYyp4G', '회의실 C');
INSERT INTO meeting_room (room_uuid, room_name) VALUES ('XlqH9cXA6pcAhwls', '회의실 D');
INSERT INTO meeting_room (room_uuid, room_name) VALUES ('evMjPeXvtSZ3q7yA', '회의실 E');
INSERT INTO meeting_room (room_uuid, room_name) VALUES ('CdLI2oq7vXQVd6oe', '회의실 F');
INSERT INTO meeting_room (room_uuid, room_name) VALUES ('OOuRmW25jVzfELho', '회의실 G');
