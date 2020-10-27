let winRef;

const openJobDatas = (triggerName, triggerGroup) => {
    console.log(triggerGroup, triggerName);
    const jsonData = JSON.stringify({
        triggerGroup : triggerGroup,
        triggerName : triggerName,
    });

    $.ajax({
        url: '/dashboard/getJob',
        type: 'POST',
        dataType: 'json',
        data: jsonData,
        contentType: 'application/json',
        async: false,
    }).done(function (datas) {
        $.each(datas, function (i, data) {
            addJobDetail(triggerName, data);
        });
    });

    $('.' + triggerName + '-con .pull-right').html("잡 닫기 <i class='fa fa-arrow-circle-up'></i>");
    $('.' + triggerName + '-con a').attr("onclick", "closeJobDatas('"+triggerName+"','" +triggerGroup+"');");

};

const closeJobDatas = (triggerName, triggerGroup) => {
    $('.' + triggerName + '-box').empty();

    $('.' + triggerName + '-con .pull-right').html("잡 펼치기 <i class='fa fa-arrow-circle-down'></i>");
    $('.' + triggerName + '-con a').attr("onclick", "openJobDatas('"+triggerName+"','" +triggerGroup+"');");

};

const addJobDetail= (target, rslt) => {
    let jobSttDtm = "실행전"; let jobEndDtm = "종료전";
    if(rslt.workSttDtime != null) {
        jobSttDtm = rslt.workSttDtime.substr(8,2) + ':' + rslt.workSttDtime.substr(10,2) + ':' + rslt.workSttDtime.substr(12,2);
    }
    if(rslt.workEndDtime != null) {
        jobEndDtm = rslt.workEndDtime.substr(8,2) + ':' + rslt.workEndDtime.substr(10,2) + ':' + rslt.workEndDtime.substr(12,2);
    }

    $('.' + target + '-box').append(''+
        '<div class="job-'+rslt.workSeq+' job-container panel-'+ rslt.workResultCd +'">' +
            '<div class="panel-heading">' +
                '<div class="row job-box">' +
                    '<div class="col-xs-2">' +
                        // '<i class="fa fa-comments fa-4x"></i>' +
                        '<span class="pull-left job-seq">'+ rslt.workSeq +'</span>' +
                    '</div>' +
                    '<div class="col-xs-10 text-right" style="display: inline-grid">' +
                        '<div class="huge"><span class="pull-right job-name">' + rslt.settmJobId + '</span></div>' +
                        '<div ><span class="pull-right" onclick="popupProgram(\'execProg\',\''+ rslt.progId +'\');" style="cursor:pointer;" data-tooltip-text="소스 보기">' + rslt.progId + '</span></div>' +
                    '</div>' +
                '</div>' +
            '</div>' +
            '<a>' +
                '<div class="panel-footer">' +
                    '<span class="pull-right" onclick="popupProgram(\'execLog\',\''+ rslt.settmJobId +'\');" style="cursor:pointer;" data-tooltip-text="로그 보기">' + rslt.workResultCd +'</span>' +

                    '<div class="clearfix"></div>' +
                '</div>' +
            '</a>' +
            '<a>' +
                '<div class="panel-footer">' +
                    '<span class="pull-left">잡 시작 시간 </span>' +
                    '<span class="pull-right">' + jobSttDtm +'</span>' +
                    '<div class="clearfix"></div>' +
                '</div>' +
            '</a>' +
            '<a>' +
                '<div class="panel-footer">' +
                    '<span class="pull-left">잡 종료 시간 </span>' +
                    '<span class="pull-right">' + jobEndDtm +'</span>' +
                    '<div class="clearfix"></div>' +
                '</div>' +
            '</a>' +
        '</div>'
    );
};

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

