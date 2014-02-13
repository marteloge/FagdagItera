$(function () {
    var timer,
        delay = 600;

    $('#current_age')    .bind('input', onChange);
    $('#pension_age')    .bind('input', onChange);
    $('#working_age')    .bind('input', onChange);
    $('#salary')         .bind('input', onChange);
    $('#employer')       .bind('input', onChange);
    $('#service_pension').bind('input', onChange);

    function onChange () {
        window.clearTimeout(timer);
        timer = window.setTimeout(updatePension, delay);
    }

    function updatePension () {
        $('#results').remove();

        $.post('/calculatePension', {
            current_age:     $('#current_age').val(),
            pension_age:     $('#pension_age').val(),
            working_age:     $('#working_age').val(),
            salary:          $('#salary').val(),
            employer:        $('#employer').val(),
            service_pension: $('#service_pension').val()
        }).done(function (data) {
            console.log(data);
            $('#controllers').append('<p id="results" class="btn btn-success">Forventet årlig pensjon er ' + data + '.</p>');
        }).fail(function () {
            $('#controllers').append('<p id="results" class="btn btn-danger">En feil forekom!</p>');
        });
    }
});
