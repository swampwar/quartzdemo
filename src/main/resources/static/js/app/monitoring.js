let winRef;

$('.huge').contextmenu(function(e){
    // console.log("context menu works");
    //Get window size:
    let winWidth = $(document).width();
    let winHeight = $(document).height();
    //Get pointer position:
    let posX = e.pageX;
    let posY = e.pageY;
    //Get contextmenu size:
    let menuWidth = $(".contextmenu").width();
    let menuHeight = $(".contextmenu").height();
    //Security margin:
    let secMargin = 10;
    //Prevent page overflow:
    let posLeft;
    let posTop;
    if(posX + menuWidth + secMargin >= winWidth
        && posY + menuHeight + secMargin >= winHeight){
        //Case 1: right-bottom overflow:
        posLeft = posX - menuWidth - secMargin + "px";
        posTop = posY - menuHeight - secMargin + "px";
    }
    else if(posX + menuWidth + secMargin >= winWidth){
        //Case 2: right overflow:
        posLeft = posX - menuWidth - secMargin + "px";
        posTop = posY + secMargin + "px";
    }
    else if(posY + menuHeight + secMargin >= winHeight){
        //Case 3: bottom overflow:
        posLeft = posX + secMargin + "px";
        posTop = posY - menuHeight - secMargin + "px";
    }
    else {
        //Case 4: default values:
        posLeft = posX + secMargin + "px";
        posTop = posY + secMargin + "px";
    };
    //Display contextmenu:
    $(".contextmenu").css({
        "left": posLeft,
        "top": posTop
    }).show();
    // console.log("before return false", posLeft, posTop, posX, posY);
    //Prevent browser default contextmenu.
    return false;
});
//Hide contextmenu:
$(document).click(function(){
    $(".contextmenu").hide();
});

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
                        '<div ><span class="pull-right" onclick="popupProgram(\'execProg\',\''+ rslt.programName +'\');" style="cursor:pointer;" data-tooltip-text="소스 보기">' + rslt.programName + '</span></div>' +
                    '</div>' +
                '</div>' +
            '</div>' +
            '<a>' +
                '<div class="panel-footer">' +
                    '<span class="pull-right" onclick="popupProgram(\'execLog\',\''+ rslt.programName +'\');" style="cursor:pointer;" data-tooltip-text="로그 보기">' + rslt.jobExecStaCd +'</span>' +
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

    let popUrl = 'dashboard/popup/' + target +'/' +progName;
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

