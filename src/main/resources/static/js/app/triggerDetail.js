let winRef;
/************************ insert or update trigger START *************************/

$(document).ready(function () {
    let time;
    const current = new Date();

    let month = current.getMonth() + 1;
    if (month < 10) {month = "0" + month;}

    // let today = String(current.getFullYear()) + month + String(current.getDate()) + "235959";
    // let yesterday = String(current.getFullYear()) + month + String(current.getDate() -1) + "000000";

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

    selectRadio();
});

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

        // getJobList(triggerGroup, triggerName);

    }else if($(this).hasClass('tg-selectbox')) {
        $('.tn-group').find('span').text('선 택');
        $('#trigger_name').val('');
        $('.table tbody').empty();
        getEmptyJobList();
    }
});
/*End selectbox Menu*/

/*selectbox-triggerName Menu*/
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

$('.selectbox.jobId').click(function () {
    const selected_group = $('#trigger_group').val();
    console.log("selected jog group : " + selected_group);

    const selected_job = $('#job_group_id').val();
    console.log("selected jog  : " + selected_job);

    if(selected_group === null || selected_group === "") {
        alert("업무 그룹을 먼저 선택해주세요.");
    }else {
        if(selected_job === null || selected_job === "") {
            alert("작업 그룹을 먼저 선택해주세요.");
        }else {
            $(this).attr('tabindex', 1).focus();
            $(this).toggleClass('active');
            $(this).find('.selectbox-menu.' + selected_job + '-selected').slideToggle(300);
        }
    }
});

$('.selectbox.progId').click(function () {
    const selected = $('#trigger_group').val();
    console.log(selected);

    if(selected === null || selected === "") {
        alert("업무 그룹을 먼저 선택해주세요.");
    }else {
        $(this).attr('tabindex', 1).focus();
        $(this).toggleClass('active');
        $(this).find('.selectbox-menu.' +selected+ '2-selected').slideToggle(300);
    }
});
/*End selectbox-Name Menu*/

/*selectbox-useYn Menu*/
$('.selectbox.useYn').click(function () {
    $(this).attr('tabindex', 1).focus();
    $(this).toggleClass('active');
    $(this).find('.selectbox-menu').slideToggle(300);
});
/*End selectbox-useYn Menu*/

$('.selectbox .selectbox-menu li').click(function () {
    var input = '<strong>' + $(this).parents('.selectbox').find('input').val() + '</strong>',
        msg = '<span class="msg">Hidden input value: ';
});


const selectRadio = () => {
    let checkedValue = $('input:radio[name=insertType]:checked').val();
    console.log("insert type : " + checkedValue);
    if (checkedValue === 'jobGroup') {
        $('.selectbox.triggerName').parent('div').hide();
        $('.selectbox.jobId').parent('div').hide();
        $('.selectbox.hgrnJobDetail').parent('div').hide();
        $('.work-time').parent('div').show();
        $('.job-seq').parent('div').hide();
        $('.job-name').parent('div').hide();
    } else if(checkedValue === 'job') {
        $('.selectbox.triggerName').parent('div').show();
        $('.selectbox.jobId').parent('div').hide();
        $('.selectbox.hgrnJobDetail').parent('div').hide();
        $('.work-time').parent('div').hide();
        $('.job-seq').parent('div').show();
        $('.job-name').parent('div').show();
    } else if(checkedValue === 'jobDetail') {
        $('.selectbox.triggerName').parent('div').show();
        $('.selectbox.jobId').parent('div').show();
        $('.selectbox.hgrnJobDetail').parent('div').show();
        $('.work-time').parent('div').hide();
        $('.job-seq').parent('div').show();
        $('.job-name').parent('div').hide();
    }
};

