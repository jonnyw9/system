/**
 * FullCalendar Copyright information.
 ************************************************************************
 Copyright (c) 2019 Adam Shaw

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


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
            let startString = info.startStr.substring(0,16);
            let endString = info.endStr.substring(0,16);

            window.location.href = "/event/add/" + userId + "?time=" + startString + "&end=" + endString;
        }
    });

   calendar.render();
});