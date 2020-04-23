$(document).ready(function () {
    let time;
    $('#dataTable').DataTable({
        "ajax": '/triggerGroup/all',
        "columns": [
            { "data": "triggerGroup" },
            // { "data": "triggerSttDtm" },
            { "data": "description" },
            { "data": "useYn"},
            { "data": "rgtDtm" },
            { "data": "udtDtm" },
            { "data": ""}
            // { "data": "jobSttDtm" },
        ],
        "columnDefs" : [{
            targets : [3, 4],
            render : function (data, type, row) {
                return data.slice(0, 4) + '/' + data.slice(4, 6) + '/' + data.slice(6, 8) + ' '
                    + data.slice(8, 10) + ':' + data.slice(10, 12) + ':' + data.slice(12, 14);
            }
        }, {
            targets : 5,
            data : null,
            render : function(data, type, row) {
                return '<div class="tg-button-container">' +
                    '<div class="tg-button tg-update" onclick="updateTriggerGroup(\'' + row.triggerGroup + '\', this)">Update</div>' +
                    '<div class="tg-button tg-delete" onclick="deleteTriggerGroup(\'' + row.triggerGroup + '\')">Delete</div>' +
                    '</div>'
            }
        }]
    });
});
$.fn.dataTable.render.ellipsis = function ( cutoff ) {
    return function ( data, type, row ) {
        if ( type === 'display' ) {
            let str = data.toString(); // cast numbers

            return str.length < cutoff ?
                str :
                str.substr(0, cutoff-1) +'&#8230;';
        }

        // Search, order and type can use the original data
        return data;
    };
};

const addTriggerGroup = () => {
    const last_tr = $('tbody');
    const gettingClass = last_tr.attr("class");
    let addClass = "odd";
    if(gettingClass === "odd") { addClass = "even";}

    last_tr.append(""+
        "<tr role='row' class='"+ addClass+ "'>" +
        "<td><input type='text' class='input-text input-triggerGroup' value=''></td>" +
        "<td><input type='text' class='input-text input-triggerGroup' value=''></td>" +
        "<td><input type='text' class='input-text input-triggerGroup' value=''></td>" +
        "<td></td>" +
        "<td></td>" +
        "<td><div class='tg-button-container'>" +
        "<div class='tg-button tg-update' onclick='saveTriggerGroup(this);'>Save</div>" +
        "<div class='tg-button tg-delete' onclick='cancelTriggerGroup(\"thisisinsert\", this);'>Cancel</div>" +
        "</div></td>" +
        "</tr>"
    )
};

const updateTriggerGroup = (triggerGroup, obj) => {
    // console.log($(obj).parent().parent('td').parent('tr').children('td'));

    $.each($(obj).parent().parent('td').parent('tr').children('td'), function (i, val) {
        const td = $(val);
        // console.log(td);
        // console.log(td.text());
        const value = td.text();

        if (i != 0 && i !== 3 && i !== 4 && i !== 5) {
            // console.log(td.innerText);
            td.html('<input type="text" class="input-text" value="' + value + '">');

            // data.push(val);
        }else if(i === 5) {
            td.html('<div class="tg-button-container">' +
                '<div class="tg-button tg-update" onclick="saveTriggerGroup(this)">Save</div>' +
                '<div class="tg-button tg-delete" onclick="cancelTriggerGroup(\'' + triggerGroup + '\', this)">Cancel</div>' +
                '</div>');
        }
    });
};

