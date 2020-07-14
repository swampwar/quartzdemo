let table;

$(document).ready(function () {
    let time;
    const current = new Date();

    let month = current.getMonth() + 1;
    if (month < 10) {month = "0" + month;}

    let today = String(current.getFullYear()) + month + String(current.getDate()) + "235959";
    let yesterday = String(current.getFullYear()) + month + String(current.getDate() -1) + "000000";

    callDataTable(yesterday, today, 'ALL', 'ALL');

    // datepicker
    $('#startDate').datepicker({
        format: "yyyy-mm-dd",	//데이터 포맷 형식(yyyy : 년 mm : 월 dd : 일 )
        // startDate: '-1y',	//달력에서 선택 할 수 있는 가장 빠른 날짜. 이전으로는 선택 불가능 ( d : 일 m : 달 y : 년 w : 주)
        // endDate: '+1y',	//달력에서 선택 할 수 있는 가장 느린 날짜. 이후로 선택 불가 ( d : 일 m : 달 y : 년 w : 주)
        autoclose : true,	//사용자가 날짜를 클릭하면 자동 캘린더가 닫히는 옵션
        calendarWeeks : false, //캘린더 옆에 몇 주차인지 보여주는 옵션 기본값 false 보여주려면 true
        clearBtn : false, //날짜 선택한 값 초기화 해주는 버튼 보여주는 옵션 기본값 false 보여주려면 true
        // datesDisabled : ['2019-06-24','2019-06-26'],//선택 불가능한 일 설정 하는 배열 위에 있는 format 과 형식이 같아야함.
        // daysOfWeekDisabled : [0,6],	//선택 불가능한 요일 설정 0 : 일요일 ~ 6 : 토요일
        // daysOfWeekHighlighted : [0,6], //강조 되어야 하는 요일 설정
        disableTouchKeyboard : false,	//모바일에서 플러그인 작동 여부 기본값 false 가 작동 true가 작동 안함.
        immediateUpdates: false,	//사용자가 보는 화면으로 바로바로 날짜를 변경할지 여부 기본값 :false
        // multidate : false, //여러 날짜 선택할 수 있게 하는 옵션 기본값 :false
        // multidateSeparator :",", //여러 날짜를 선택했을 때 사이에 나타나는 글짜 2019-05-01,2019-06-01
        templates : {
            leftArrow: '&laquo;',
            rightArrow: '&raquo;'
        }, //다음달 이전달로 넘어가는 화살표 모양 커스텀 마이징
        showWeekDays : true ,// 위에 요일 보여주는 옵션 기본값 : true
        todayHighlight : true ,	//오늘 날짜에 하이라이팅 기능 기본값 :false
        toggleActive : true,	//이미 선택된 날짜 선택하면 기본값 : false인경우 그대로 유지 true인 경우 날짜 삭제
        weekStart : 0 ,//달력 시작 요일 선택하는 것 기본값은 0인 일요일
        language : "ko"	//달력의 언어 선택, 그에 맞는 js로 교체해줘야한다.

    }).datepicker('setDate', '-1D')
        .on("changeDate", function(e) {
            if($('#startDate').val() > $('#endDate').val()) {
                $('#startDate').datepicker('setDate', new Date($('#endDate').val()));
            }
    });

    $('#endDate').datepicker({
        format: "yyyy-mm-dd",	//데이터 포맷 형식(yyyy : 년 mm : 월 dd : 일 )sel
        autoclose : true,	//사용자가 날짜를 클릭하면 자동 캘린더가 닫히는 옵션
        templates : {
            leftArrow: '&laquo;',
            rightArrow: '&raquo;'
        }, //다음달 이전달로 넘어가는 화살표 모양 커스텀 마이징
        showWeekDays : true ,// 위에 요일 보여주는 옵션 기본값 : true
        todayHighlight : true ,	//오늘 날짜에 하이라이팅 기능 기본값 :false
        toggleActive : true,	//이미 선택된 날짜 선택하면 기본값 : false인경우 그대로 유지 true인 경우 날짜 삭제
        weekStart : 0 ,//달력 시작 요일 선택하는 것 기본값은 0인 일요일
        language : "ko"	//달력의 언어 선택, 그에 맞는 js로 교체해줘야한다.
    }).datepicker('setDate', 'today')
        .on("changeDate", function(e) {
            if($('#startDate').val() > $('#endDate').val()) {
                $('#endDate').datepicker('setDate', new Date($('#startDate').val()));
            }
        });

    //datepicker end
});

