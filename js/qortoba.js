function Qortoba(apiVersion) {
	this.apiVersion = apiVersion;
}

//
// Callled externally (from outside JavaScript)
//

Qortoba.prototype.callback = function(apiVersion, serviceName, methodName, paramsArray) {

    // get the global injector, expects an ng-app definition

    var injector = angular.element('[ng-app]').injector();

    if (! injector) {
    	console.log('WARN: Injector not found for ng-app.');
    	return;
    }

	var service = injector.get(serviceName);
	if (! service) {
		console.log('WARAN: Angular service $service_name not found.');
		return;
	}

	var method = service[methodName];
	if (! method) {
		console.log('WARAN: Angular service method $method_name not found.');
		return;		
	}

	var paramsDeserializedAsJson = JSON.parse(paramsArray);
	
	console.log("NO_SER:" + paramsArray.length);
	console.log("DE_SER:" + paramsDeserializedAsJson.length);
	
	console.log("PARAMS: " + paramsDeserializedAsJson);
	
	method.apply(window, paramsDeserializedAsJson);
};

//
// Declare the qortoba global object 
//

console.log("QORTOBA: Loading the qortoba class.");

window.qortoba = new Qortoba(1.0);