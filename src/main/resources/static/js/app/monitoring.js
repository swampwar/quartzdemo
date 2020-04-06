let activeButton = $('.active-button').val();
console.log($('.group-container'));
if(activeButton === 'ALL') {
    $('.group-container').addClass("col-md-6");
}else {
    $('.group-container').addClass("col-md-12");
}

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
    if(rslt.jobSttDtm != null) {
        jobSttDtm = rslt.jobSttDtm.substr(8,2) + ':' + rslt.jobSttDtm.substr(10,2) + ':' + rslt.jobSttDtm.substr(12,2);
    }
    if(rslt.jobEndDtm != null) {
        jobEndDtm = rslt.jobEndDtm.substr(8,2) + ':' + rslt.jobEndDtm.substr(10,2) + ':' + rslt.jobEndDtm.substr(12,2);
    }

    $('.' + target + '-box').append(''+
        '<div class="job-'+rslt.seq+' job-container panel-'+ rslt.jobExecStaCd +'">' +
            '<div class="panel-heading">' +
                '<div class="row job-box">' +
                    '<div class="col-xs-2">' +
                        // '<i class="fa fa-comments fa-4x"></i>' +
                        '<span class="pull-left job-seq">'+ rslt.seq +'</span>' +
                    '</div>' +
                    '<div class="col-xs-10 text-right" style="display: inline-grid">' +
                        '<div class="huge"><span class="pull-right job-name">' + rslt.jobName + '</span></div>' +
                        '<div><span class="pull-right">' + rslt.programName + '</span></div>' +
                    '</div>' +
                '</div>' +
            '</div>' +
            '<a href="#">' +
                '<div class="panel-footer">' +
                    '<span class="pull-right">' + rslt.jobExecStaCd +'</span>' +
                    '<div class="clearfix"></div>' +
                '</div>' +
                '<div class="panel-footer">' +
                    '<span class="pull-left">잡 시작 시간 </span>' +
                    '<span class="pull-right">' + jobSttDtm +'</span>' +
                    '<div class="clearfix"></div>' +
                '</div>' +
                '<div class="panel-footer">' +
                    '<span class="pull-left">잡 종료 시간 </span>' +
                    '<span class="pull-right">' + jobEndDtm +'</span>' +
                    '<div class="clearfix"></div>' +
                '</div>' +
            '</a>' +
        '</div>'
    );
};
