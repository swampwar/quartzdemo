let winRef;
/************************ insert or update trigger START *************************/
/*selectbox-intable-progType Menu*/
$('.selectbox-inTable').click(function () {
    $(this).attr('tabindex', 1).focus();
    $(this).toggleClass('active');
    $(this).find('.selectbox-menu').slideToggle(300);
});
/*End selectbox-useYn Menu*/
$('.selectbox-inTable').focusout(function () {
    $(this).removeClass('active');
    $(this).find('.selectbox-menu').slideUp(300);
});

$('.selectbox-inTable .selectbox-menu li').click(function () {
    $(this).parents('.selectbox-inTable').find('span').text($(this).text());
    $(this).parents('.selectbox-inTable').find('input').attr('value', $(this).attr('id'));
});

$('.selectbox-inTable .selectbox-menu li').click(function () {
    var input = '<strong>' + $(this).parents('.selectbox-inTable').find('input').val() + '</strong>',
        msg = '<span class="msg">Hidden input value: ';
});

const getEmptyJobList = () => {
    const i = $('.table tbody').children('tr').length;
    // console.log($('.table tbody').children('tr').length);

    $('.table tbody').append("" +
        "<tr>\n" +
        "<td width='6%'><input class='input-job job-seq' type='text' name='execProgInfoList["+i+"].jobSeq' placeholder=' ' value='"+(i+1)+"' onchange='changeSeq(this);'></td>" +
        "<td width='10%'><div class='selectbox-inTable workDvsCd'>" +
            "<div class='select'>" +
                "<span>선 택</span>" +
                "<i class='fa fa-chevron-left'></i>" +
                "<input class='input-job job-workDvsCd' type='hidden' name='execProgInfoList["+i+"].settmWorkDvsCd'>" +
            "</div>" +
            "<ul class='selectbox-menu'>" +
                "<li class='tg-selectbox' id='MGT'><div>MGT</div></li>" +
                "<li class='tg-selectbox' id='OBZ'><div>OBZ</div></li>" +
            "</ul>" +
        "</div></td>" +
        "<td width='18%'><input class='input-job job-progName' type='text' name='execProgInfoList["+i+"].progNm' placeholder=' ' ></td>" +
        "<td width='10%'><div class='selectbox-inTable progType'>" +
            "<div class='select'>" +
                "<span>선 택</span>" +
                "<i class='fa fa-chevron-left'></i>" +
                "<input class='input-job job-progDvsCd' type='hidden' name='execProgInfoList["+i+"].progDvsCd'>" +
            "</div>" +
            "<ul class='selectbox-menu'>" +
                "<li class='tg-selectbox' id='00'><div>SHELL (00)</div></li>" +
                "<li class='tg-selectbox' id='01'><div>PROC (01)</div></li>" +
                "<li class='tg-selectbox' id='02'><div>ETC (02)</div></li>" +
            "</ul>" +
        "</div></td>" +
        "<td width='25%'><input class='input-job job-progPath' type='text' name='execProgInfoList["+i+"].progPath' placeholder=' ' ></td>" +
        "<td width='25%'><input class='input-job job-progDesc' type='text' name='execProgInfoList["+i+"].progDesc' placeholder=' ' ></td>" +
        "<td>\n" +
        "    <button class=\"btn btn-danger form-control\" onclick=\"deleteJob(this);\">DELETE</button>\n" +
        "</td>" +
        "</tr>");

    /*selectbox-intable-progType Menu*/
    $('.selectbox-inTable').eq(-1).click(function () {
        $(this).attr('tabindex', 1).focus();
        $(this).toggleClass('active');
        $(this).find('.selectbox-menu').slideToggle(300);
    });
    /*End selectbox-useYn Menu*/
    $('.selectbox-inTable').eq(-1).focusout(function () {
        $(this).removeClass('active');
        $(this).find('.selectbox-menu').slideUp(300);
    });

    /*selectbox-intable-progType Menu*/
    $('.selectbox-inTable').eq(-2).click(function () {
        $(this).attr('tabindex', 1).focus();
        $(this).toggleClass('active');
        $(this).find('.selectbox-menu').slideToggle(300);
    });
    /*End selectbox-useYn Menu*/
    $('.selectbox-inTable').eq(-2).focusout(function () {
        $(this).removeClass('active');
        $(this).find('.selectbox-menu').slideUp(300);
    });

    $('.selectbox-inTable .selectbox-menu li').click(function () {
        $(this).parents('.selectbox-inTable').find('span').text($(this).text());
        $(this).parents('.selectbox-inTable').find('input').attr('value', $(this).attr('id'));
    });

    $('.selectbox-inTable .selectbox-menu li').click(function () {
        var input = '<strong>' + $(this).parents('.selectbox-inTable').find('input').val() + '</strong>',
            msg = '<span class="msg">Hidden input value: ';
    });

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
                if(j !== 6) {
                    let name = $(td).find('input').attr('name');
                    name = name.substr(0, name.indexOf('[') + 1) + i + name.substr(name.indexOf(']'));
                    // console.log(name);
                    $(td).find('input').attr('name', name);
                }
            })
        });
    }
};

const changeSeq = (obj) => {

    const changedSeq = $(obj).val();
    $.each($(obj).parents('tr').children('td'), function (i, td) {
        // console.log(td);
        if(i !== 6) {
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
}

