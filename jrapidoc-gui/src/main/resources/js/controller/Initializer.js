/**
 * Created by papa on 21.3.15.
 */
var Initialiser = function () {

};

Initialiser.prototype.initialise = function () {
//    window.properties = new Properties();
    window.apiModel = new ApiModel();
    apiModel.loadModel(Properties.apiModelPath);
    new Graphics().init(apiModel.modelJSON);
    var listener = new Listeners();
};