const searchHistory = () => {
    let startDate = $('#startDate').val();
    let endDate = $('#endDate').val();
    const triggerGroup = $('#trigger_group').val();
    const triggerName = $('#trigger_name').val();

    startDate = startDate.substr(0,4) + startDate.substr(5,2) + startDate.substr(8,2) + "000000";
    endDate = endDate.substr(0,4) + endDate.substr(5,2) + endDate.substr(8,2) + "235959";

    // const jsonData = JSON.stringify(
    //     {startDate: startDate, endDate: endDate, triggerGroup: triggerGroup}
    // );

    table.destroy();

    callDataTable(startDate, endDate, triggerGroup, triggerName);
};

const callDataTable = (startDate, endDate, triggerGroup, triggerName) => {
    if (triggerGroup == "" || triggerGroup == null) {
        triggerGroup = "ALL";
    }

    if (triggerName == "" || triggerName == null) {
        triggerName = "ALL";
    }

    console.log(triggerGroup, triggerName);

    table = $('#dataTable').DataTable({
        "order": [],
        "ajax": {
            type: 'POST',
            url: '/history/search',
            data: {startDate: startDate, endDate: endDate, triggerGroup: triggerGroup, triggerName: triggerName},
            dateType: "JSON"
        },
        "columns": [
            { "data": "triggerName" },
            // { "data": "triggerSttDtm" },
            { "data": "execProgSeq" },
            { "data": "execProgName" },
            { "data": "summary" },
            { "data": "jobSttDtm" },
            { "data": "jobEndDtm" },
            { "data": "jobExecStaCd" },
            { "data": "jobExecRslt" }
            // { "data": "execParam1" },
            // { "data": "execParam2" },
            // { "data": "execParam3" },
            // { "data": "execCmd" },
        ],
        rowGroup: {
            startRender: null,
            endRender: function ( rows, group ) {
                let triggerName = rows.data()[0].triggerName;
                // let jobSttDtm = convertStrToDate(rows.data()[rows.length-1].jobSttDtm);
                // let jobEndDtm = convertStrToDate(rows.data()[rows.length-1].jobEndDtm);

                let execTime = 0;
                rows.data().each(function(data){
                    if(data.jobSttDtm == null || data.jobEndDtm == null){
                        console.log('시작시간 또는 종료시간이 null');
                        return true;
                    }

                    execTime += (convertStrToDate(data.jobEndDtm) - convertStrToDate(data.jobSttDtm))/1000;
                });

                return '  '+triggerName + ' 실행이력 : 트리거 시작 (' + formatTime(group) + '), 프로그램 ('+rows.count()+' 건), 실행시간 (' + execTime + ' 초)';
            },
            dataSrc: 'triggerSttDtm'
        }
    });
};

function convertStrToDate(str){
    return new Date(str.slice(0, 4), str.slice(4, 6) - 1, str.slice(6, 8),
        str.slice(8, 10), str.slice(10, 12), str.slice(12, 14));
}

function formatTime(str){
    return str.slice(0, 4) + '/' + str.slice(4, 6) + '/' + str.slice(6, 8) + ' '
        + str.slice(8, 10) + ':' + str.slice(10, 12) + ':' + str.slice(12, 14);
}

/*selectbox Menu*/
$('.selectbox.triggerGroup').click(function () {
    $(this).attr('tabindex', 1).focus();
    $(this).toggleClass('active');
    $(this).find('.selectbox-menu').slideToggle(300);
});
$('.selectbox').focusout(function () {
    $(this).removeClass('active');
    $(this).find('.selectbox-menu').slideUp(300);
});
$('.selectbox .selectbox-menu li').click(function () {
    $(this).parents('.selectbox').find('span').text($(this).text());
    $(this).parents('.selectbox').find('input').attr('value', $(this).attr('id'));

    if($(this).hasClass('tn-selectbox')) {
        const triggerGroup = $('#trigger_group').val();
        const triggerName = $('#trigger_name').val();

    }else if($(this).hasClass('tg-selectbox')) {
        $('.tn-group').find('span').text('ALL');
        $('#trigger_name').val('');
    }
});

$('.selectbox.triggerName').click(function () {
    const selected = $('#trigger_group').val();
    console.log(selected);

    if(selected === null || selected === "") {
        alert("트리거 그룹을 먼저 선택해주세요.");
    }else {
        $(this).attr('tabindex', 1).focus();
        $(this).toggleClass('active');
        $(this).find('.selectbox-menu.' +selected+ '-selected').slideToggle(300);
    }
});

