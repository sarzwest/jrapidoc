/**
 * Created by Tomas "sarzwest" Jiricek on 21.3.15.
 */
var Listeners = function () {

};

Listeners.prototype.init = function () {
    this.regEvents();
    this.switchListeners();
};

Listeners.prototype.loadModel = function (e) {
    try {
        ProgressBar.showProgressBar();
        var modelPath = document.querySelector("#modelUrl").value;
        window.apiModel.loadModel(modelPath);
        window.graphics.show(window.apiModel.modelJSON);
        window.graphics.openTypesElement();
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

Listeners.prototype.switchListeners = function () {
    var switchButton = document.querySelector("#loadModel");
    switchButton.addEventListener("click", this.loadModel);
};

Listeners.prototype.regEvents = function () {

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
};