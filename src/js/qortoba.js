function Qortoba(apiVersion) {
	this.apiVersion = apiVersion;
}

//
// Callled externally (from outside JavaScript)
//

Qortoba.prototype.callback = function(apiVersion, serviceName, methodName, paramsArrayJson) {

    // get the global injector, expects an ng-app definition

    var injector = angular.element('[ng-app]').injector();

    if (! injector) {
    	console.log('WARN: Injector not found for ng-app.');
    	return;
    }

	var service = injector.get(serviceName);
	if (! service) {
		console.log('WARN: Angular service $service_name not found.');
		return;
	}

	var method = service[methodName];
	if (! method) {
		console.log('WARAN: Angular service method $method_name not found.');
		return;		
	}

	var paramsArray = JSON.parse(paramsArrayJson);
	
	method.apply(window, paramsArray);
};

//
// Declare the qortoba global object 
//

console.log("QORTOBA: Loading qortoba.");

window.qortoba = new Qortoba(1.0);