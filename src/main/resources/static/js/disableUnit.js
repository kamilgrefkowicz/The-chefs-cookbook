window.onload = function () {
    document.getElementById('choseDish').onclick = function () {

        var unitsNeeded = this.checked;

        var units = this.form.elements['unit'];

        for (var i = 0, len = units.length; i < len; i++) {
            var r = units[i];

            if (r.id === 'pc') {
                r.checked = true;
            }
            r.disabled = unitsNeeded;



        }
    }
    document.getElementById('choseIntermediate').onclick = function () {

        var unitsNeeded = !this.checked;

        var units = this.form.elements['unit'];

        for (var i = 0, len = units.length; i < len; i++) {
            var r = units[i];

            r.disabled = unitsNeeded;
        }
    }
}