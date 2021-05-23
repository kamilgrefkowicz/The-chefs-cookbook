$(function() {
    $("#ingredientLabel").autocomplete({
        source:"/autocomplete",
        minLength: 3,
        select: function (event, ui) {
            this.value = ui.item.label;
            $("#ingredientId").val(ui.item.value);
            return false;
        }
    }) ;
});