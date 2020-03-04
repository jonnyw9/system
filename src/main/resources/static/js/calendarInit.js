document.addEventListener('DOMContentLoaded', function(){
   var calendarE1 = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarE1, {
        plugins: [ 'timeGrid' ],
        defaultView: 'timeGridWeek'
    });

   calendar.render();
});