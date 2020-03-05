document.addEventListener('DOMContentLoaded', function(){
    var calendarE1 = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarE1, {
        plugins: [ 'timeGrid', 'interaction' ],
        defaultView: 'timeGridWeek',
        minTime: dayStart,
        maxTime: dayEnd,
        nowIndicator: true,
        height: "auto",
        weekends: false,
        allDaySlot: allDay,
        selectable: true,
        editable: true,
        selectMirror: true,
        selectOverlap: false,
        events: eventUrl,
        select: function (start, end) {
            console.log(start);
            let startString = start.startStr.substring(0,16)
            window.location.href = "http://localhost:8080/event/add/" + userId + "?time=" + startString;
        }
    });

   calendar.render();


});