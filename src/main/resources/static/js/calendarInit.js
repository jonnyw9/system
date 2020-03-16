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
                return info.start.getTime() > new Date();
            }else{
                var duration = info.end.getTime() - info.start.getTime();
                console.log(duration);
                if(duration > 1800000){
                    return false;
                }else{
                    const HOURS = 1000*60*60*24;
                    var dayBefore = info.start.getTime() - HOURS;
                    var now = new Date();
                    return dayBefore >= now ;
                }
            }
        },
        select: function (info) {
            console.log(info);
            let startString = info.startStr.substring(0,16);
            let endString = info.endStr.substring(0,16);

            window.location.href = "https://jwbookingsystem.herokuapp.com/event/add/" + userId + "?time=" + startString + "&end=" + endString;
        }
    });

   calendar.render();
});