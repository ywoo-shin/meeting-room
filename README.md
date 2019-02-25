# meeting-room

* 설계
  * 요구사항
    * 정시, 30분 간격 단위로 예약
    * 1회성 예약
    * 주 단위 반복 예약 (반복 횟수 지정)

  * Class

  * API 
    * HOST: localhost:10080
    * 회의실 에약 리스트 조회
      * `GET` /pay/v1/reservations?reserveDate={reserveDate}
    * 회의실 예약 등록
      * `POST` /pay/v1/reservations
    * 회의실 예약 수정
      * `PUT` /pay/v1/reservations
    * 회의실 예약 삭제
      * `DELETE` /pay/v1/reservations
            
  
* REST API 
  * http://localhost:10080/swagger-ui.htm
