/**
 * Created by papa on 21.3.15.
 */
var ApiModel = function(){
    this.modelJSON;
};

/**
 * Stores the model in JSON format in modelJSON property
 * @param modelUrl URL where model can be retrieved
 */
ApiModel.prototype.loadModel = function(modelUrl){
    var stateChanged = function (caller) {
        if (this.readyState === 4) {
            console.log(this.responseText);
            var modelJSON = caller.checkAndGetModel(this.responseText);
            caller.setModelJSON(modelJSON);
        }
    };

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open('GET', modelUrl, false);
    var oldThis = this;
    var callback = stateChanged.bind(xmlhttp);
    xmlhttp.addEventListener('readystatechange', function (){callback(oldThis)});
    xmlhttp.send();
};

/**
 * Checks model in string format and retrieves model in JSON. When problem during checking, exception is thrown.
 * @param givenModel model in string format
 * @returns {*} model in JSON format
 */
ApiModel.prototype.checkAndGetModel = function(givenModel){
    try {
        givenModel = givenModel.replace(/[<]/g, "&lt;").replace(/[>]/g, "&gt;");
        var modelJSON = JSON.parse(givenModel);
        return modelJSON;
    }catch (e){
        console.log("error occured");
        throw e;
    }
};

ApiModel.prototype.setModelJSON = function(modelJSON){
    this.modelJSON = modelJSON;
};

