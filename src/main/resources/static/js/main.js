$(document).ready(function(){
    var date_input1=$('input[id="reserveDate"]');
    var date_input2=$('input[id="searchReserveDate"]');

    var container=$('.bootstrap-iso form').length>0 ? $('.bootstrap-iso form').parent() : "body";
    var options={
        format: 'yyyy-mm-dd',
        container: container,
        todayHighlight: true,
        autoclose: true,
    };
    date_input1.datepicker(options);
    date_input2.datepicker(options);

    $("#search-form").submit(function (event) {
        event.preventDefault();
        reservationSearch();
    })
    $("#room-form").submit(function (event) {
        event.preventDefault();
        var reserveUuid = $('#reserveUuid').val();
        if ( reserveUuid == '') {
            add();
        } else {
            update(reserveUuid);
        }
    })

    // 오늘
    $("#searchReserveDate").datepicker("setDate",new Date());
    reservationSearch();
})

// 검색
function reservationSearch() {
    $.get("/pay/v1/reservations", {"reserveDate": $("#searchReserveDate").val()}, function (result) {
        $('#reservationTable tr').each(function () {
            $(this).find('td').empty();
        });

        $.each(result.rooms,function(index, room){
            var td = "";
            $.each(room.reservations,function(index, reservation){
                td += "<br><b>" + reservation.memberName + "</b>";
                $.each(reservation.reservedTimes,function(index, reservedTime){
                    td +=  "<br><a href=javascript:choice('"+ room.meetingRoomUuid
                                                        + "','" + reservation.memberUuid
                                                        + "','" + reservedTime.reserveUuid
                                                        + "','" + reservedTime.reserveTime+ "')>" + reservedTime.reserveTime + "</a>";
                    if (reservedTime.repeatCount > 0) {
                        td += "(반복" + reservedTime.repeatCount + " 회)";
                    }
                })
            })
            $('#' + room.meetingRoomUuid).html(td);
        });
    })
}

// 삭제
$("#bth-remove").click(function () {
    if (confirm("삭제하시겠습니까?")) {
        callJSON( "/pay/v1/reservations/" + $("#reserveUuid").val(), "", 'DELETE').done(function () {
            alert("삭제하였습니다.");
            init();
            reservationSearch();
            changeDisabledProp(false)
            return;

        }).fail(function() {
            alert("삭제 실패하였습니다");
            return;
        });
    }
})

// 등록
function add() {
    var input = {
        meetingRoomUuid : $("#meetingRoomUuid").val(),
        memberUuid : $("#memberUuid").val(),
        reserveDate : $("#reserveDate").val(),
        startReserveTime : $("#startReserveTime").val(),
        endReserveTime : $("#endReserveTime").val(),
        repeatCount: $("#repeatCount").val()
    };
    callJSON( "/pay/v1/reservations", input, 'POST').done(function (result) {
        var header = result.header;
        if (header.code != 0) {
            alert(header.message);
            return;
        }

        alert("예약되었습니다");
        $("#searchReserveDate").val($("#reserveDate").val());
        reservationSearch();

        init();
        return;

    }).fail(function(e) {
        alert("예약 실패하였습니다");
    });
}

// 수정
function update(reserveUuid) {
    var input = {
        meetingRoomUuid : $("#meetingRoomUuid").val(),
        reserveDate : $("#reserveDate").val(),
        startReserveTime : $("#startReserveTime").val(),
        endReserveTime : $("#endReserveTime").val()
    };
    callJSON( "/pay/v1/reservations/" + reserveUuid, input, 'PUT').done(function (result) {
        var header = result.header;
        if (header.code != 0) {
            alert(header.message);
            return;
        }

        alert("수정되었습니다");
        $("#searchReserveDate").val($("#reserveDate").val());
        reservationSearch();

        changeDisabledProp(false)

        init();
        return;

    }).fail(function(e) {
        alert("수정 실패하였습니다");
    });
}

function choice(meetingRoomUuid, memberUuid, reserveUuid, reserveTime) {
    $("#meetingRoomUuid").val(meetingRoomUuid);
    $("#memberUuid").val(memberUuid);
    $("#reserveDate").val($("#searchReserveDate").val());

    var times = reserveTime.split("~");
    $("#startReserveTime").val(times[0]);
    $("#endReserveTime").val(times[1]);

    $("#reserveUuid").val(reserveUuid);
    changeDisabledProp(true);
}

function init() {
    $('#room-form').each(function(){
        this.reset();
    });
}

function callJSON(url, data, method){
    return $.ajax({
        url: url,
        data: JSON.stringify(data),
        type: method,
        contentType:'application/json'
    });
};

function changeDisabledProp(flag) {
    $("#memberUuid").prop('disabled', flag);
    $("#reserveDate").prop('disabled', flag);
    $("#repeatCount").prop('disabled', flag);

    $("#bth-remove").prop('disabled', !flag);
}

$("#startReserveTime").keyup(function() {
    var s = $("#startReserveTime").val();
    if (s.length == 2) {
        $("#startReserveTime").val(s + ":");
    }
});

$("#endReserveTime").keyup(function() {
    var e = $("#endReserveTime").val();
    if (e.length == 2) {
        $("#endReserveTime").val(e + ":");
    }
});