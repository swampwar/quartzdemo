let winRef;
/************************ insert or update trigger START *************************/

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

        getJobList(triggerGroup, triggerName);

    }else if($(this).hasClass('tg-selectbox')) {
        $('.tn-group').find('span').text('Select Trigger');
        $('#trigger_name').val('');
        $('.table tbody').empty();
        $('.table tbody').append("" +
            "<tr>\n" +
            "<td>1</td>\n" +
            "<td>-</td>\n" +
            "<td>-</td>\n" +
            "<td>-</td>\n" +
            "<td>-</td>\n" +
            "<td>-</td>\n" +
            "<td>-</td>\n" +
            "<td>\n" +
            "<button class=\"btn btn-success form-control\" onClick=\"editJob()\" data-toggle=\"modal\" data-target=\"#myModal\")>EDIT</button>\n" +
            "<button class=\"btn btn-danger form-control\" onClick=\"\">DELETE</button>\n" +
            "</td>\n" +
            "</tr>");

    }
});
/*End selectbox Menu*/

/*selectbox-triggerName Menu*/
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
/*End selectbox-Name Menu*/

$('.selectbox-menu li').click(function () {
    var input = '<strong>' + $(this).parents('.selectbox').find('input').val() + '</strong>',
        msg = '<span class="msg">Hidden input value: ';
});

/************************ insert or update trigger END *************************/


/************************ search trigger deatil START *************************/

const updateTrigger = (triggerGroup, triggerName) => {
    location.href = '/triggerDetail/update/' + triggerGroup + "/" + triggerName;
};

/************************ search trigger deatil END *************************/




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
                "<td>" + execProg.seq + "</td>\n" +
                "<td>" + execProg.summary + "</td>\n" +
                "<td>" + execProg.description + "</td>\n" +
                "<td>" + execProg.execParam1 +  "</td>\n" +
                "<td>" + execProg.execParam2 +  "</td>\n" +
                "<td>" + execProg.execParam3 +  "</td>\n" +
                "<td onclick='popupProgram(\"execProg\",\""+ execProg.programName +"\");' style=\"cursor:pointer;\" data-tooltip-text=\"소스 보기\">" + execProg.programName + "</td>\n" +
                "<td>\n" +
                "<button class=\"btn btn-success form-control\" onClick=\"editJob()\" data-toggle=\"modal\" data-target=\"#myModal\")>EDIT</button>\n" +
                "<button class=\"btn btn-danger form-control\" onClick=\"\">DELETE</button>\n" +
                "</td>\n" +
                "</tr>");
        });
    });
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

$("form").submit(function(e) {
    e.preventDefault();
});