const getEmptyJobList = () => {
    const i = $('.table tbody').children('tr').length;
    // console.log($('.table tbody').children('tr').length);

    $('.table tbody').append("" +
        "<tr>\n" +
        "<td width='3%'><input class='input-job job-seq' type='text' name='execProgInfoList["+i+"].jobSeq' placeholder=' ' value='"+(i+1)+"' onchange='changeSeq(this);'></td>" +
        "<td width='10%'><input class='input-job job-name' type='text' name='execProgInfoList["+i+"].jobName' placeholder=' ' ></td>" +
        "<td width='15%'><input class='input-job job-pgNm' type='text' name='execProgInfoList["+i+"].programName' placeholder=' ' ></td>" +
        "<td width='9%'><input class='input-job job-pgType' type='text' name='execProgInfoList["+i+"].programType' placeholder=' ' ></td>" +
        "<td width='23%'><input class='input-job job-pgDir' type='text' name='execProgInfoList["+i+"].programDir' placeholder=' ' ></td>" +
        "<td width='15%'><input class='input-job job-pgDes' type='text' name='execProgInfoList["+i+"].programDes' placeholder=' ' ></td>" +
        // "<td width='18%'><input class='input-job job-file' type='file' name='execProgFileList["+i+"]' placeholder=' ' onchange=\"getFileName(this);\">" +
        // "                <input type=\"text\" class=\"input-job job-progNm\" name=\"execProgInfoList["+i+"].programName\" placeholder=\"선택된 파일이 없습니다.\" readonly=\"readonly\"></td>" +
        "<td width='10%'><input class='input-job job-sttDt' type='text' name='execProgInfoList["+i+"].sttDt' placeholder=' ' ></td>" +
        "<td width='10%'><input class='input-job job-endDt' type='text' name='execProgInfoList["+i+"].endDt' placeholder=' ' ></td>" +
        "<td width='6%'><input class='input-job job-useYn' type='text' name='execProgInfoList["+i+"].useYn' placeholder=' ' ></td>" +
        "<td>\n" +
        "    <button class=\"btn btn-danger form-control\" onclick=\"deleteJob(this);\">DELETE</button>\n" +
        "</td>" +
        "</tr>");
};

const getJobList = (triggerGroup, triggerName) => {
    const jsonData = JSON.stringify(
        {triggerGroup : triggerGroup, triggerName : triggerName}
    );

    $.ajax({
        url: '/triggerDetail/getExecProgList',
        type: 'post',
        dataType: 'json',
        data: jsonData,
        contentType: 'application/json',
        async: false,
    }).done(function (datas) {
        $('.table tbody').empty();
        $.each(datas, function (i, execProg) {
            $('.table tbody').append("" +
                "<tr>\n" +
                "<td width='6%'><input class='input-job job-seq' type='text' name='execProgInfoList["+i+"].jobSeq' placeholder=' ' value='" + execProg.seq + "' onchange='changeSeq(this);'></td>" +
                "<td width='15%'><input class='input-job job-name' type='text' name='execProgInfoList["+i+"].jobName' placeholder=' ' value='" + execProg.jobName + "'></td>" +
                "<td width='15%'><input class='input-job job-pgNm' type='text' name='execProgInfoList["+i+"].programName' placeholder=' ' value='" + execProg.programName + "'></td>" +
                "<td width='15%'><input class='input-job job-pgType' type='text' name='execProgInfoList["+i+"].programType' placeholder=' ' value='" + execProg.programType + "'></td>" +
                "<td width='15%'><input class='input-job job-pgDir' type='text' name='execProgInfoList["+i+"].programDir' placeholder=' ' value='" + execProg.programDirectory + "'></td>" +
                "<td width='15%'><input class='input-job job-pgDes' type='text' name='execProgInfoList["+i+"].programDes' placeholder=' ' value='" + execProg.programDescription + "'></td>" +
                // "<td width='18%'><input class='input-job job-file' type='file' name='execProgFileList["+i+"]' placeholder='" + execProg.description + "' onchange='getFileName(this);'>" +
                // "                <input type='text' class='input-job job-progNm' name='execProgInfoList["+i+"].programName' placeholder='선택된 파일이 없습니다.' readonly='readonly' value='" + execProg.programName + "'></td>" +
                "<td width='13%'><input class='input-job job-sttDt' type='text' name='execProgInfoList["+i+"].sttDt' placeholder=' ' value='" + execProg.sttDt + "'></td>" +
                "<td width='13%'><input class='input-job job-endDt' type='text' name='execProgInfoList["+i+"].endDt' placeholder=' ' value='" + execProg.endDt + "'></td>" +
                "<td width='13%'><input class='input-job job-useYn' type='text' name='execProgInfoList["+i+"].useYn' placeholder=' ' value='" + execProg.useYn + "'></td>" +
                "<td>\n" +
                "<button class=\"btn btn-danger form-control\" onclick=\"deleteJob(this);\">DELETE</button>\n" +
                "</td>\n" +
                "</tr>");
        });
    });
};

