/**
 * Created by papa on 21.3.15.
 */
var Initializer = function () {

};

Initializer.prototype.initialize = function () {
    window.apiModel = new ApiModel();
    window.apiModel.loadModel(Properties.restModelPath);
    window.listener = new Listeners();
    window.listener.regEvents();
    window.graphics = new Graphics();
    window.graphics.show(window.apiModel.modelJSON);
    window.graphics.openTypesElement();
//    new Graphics().init(apiModel.modelJSON);
//    var listener = new Listeners();
};