const saveTriggerGroup = (obj) => {

    let triggerGroup = "";
    let description = "";
    let useYn = "";

    // console.log($(obj).parent().parent('td').parent('tr').children('td'));

    $.each($(obj).parent().parent('td').parent('tr').children('td'), function (i, val) {
        const td = $(val);
        // console.log(td);

        if(i === 0) {
            if(td.text() !== "") {triggerGroup = td.text();}
            else{triggerGroup = td.children('input').val();}
        }else if(i === 1){
            description = td.children('input').val();
        }else if(i === 2){
            useYn = td.children('input').val();
        }
    });

    if(!validationTriggerGroup(triggerGroup, description, useYn)) {
        return;
    }

    const flag = confirm("트리거 그룹을 저장하시겠습니까?");
    if (flag === true) {
        const jsonData = JSON.stringify(
            {triggerGroup : triggerGroup, description : description, useYn : useYn}
        );

        $.ajax({
            url: '/triggerGroup/save',
            type: 'post',
            dataType: 'json',
            data: jsonData,
            contentType: 'application/json',
            async: false,
            success : function(rslt){
                alert(rslt.msg);
                let tg = [];
                tg.push(rslt.triggerGroup.triggerGroup);
                tg.push(rslt.triggerGroup.description);
                tg.push(rslt.triggerGroup.useYn);
                tg.push(rslt.triggerGroup.rgtDtm);
                tg.push(rslt.triggerGroup.udtDtm);

                $.each($(obj).parent().parent('td').parent('tr').children('td'), function (i, val) {
                    const td = $(val);
                    // console.log(td);
                    td.html(tg[i]);
                    if (i !== 5 ) {
                        // console.log(td.innerText);
                        if (i === 3 || i === 4) {
                            let data = tg[i].slice(0, 4) + '/' + tg[i].slice(4, 6) + '/' + tg[i].slice(6, 8) + ' '
                            + tg[i].slice(8, 10) + ':' + tg[i].slice(10, 12) + ':' + tg[i].slice(12, 14);
                            td.html(data);
                        } else {
                            td.html(tg[i]);
                        }
                    }else {
                        td.html('<div class="tg-button-container">' +
                            '<div class="tg-button tg-update" onclick="updateTriggerGroup(\'' + triggerGroup + '\', this)">Update</div>' +
                            '<div class="tg-button tg-delete" onclick="deleteTriggerGroup(\'' + triggerGroup + '\', this)">Delete</div>' +
                            '</div>');
                    }
                });
            },
            error : function(rslt){
                alert(rslt.msg);
            }
        });
    }else {
        return;
    }
};

const validationTriggerGroup = (triggerGroup, description, useYn) => {
    if(useYn === 'Y' || useYn === 'N' ) {  // || useYn === 'y' || useYn === 'n'

    } else {
        alert("사용여부는 Y 또는 N으로 입력해주시기 바랍니다.");
        return false;
    }

    return true;

};

const cancelTriggerGroup = (triggerGroup, obj) => {
    if (triggerGroup === "thisisinsert") {
        const flag = confirm("트리거 그룹 등록을 취소하시겠습니까?");
        if (flag === true) {
            const last_tr = $(obj).parent().parent('td').parent('tr');
            $(last_tr).remove();
            return;
        } else {
            return;
        }
    }

    // console.log($(obj).parent().parent('td').parent('tr').children('td'));

    let tg = [];
    $.ajax({
        url: '/triggerGroup/one/'+ triggerGroup,
        type: 'post',
        dataType: 'json',
        contentType: 'application/json',
        async: false,
        success : function(triggerGroup){
            tg.push(triggerGroup.triggerGroup);
            tg.push(triggerGroup.description);
            tg.push(triggerGroup.useYn);
            tg.push(triggerGroup.rgtDtm);
            tg.push(triggerGroup.udtDtm);
        },
        error : function(){
            alert("서버 오류로 작업에 실패했습니다.");
            return;
        }
    });

    $.each($(obj).parent().parent('td').parent('tr').children('td'), function (i, val) {
        const td = $(val);
        // console.log(td);
        // console.log(td.text());

        if (i != 0 && i !== 3 && i !== 4 && i !== 5) {
            // console.log(td.innerText);
            td.html(tg[i]);

            // data.push(val);
        }else if(i === 5) {
            td.html('<div class="tg-button-container">' +
                '<div class="tg-button tg-update" onclick="updateTriggerGroup(\'' + triggerGroup + '\', this)">Update</div>' +
                '<div class="tg-button tg-delete" onclick="deleteTriggerGroup(\'' + triggerGroup + '\', this)">Delete</div>' +
                '</div>');
        }
    });
};

const deleteTriggerGroup = (triggerGroup, obj) => {
    // console.log(triggerGroup + "delete !!");
    const flag = confirm("트리거 그룹을 삭제하시겠습니까?");
    if (flag) {
        $.ajax({
            url: '/triggerGroup/delete/'+ triggerGroup,
            type: 'post',
            dataType: 'json',
            contentType: 'application/json',
            async: false,
            success : function(rslt){
                alert(rslt.msg);
                const last_tr = $(obj).parent().parent('td').parent('tr');
                $(last_tr).remove();
            },
            error : function(rslt){
                alert(rslt.msg);
                return;
            }
        });

    }
};