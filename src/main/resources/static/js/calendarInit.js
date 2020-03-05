document.addEventListener('DOMContentLoaded', function(){
    var calendarE1 = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarE1, {
        plugins: [ 'timeGrid' ],
        defaultView: 'timeGridWeek',
        minTime: "07:00:00",
        maxTime: "19:00:00",
        nowIndicator: true,
        height: "auto",
        weekends: false,
        selectable: true,
        selectMirror: true,
        selectOverlap: false,
        events: eventUrl
    });

   calendar.render();

});