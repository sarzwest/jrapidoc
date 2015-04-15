/**
 * Created by papa on 21.3.15.
 */
var Listeners = function(){

};

Listeners.prototype.regEvents = function() {

    $('.header').click(function () {
        var parent = $(this).parent();

        if (parent.hasClass('closed')) {
            parent.removeClass('closed');
            parent.addClass('open');
        } else {
            parent.removeClass('open');
            parent.addClass('closed');
        }
    });
}