const newTrigger = (obj) => {
    if($(obj).is(":checked") === true) {
        $('.indent-small.triggerName.display-none').removeClass('display-none');
        $('.selectbox.triggerName').addClass('display-none');
        // console.log($('.selectbox.triggerName').children('div').children('input'));
        $('.selectbox.triggerName').children('div').children('input').val("");
        $('.table tbody').empty();
        getEmptyJobList();
    }else {
        $('.indent-small.triggerName').addClass('display-none');
        // console.log($('.indent-small.triggerName').children('input').children('input'));
        $('.indent-small.triggerName').children('div').children('input').val("");
        $('.selectbox.triggerName.display-none').removeClass('display-none');

        const triggerGroup = $('#trigger_group').val();
        const triggerName = $('#trigger_name').val();
        getJobList(triggerGroup, triggerName);
    }
};

const newJob = () => {
    getEmptyJobList();
};

const deleteJob = (obj) => {
    // console.log(obj);
    const row = $(obj).parents('tr');
    const tbody = $(row).parent('tbody');
    // console.log(row);
    $(row).remove();

    // 아직 tbody의 row가 남아있으면 seq 순서 변경
    if ($(tbody).children('tr').length !== 0) {
        $.each((tbody).children('tr'), function (i, tr) {
            // console.log(tr);
            $.each($(tr).children('td'), function (j, td) {
                // console.log(td);
                if(j === 0 ) {
                    $(td).children('input').val(i+1);
                }
                if(j !== 7) {
                    let name = $(td).children('input').attr('name');
                    name = name.substr(0, name.indexOf('[') + 1) + i + name.substr(name.indexOf(']'));
                    // console.log(name);
                    $(td).children('input').attr('name', name);
                }
            })
        });
    }
};

const changeSeq = (obj) => {

    const changedSeq = $(obj).val();
    $.each($(obj).parents('tr').children('td'), function (i, td) {
        // console.log(td);
        if(i !== 7) {
            let name = $(td).children('input').attr('name');
            name = name.substr(0, name.indexOf('[') + 1) + (changedSeq - 1) + name.substr(name.indexOf(']'));
            // console.log(name);
            $(td).children('input').attr('name', name);
        }
    });

};

/************************ insert or update trigger END *************************/


/************************ search trigger detail START *************************/

const updateTrigger = (triggerGroup, triggerName) => {
    location.href = '/triggerDetail/update/' + triggerGroup + "/" + triggerName;
};

const canselUpdate = (triggerGroup, triggerName) => {
    location.href = '/triggerDetail/' + triggerGroup + "/" + triggerName;
};

/************************ search trigger detail END *************************/

const getFileName = (obj) => {
    // console.log(obj);
    const fileValue = $(obj).val().split("\\");
    const fileName = fileValue[fileValue.length-1];
    // console.log(fileName);

    $(obj).parent('td').children('input:last').val(fileName);
    // $('#file_nm').val(fileName);
};

/* POPUP*/
const popupProgram = (target, progName) => {
    console.log(target, progName);

    let contents = "";

    const input = JSON.stringify({
        shellScriptNm : progName
    });

    let popUrl = '/dashboard/popup/' + target +'/' +progName;
    let popupWidth = 650; let popupHeight = 800;
    let popupX = (window.screen.width/2) - (popupWidth/2) ;
    let popupY = (window.screen.height/2) - (popupHeight/2) ;

    let popOption = 'width='+popupWidth+'px, height='+popupHeight+'px, resizable=no, location=no, top='+popupY+'px, left='+popupX+'px';

    if(winRef == null) {
        winRef = window.open(popUrl, target, popOption);
    }else {
        if (winRef.closed == false) {
            winRef.focus();
        }else{
            winRef = window.open(popUrl, target, popOption);
        }
    }
};

/* Job List Table */
function editJob() {
    $(".modal-body").empty().append(`
                <form id="updateJob" action="">
                    <label for="name">Name</label>
                    <input class="form-control" type="text" name="name" value="Name"/>
                    <label for="param1">Param1</label>
                    <input class="form-control" type="text" name="param1" value="Param1"/>
                    <label for="param2">Param2</label>
                    <input class="form-control" type="text" name="param2" value="Param2"/>
                    <label for="param3">Param3</label>
                    <input class="form-control" type="text" name="param3" value="Param3"/>
                    <label for="remark">Remark</label>
                    <input class="form-control" type="text" name="remark" value="Remark"/>
                    <label for="file">FILE</label>
                    <input class="form-control" type="text" name="file" value="FILE"/>
            `);
    $(".modal-footer").empty().append(`
                    <button type="button" type="submit" class="btn btn-primary" onClick="">Save changes</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                </form>
            `);
    /*
    users.forEach(function(user, i) {
        if (user.id == id) {

        }
    });
     */
}

// $("form").submit(function(e) {
//     e.preventDefault();
// });
