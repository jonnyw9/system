function onCheck(){
    var checkBoxRecurring = document.getElementById("recurring");
    var recurringLength = document.getElementById("recurringLength");
    var recurringLengthLabel = document.getElementById("recurringLengthLabel");

    if(checkBoxRecurring.checked === true){
        recurringLength.style.display = "flex";
        recurringLengthLabel.style.display = "flex";
    }else{
        recurringLength.style.display = "none";
        recurringLengthLabel.style.display = "none";
    }
}