/**
 * Created by Tomas "sarzwest" Jiricek on 21.3.15.
 */
var Graphics = function () {
    this.transforms;
};

Graphics.prototype.init = function () {
    var input = document.querySelector("#modelUrl");
    input.value = Properties.defaultModelPath;
};

Graphics.prototype.setCSSClass = function (obj) {
    var classes = "package open object ";
    if (Properties.keys.customInfo == obj.name) {
        return classes + "info";
    } else if (Properties.keys.types == obj.name) {
        return classes + "types";
    } else if (Properties.keys.serviceGroups == obj.name) {
        return classes + "serviceGroups";
    } else if (Properties.keys.services == obj.name) {
        return classes + "services";
    } else if (Properties.keys.methods == obj.name) {
        return classes + "methods";
    } else if (obj.name == Properties.keys.cookieParams ||
        obj.name == Properties.keys.formParams ||
        obj.name == Properties.keys.headerParams ||
        obj.name == Properties.keys.matrixParams ||
        obj.name == Properties.keys.pathParams ||
        obj.name == Properties.keys.queryParams) {
        return classes + "httpParameters";
    } else if (Properties.keys.parameters == obj.name) {
        return classes + "parameters";
    } else if (Properties.keys.returnOptions == obj.name) {
        return classes + "returnOptions";
    } else if (Properties.keys.soapInputHeaders == obj.name) {
        return classes + "soapInputHeaders";
    } else if (Properties.keys.soapOutputHeaders == obj.name) {
        return classes + "soapOutputHeaders";
    }
    return classes;
};

Graphics.prototype.transforms = {
    'object': {'tag': 'div', 'class': function (obj) {
        return Graphics.prototype.setCSSClass(obj)
    }, 'children': [
        {'tag': 'div', 'class': 'header', 'children': [
            {'tag': 'div', 'class': function (obj) {
                if (Graphics.prototype.getValue(obj.value) !== undefined) return('arrow hide');
                else return('arrow');
            }},
            {'tag': 'span', 'class': 'name', 'html': '${name}'},
            {'tag': 'span', 'class': 'value', 'html': function (obj) {
                var value = Graphics.prototype.getValue(obj.value);
                if (value !== undefined) return(" : " + Graphics.prototype.anchorLink(obj));
                else return('');
            }}
        ]},
        {'tag': 'div', 'class': 'children', 'children': function (obj) {
            return(Graphics.prototype.children(obj.value));
        }}
    ]}
};

Graphics.prototype.anchorLink = function (obj) {
    if (obj.name == Properties.keys.typeRef || obj.name == Properties.keys.includeTypeRef || obj.name == Properties.keys.keyTypeRef || obj.name == Properties.keys.valueTypeRef) {
        return "<a href='#" + obj.value + "'>" + obj.value + "</a>";
    } else {
        return obj.value;
    }
};

Graphics.prototype.anchor = function (obj) {
    return "<span id='" + obj.name + "'>" + obj.name + "</span>";
};

Graphics.prototype.closeMethodElement = function () {
    var methodList = $(".methods > .children > .package.open.object");
    methodList.removeClass("open");
    methodList.addClass("closed");
};

Graphics.prototype.createAnchorsToTypes = function () {
    var top = document.querySelector("#top");
    var nodesArray = top.children[0].children[1].children;
    for (var i = 0; i < nodesArray[nodesArray.length - 1].children[1].children.length; i++) {
        var type = nodesArray[nodesArray.length - 1].children[1].children[i];
        var typeRef = type.children[0].children[1].innerHTML;
        type.children[0].children[1].innerHTML = "<span id='" + typeRef + "'>" + typeRef + "</span>";
    }
};

Graphics.prototype.show = function (modelJSON) {
    //Visualize sample
    this.visualize(modelJSON);
};

Graphics.prototype.visualize = function (json) {

    $('#top').html('');

    $('#top').json2html(this.convert(Properties.modelRootName, json, 'open'), this.transforms.object);

    window.listener.regEvents();
};

Graphics.prototype.children = function (obj) {
    var type = $.type(obj);

    //Determine if this object has children
    switch (type) {
        case 'array':
        case 'object':
            return(json2html.transform(obj, this.transforms.object));
            break;

        default:
            //This must be a litteral
            break;
    }
};

Graphics.prototype.convert = function (name, obj, show) {

    var type = $.type(obj);

    if (show === undefined) show = 'closed';

    var children = [];

    //Determine the type of this object
    switch (type) {
        case 'array':
            //Transform array
            //Iterate through the array and add it to the elements array
            var len = obj.length;
            for (var j = 0; j < len; ++j) {
                //Concat the return elements from this objects tranformation
                children[j] = this.convert(j, obj[j]);
            }
            break;

        case 'object':
            //Transform Object
            var j = 0;
            for (var prop in obj) {
                children[j] = this.convert(prop, obj[prop]);
                j++;
            }
            break;

        default:
            //This must be a literal (or function)
            children = obj;
            break;
    }

    return( {'name': name, 'value': children, 'type': type, 'show': show} );

};

Graphics.prototype.getValue = function (obj) {
    var type = $.type(obj);

    //Determine if this object has children
    switch (type) {
        case 'array':
        case 'object':
            return(undefined);
            break;

        case 'function':
            //none
            return('function');
            break;

        case 'string':
            return("'" + obj + "'");
            break;

        default:
            return(obj);
            break;
    }
};
