function onCheck(){
    var checkBoxRecurring = document.getElementById("recurring");
    var recurringLength = document.getElementById("recurringLengthString");

    if(checkBoxRecurring.checked === true){
        recurringLength.style.display = "flex";
    }else{
        recurringLength.style.display = "none";
    }
}