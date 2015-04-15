/**
 * Created by papa on 21.3.15.
 */
var Graphics = function () {
    this.transforms;
};

Graphics.prototype.transforms = {
    'object': {'tag': 'div', 'class': 'package ${show} ${type}', 'children': [
        {'tag': 'div', 'class': 'header', 'children': [
            {'tag': 'div', 'class': function (obj) {
                if (Graphics.prototype.getValue(obj.value) !== undefined) return('arrow hide');
                else return('arrow');
            }},
//                {'tag':'span','class':'name','html':'${name}'},
            {'tag': 'span', 'class': 'name', 'html': function (obj) {
                return Graphics.prototype.anchor(obj);
//                    return obj.name;
            }},
            {'tag': 'span', 'class': 'value', 'html': function (obj) {
                var value = Graphics.prototype.getValue(obj.value);
//                    if (value !== undefined) return(" : " + value);
                if (value !== undefined) return(" : " + Graphics.prototype.anchorLink(obj));
                else return('');
            }}
//                , {'tag':'span','class':'type','html':'${type}'}
        ]},
        {'tag': 'div', 'class': 'children', 'children': function (obj) {
            return(Graphics.prototype.children(obj.value));
        }}
    ]}
};

Graphics.prototype.anchorLink = function (obj) {
    if (obj.name == "typeRef" || obj.name == "includeTypeRef") {
        return "<a href='#" + obj.value + "'>" + obj.value + "</a>";
    } else {
        return obj.value;
    }
};

Graphics.prototype.anchor = function (obj) {
    return "<span id='" + obj.name + "'>" + obj.name + "</span>";
};