const pauseTrigger = (triggerGroup, triggerName) => {
    const target = $('.'+triggerGroup+'-'+triggerName);
    // console.log(target.parent('div'));
    const panel = target.parent('div');

    const jsonData = JSON.stringify(
        {triggerGroup : triggerGroup, triggerName : triggerName}
    );

    $.ajax({
        url: '/scheduler/job/pause',
        type: 'put',
        dataType: 'json',
        data: jsonData,
        contentType: 'application/json',
        async: false,
        success : function(rslt){
            alert(rslt.msg);
            getDashboardSection($('.active-button').val());
        },
        error : function(rslt){
            alert(rslt.msg);
        }
    });

};

const resumeTrigger = (triggerGroup, triggerName) => {
    const target = $('.'+triggerGroup+'-'+triggerName);
    // console.log(target.parent('div'));
    const panel = target.parent('div');

    const jsonData = JSON.stringify(
        {triggerGroup : triggerGroup, triggerName : triggerName}
    );

    $.ajax({
        url: '/scheduler/job/resume',
        type: 'put',
        dataType: 'json',
        data: jsonData,
        contentType: 'application/json',
        async: false,
        success : function(rslt){
            alert(rslt.msg);
            getDashboardSection($('.active-button').val());
        },
        error : function(rslt){
            alert(rslt.msg);
        }
    });
};

const killTrigger = (triggerGroup, triggerName) => {
    const target = $('.'+triggerGroup+'-'+triggerName);
    // console.log(target.parent('div'));
    const panel = target.parent('div');

    const jsonData = JSON.stringify(
        {triggerGroup : triggerGroup, triggerName : triggerName}
    );

    $.ajax({
        url: '/scheduler/job/kill',
        type: 'post',
        dataType: 'json',
        data: jsonData,
        contentType: 'application/json',
        async: false,
        success : function(rslt){
            alert(rslt.msg);
            getDashboardSection($('.active-button').val());
        },
        error : function(rslt){
            alert(rslt.msg);
        }
    });
};

const rerunTrigger = (triggerGroup, triggerName) => {
    let execSeq = prompt("재실행할 시퀀스를 입력하세요. (미입력시 전체 재실행)", 'ex) 1,3');
    if (execSeq != null) {
        if (execSeq == "") { execSeq = null; }
        console.log(execSeq);

        const target = $('.' + triggerGroup + '-' + triggerName);
        // console.log(target.parent('div'));
        const panel = target.parent('div');

        const jsonData = JSON.stringify(
            {triggerGroup: triggerGroup, triggerName: triggerName, execSeq: execSeq}
        );

        $.ajax({
            url: '/scheduler/job/runNow',
            type: 'post',
            dataType: 'json',
            data: jsonData,
            contentType: 'application/json',
            async: false,
            success: function (rslt) {
                alert(rslt.msg);
                getDashboardSection($('.active-button').val());
            },
            error: function (rslt) {
                alert(rslt.msg);
            }
        });
    }
};

const rerunAllTrigger = (triggerGroup, triggerName) => {
    if (confirm("재기동하시겠습니까?")) {

        const target = $('.' + triggerGroup + '-' + triggerName);
        // console.log(target.parent('div'));
        const panel = target.parent('div');

        const jsonData = JSON.stringify(
            {triggerGroup: triggerGroup, triggerName: triggerName}
        );

        $.ajax({
            url: '/scheduler/job/runNow',
            type: 'post',
            dataType: 'json',
            data: jsonData,
            contentType: 'application/json',
            async: false,
            success: function (rslt) {
                alert(rslt.msg);
                getDashboardSection($('.active-button').val());
            },
            error: function (rslt) {
                alert(rslt.msg);
            }
        });
    }
};

const modifyTrigger = (triggerGroup, triggerName) => {
    const target = $('.'+triggerGroup+'-'+triggerName);
    // console.log(target.parent('div'));
};

const goToDetail = (triggerGroup, triggerName) => {
    location.href = 'triggerDetail/' + triggerGroup + '/' + triggerName;
};

