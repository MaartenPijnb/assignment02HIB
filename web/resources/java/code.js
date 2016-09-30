/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


console.log("javascript laad");

$(document).ready(function () {
    console.log('test');
    

     $('.dateStart').datetimepicker({
         locale: 'de'
     });
        $('.dateDeadline').datetimepicker({
             locale: 'de',
            useCurrent: false //Important! See issue #1075
        });
        $(".dateStart").on("dp.change", function (e) {
            $('.dateDeadline').data("DateTimePicker").minDate(e.date);
        });
        $(".dateDeadline").on("dp.change", function (e) {
            $('.dateStart').data("DateTimePicker").maxDate(e.date);
        });
});
