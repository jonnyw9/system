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
        select: function (info) {
            console.log(info);
            let startString = info.startStr.substring(0,16);
            let endString = info.endStr.substring(0,16);
            window.location.href = "http://localhost:8080/event/add/" + userId + "?time=" + startString + "&end=" + endString;
        }
    });

   calendar.render();


});