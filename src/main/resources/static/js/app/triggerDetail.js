/*selectbox Menu*/
$('.selectbox').click(function () {
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
});
/*End selectbox Menu*/

$('.selectbox-menu li').click(function () {
    var input = '<strong>' + $(this).parents('.selectbox').find('input').val() + '</strong>',
        msg = '<span class="msg">Hidden input value: ';
});

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
