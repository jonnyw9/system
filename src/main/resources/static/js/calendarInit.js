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
        allDaySlot: false,
        selectable: true,
        selectMirror: true,
        selectOverlap: false,
        events: eventUrl,
        eventClick: function(info){
            if(!isHome){
                info.jsEvent.preventDefault();
            }
        },
        selectAllow: function(info){
            if(isHome){
                return true;
            }else{
                var duration = info.end.getTime() - info.start.getTime();
                console.log(duration);
                return duration <= 1800000;
            }
        },
        select: function (info) {
            console.log(info);
            let startString = info.startStr.substring(0,16);
            let endString = info.endStr.substring(0,16);

            window.location.href = "http://localhost:8080/event/add/" + userId + "?time=" + startString + "&end=" + endString;
        }
    });

   calendar.render();
});