let table;

$(document).ready(function () {
    callDataTable('ALL');
});

const searchProgram = () => {
    const triggerGroup = $('#trigger_group').val();

    table.destroy();

    callDataTable(triggerGroup);
};

const callDataTable = (triggerGroup) => {
    if (triggerGroup == "" || triggerGroup == null) {
        triggerGroup = "ALL";
    }

    console.log(triggerGroup);

    table = $('#dataTable').DataTable({
        "order": [],
        "ajax": {
            type: 'POST',
            url: '/program/search',
            data: {workDvsCd: triggerGroup},
            dateType: "JSON"
        },
        "columns": [
            { "data": "workDvsCd" },
            // { "data": "triggerSttDtm" },
            { "data": "progId" },
            { "data": "progNm" },
            { "data": "progDvsCd" },
            { "data": "progPath" },
            { "data": "progDesc" },
            { "data": "registDtime" },
            { "data": "updateDtime" }
            // { "data": "execParam1" },
            // { "data": "execParam2" },
            // { "data": "execParam3" },
            // { "data": "execCmd" },
        ]
        // rowGroup: {
        //     startRender: null,
        //     endRender: function ( rows, group ) {
        //         let triggerName = rows.data()[0].triggerName;
        //         // let jobSttDtm = convertStrToDate(rows.data()[rows.length-1].jobSttDtm);
        //         // let jobEndDtm = convertStrToDate(rows.data()[rows.length-1].jobEndDtm);
        //
        //         let execTime = 0;
        //         rows.data().each(function(data){
        //             if(data.jobSttDtm == null || data.jobEndDtm == null){
        //                 console.log('시작시간 또는 종료시간이 null');
        //                 return true;
        //             }
        //
        //             execTime += (convertStrToDate(data.jobEndDtm) - convertStrToDate(data.jobSttDtm))/1000;
        //         });
        //
        //         return '  '+triggerName + ' 실행이력 : 트리거 시작 (' + formatTime(group) + '), 프로그램 ('+rows.count()+' 건), 실행시간 (' + execTime + ' 초)';
        //     },
        //     dataSrc: 'triggerSttDtm'
        // }
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
        alert("업무 그룹을 먼저 선택해주세요.");
    }else {
        $(this).attr('tabindex', 1).focus();
        $(this).toggleClass('active');
        $(this).find('.selectbox-menu.' +selected+ '-selected').slideToggle(300);
    }
});

/*************************** search program detail START **************************/
const programDetail = () => {
    location.href = '/programRegist';
};
/*************************** search program detail END **************************/

