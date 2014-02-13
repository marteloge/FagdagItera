$(function () {
    var timer,
        delay = 600;

    $('#current_age').bind('input', onChange);
    $('#pension_age').bind('input', onChange);
    $('#working_age').bind('input', onChange);
    $('#salary').bind('input', onChange);
    $('input[name=employer]').bind('change', onChange);
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
            employer:        $('input[name=employer]').val(),
            service_pension: $('#service_pension').val()
        }).done(function (data) {
            console.log(data);
            $('form').after('<div id="results" class="alert alert-success">Forventet årlig pensjon er ' + data + '.</div>');
        }).fail(function () {
            $('form').after('<div id="results" class="alert alert-danger">En feil forekom!</div>');
        });
    }
});
