document.addEventListener('DOMContentLoaded', function(){
    var calendarE1 = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarE1, {
        plugins: ['dayGrid', 'timeGrid', 'interaction', 'bootstrap', 'list'],
        defaultView: 'timeGridWeek',
        header: {
            left: 'timeGridWeek dayGridMonth listWeek',
            center: 'title',
            right: 'today prev next'
        },
        buttonText: {
            today: 'Today',
            month: 'Month',
            week: 'Week',
            list: 'List Events'
        },
        themeSystem: 'bootstrap',
        minTime: dayStart,
        maxTime: dayEnd,
        nowIndicator: true,
        longPressDelay: 500,
        eventLongPressDelay: 500,
        selectLongPressDelay: 500,
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

            window.location.href = "https://jwbookingsystem.herokuapp.com/" + userId + "?time=" + startString + "&end=" + endString;
        }
    });

   calendar.render();
});