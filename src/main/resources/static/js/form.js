function onCheck(){
    var checkBoxStudent = document.getElementById("student");
    var studentInput = document.getElementById("studentNumber");
    var studentLabel = document.getElementById("studentNumberLabel");

    var checkBoxStaff = document.getElementById("staff");
    var staffInput = document.getElementById("room");
    var staffLabel = document.getElementById("roomLabel");
    var startTimeInput = document.getElementById("startTime");
    var startTimeLabel = document.getElementById("startTimeLabel");
    var endTimeInput = document.getElementById("endTime");
    var endTimeLabel = document.getElementById("endTimeLabel");


    if(checkBoxStudent.checked === true){
        studentInput.style.display = "flex";
        studentLabel.style.display = "flex";
    }else{
        studentInput.style.display = "none";
        studentLabel.style.display = "none";
    }

    if(checkBoxStaff.checked === true){
        staffInput.style.display = "flex";
        staffLabel.style.display = "flex";
        startTimeInput.style.display = "flex";
        startTimeLabel.style.display = "flex";
        endTimeInput.style.display = "flex";
        endTimeLabel.style.display = "flex";
    }else{
        staffInput.style.display = "none";
        staffLabel.style.display = "none";
        startTimeInput.style.display = "none";
        startTimeLabel.style.display = "none";
        endTimeInput.style.display = "none";
        endTimeLabel.style.display = "none";
    }
}