Graphics.prototype.openTypesElement = function () {
    var top = document.querySelector("#top");
    top.children[0].children[1].children[2].setAttribute("class", "package open object");
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
            //Itterrate through the array and add it to the elements array
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
            //This must be a litteral (or function)
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

/**
 * Initializes HTML elements from JSON model
 * @param modelJSON
 */
Graphics.prototype.init = function (modelJSON) {
    this.addRootResourceElementsToMenu(modelJSON.rootResources, modelJSON.baseUrl);
    this.createTypesContent(modelJSON.types);
};

/**
 * Creates and adds type HTML elements to page
 * @param typesModel
 */
Graphics.prototype.createTypesContent = function (typesModel) {
    var typesContentElement = document.querySelector("#typesContent");
    for (var key in typesModel) {
        var typeElement = this.createTypeElement(key, typesModel[key]);
        typesContentElement.appendChild(typeElement);
    }
};

/**
 * Creates clone from HTML element template
 * @param byId template id
 * @param newId id for new clone
 * @returns {Node}
 */
Graphics.prototype.createGenericElement = function (byId, newId) {
    var template = document.querySelector("#" + byId);
    var newElement = template.cloneNode(true);
    newElement.setAttribute("id", newId);
    return newElement;
};

/**
 * Creates new HTML element for one type from model
 * @param typeName name of type
 * @param typeModel properties of type
 * @returns {Node}
 */
Graphics.prototype.createTypeElement = function (typeName, typeModel) {
    var typeElement = this.createGenericElement("typeTemplate", typeName);
    typeElement.children[0].textContent = typeName;
    var description = this.createDescriptionElement(typeModel.description);
    typeElement.appendChild(description);
    var properties = this.createPropertiesElement(typeModel.properties);
    if (Utils.isNotEmpty(properties)) {
        typeElement.appendChild(properties);
    }
    var options = this.createOptionsElement(typeModel.options);
    if (Utils.isNotEmpty(options)) {
        typeElement.appendChild(options);
    }
    return typeElement;
};

/**
 * Creates HTML element for description property from model
 * @param description
 * @returns {Node}
 */
Graphics.prototype.createDescriptionElement = function (description) {
    var descriptionElement = this.createGenericElement("descriptionTemplate", "");
    descriptionElement.children[1].textContent = description;
    return descriptionElement;
};

/**
 * Creates table's td element with text inside
 * @param textContent text inside element
 * @returns {HTMLElement}
 */
Graphics.prototype.createTdElement = function (textContent) {
    var td = document.createElement("td");
    td.textContent = textContent;
    return td;
};

/**
 * Creates property HTML element for type properties
 * @param propertiesModel
 * @returns {*}
 */
Graphics.prototype.createPropertiesElement = function (propertiesModel) {
    if (propertiesModel.length == 0) {
        return null;
    }
    var propertiesElement = this.createGenericElement("propertiesTemplate", "");
    var tableElement = propertiesElement.children[0];
    for (var key in propertiesModel) {
        var tr = document.createElement("tr");
        tr.appendChild(this.createTdElement(key));
        tr.appendChild(this.createTdElement(propertiesModel[key].typeref));
        tr.appendChild(this.createTdElement(propertiesModel[key].description));
        tableElement.appendChild(tr);
    }
    return tableElement;
};

Graphics.prototype.createOptionsElement = function (optionsModel) {
    if (Utils.isEmpty(optionsModel)) {
        return null;
    }
    var optionsElement = this.createGenericElement("optionsTemplate", "");
    for (var i = 0; i < optionsModel.length; i++) {
        var optionElement = document.createElement("span");
        optionElement.textContent = optionsModel[i];
        optionsElement.appendChild(optionElement);
    }
    return optionsElement;
};

Graphics.prototype.createTyperefElement = function (typeref) {
    if (Utils.isEmpty(typeref)) {
        return null;
    }
    var typerefElement = this.createGenericElement("typerefTemplate", "");
    typerefElement.children[1].textContent = typeref;
    return typerefElement;
};

Graphics.prototype.createRequiredElement = function (required) {
    if (Utils.isEmpty(required)) {
        return null;
    }
    var requiredElement = this.createGenericElement("requiredTemplate", "");
    requiredElement.children[1].textContent = required;
    return requiredElement;
};

Graphics.prototype.createParameterElement = function (parameterName, parameterModel) {
    if (Utils.isEmpty(parameterModel)) {
        return null;
    }
    var parameterElement = this.createGenericElement("paramTemplate", "");
    parameterElement.children[0].textContent = parameterName;
    var description = this.createDescriptionElement(parameterModel.description);
    parameterElement.appendChild(description);
    var typerefElement = this.createTyperefElement(parameterModel.typeref);
    if (Utils.isNotEmpty(typerefElement)) {
        parameterElement.appendChild(typerefElement);
    }
    var optionsElement = this.createOptionsElement(parameterModel.options);
    if (Utils.isNotEmpty(optionsElement)) {
        parameterElement.appendChild(optionsElement);
    }
    var requiredElement = this.createRequiredElement(parameterModel.required);
    if (Utils.isNotEmpty(requiredElement)) {
        parameterElement.appendChild(requiredElement);
    }
    return parameterElement;
};

Graphics.prototype.createParameterTypeElement = function (typeName, typeModel) {
    if (Utils.isEmpty(typeModel)) {
        return null;
    }
    var paramTypeElement = this.createGenericElement("parameterTypeTemplate", "");
    paramTypeElement.children[0].textContent = typeName;
    for (var key in typeModel) {
        var paramElement = this.createParameterElement(Utils.subs(key), typeModel[key]);
        if (Utils.isNotEmpty(paramElement)) {
            paramTypeElement.appendChild(paramElement);
        }
    }
    return paramTypeElement;
};

Graphics.prototype.createRequestElement = function (requestModel) {
    if (Utils.isEmpty(requestModel)) {
        return null;
    }
    var paramWrapElement = this.createGenericElement("parameterTypeTemplate", "");
    paramWrapElement.children[0].textContent = Properties.request;
    for (var key in requestModel) {
        if (Utils.isNotEmpty(requestModel[key])) {
            if (key != "body") {
                paramWrapElement.appendChild(this.createParameterTypeElement(Utils.subs(key), requestModel[key]));
            }
        }
    }
    var body = this.createParameterElement(Utils.subs("body"), requestModel.body);
    if (Utils.isNotEmpty(body)) {
        paramWrapElement.appendChild(body);
    }
    return paramWrapElement;
};

Graphics.prototype.createResponsesElement = function (responsesModel) {
    var responsesElement = this.createGenericElement("responsesTemplate", "");
    responsesElement.children[0].textContent = Properties.responses;
    for (var i = 0; i < responsesModel.length; i++) {
        responsesElement.appendChild(this.createResponseElement(responsesModel[i]));
    }
    return responsesElement;
};

Graphics.prototype.createResponseElement = function (responseModel) {
    var responseElement = this.createGenericElement("responseTemplate", "");
    responseElement.children[0].textContent = Properties.response;
    var statusElement = this.createStatusElement(responseModel.status);
    responseElement.appendChild(statusElement);
    var headersElement = this.createParameterTypeElement(Properties.headers, responseModel.headers);
    if (Utils.isNotEmpty(headersElement)) {
        responseElement.appendChild(headersElement);
    }
    var cookiesElement = this.createParameterTypeElement(Properties.cookies, responseModel.cookies);
    if (Utils.isNotEmpty(cookiesElement)) {
        responseElement.appendChild(cookiesElement);
    }
    var returnType = this.createParameterElement(Properties.returnType, responseModel.return);
    if (Utils.isNotEmpty(returnType)) {
        responseElement.appendChild(returnType);
    }
    return responseElement;
};

Graphics.prototype.createStatusElement = function (status) {
    if (Utils.isEmpty(status)) {
        return null;
    }
    var statusElement = this.createGenericElement("statusTemplate", "");
    statusElement.children[1].textContent = status;
    return statusElement;
};

Graphics.prototype.createMethodElement = function (methodName, methodModel) {
    var methodElement = this.createGenericElement("methodTemplate", "");
    methodElement.children[0].textContent = methodName;
    var requestElement = this.createRequestElement(methodModel.request);
    if (Utils.isNotEmpty(requestElement)) {
        methodElement.appendChild(requestElement);
    }
    methodElement.appendChild(this.createResponsesElement(methodModel.responses));
    return methodElement;
};

Graphics.prototype.createMethodsElement = function (methodsModel) {
    var methodsElement = this.createGenericElement("methodsTemplate", "");
    methodsElement.children[0].textContent = Properties.httpmethods;
    for (var key in methodsModel) {
        methodsElement.appendChild(this.createMethodElement(key, methodsModel[key]));
    }
    return methodsElement;
};

Graphics.prototype.addRootResourceElementsToMenu = function (rootResourcesModel, previousPath) {
//    var ul = document.querySelector("#menu ul");
//    for (i = 0; i < rootResources.length; i++) {
//        var li = document.createElement("li");
//        li.textContent = rootResources[i].path;
//        ul.appendChild(li);
//    }
    var ul = document.querySelector("#menu ul");
    var id = rootResourcesModel.id;
    for (var i = 0; i < rootResourcesModel.length; i++) {
        var rootResourceElement = this.createResourceElement(rootResourcesModel[i], previousPath);
        ul.appendChild(rootResourceElement);
    }
};

Graphics.prototype.createResourceElement = function (resourceModel, previousPath) {
    var fullPath = previousPath.trimSlash() + "/" + resourceModel.path.trimSlash();
    var resourceElement = this.createGenericElement("resourceTemplate", "");
    var pathElement = this.createPathElement(fullPath);
    resourceElement.appendChild(pathElement);
    var methodsElement = this.createMethodsElement(resourceModel.httpmethods);
    if (Utils.isNotEmpty(methodsElement)) {
        resourceElement.appendChild(methodsElement);
    }
    if (Utils.isNotEmpty(resourceModel.resources) && resourceModel.resources.length > 0) {
        var subresourceElement = this.createGenericElement("subresourceTemplate", "");
        for (var i = 0; i < resourceModel.resources.length; i++) {
            subresourceElement.appendChild(this.createResourceElement(resourceModel.resources[i], fullPath));
        }
        resourceElement.appendChild(subresourceElement);
    }
    return resourceElement;
};

Graphics.prototype.createPathElement = function (path) {
    if (Utils.isEmpty(path)) {
        return null;
    }
    var pathElement = this.createGenericElement("pathTemplate", "");
    pathElement.children[0].textContent = path;
    return pathElement;
};