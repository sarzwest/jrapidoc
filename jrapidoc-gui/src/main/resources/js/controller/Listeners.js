/**
 * Created by Tomas "sarzwest" Jiricek on 21.3.15.
 */
var Listeners = function () {

};

Listeners.prototype.init = function () {
    this.regEvents();
    this.loadListener();
};

Listeners.prototype.loadModel = function (e) {
    try {
        ProgressBar.showProgressBar();
        var modelPath = document.querySelector("#modelUrl").value;
        window.apiModel.loadModel(modelPath);
        window.graphics.show(window.apiModel.modelJSON);
        window.graphics.closeMethodElement();
        window.graphics.createAnchorsToTypes();
    }catch (e){
        if(e instanceof CaughtException){
            Logger.error(e.getMsg());
        }else{
            Logger.error("Unexpected error during retrieving model");
        }
    }finally{
        ProgressBar.hideProgressBar();
    }
};

Listeners.prototype.loadListener = function () {
    var switchButton = document.querySelector("#loadModel");
    switchButton.addEventListener("click", this.loadModel);
};

Listeners.prototype.regEvents = function () {

    $('.header').click(function () {
        var parent = $(this).parent();

        if (parent.hasClass('closed')) {
            parent.children(".children").slideToggle("slow");
            parent.children(".children").promise().done(
                function(onFired){
                    parent.removeClass('closed');
                    parent.addClass('open');
                }
            );
//            parent.removeClass('closed');
//            parent.addClass('open');
//            parent.children(".children").fadeIn(1000);
        } else {
            parent.children(".children").slideToggle("slow");
            parent.children(".children").promise().done(
                function(onFired){
                    parent.removeClass('open');
                    parent.addClass('closed');
                }
            );
//            parent.removeClass('open');
//            parent.addClass('closed');
//            parent.children(".children").fadeOut(1000);
        }
    });
};