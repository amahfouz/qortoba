//
// Class which handles calls from native code
//

function Qortoba() {

    // map from object ID to object
    //
    // Holds references to objects created using 'instantiate' method
    // so that they do not garbage-collected
    // Upon destruction, the reference is removed allowing for GC
    //
    this.objects = {};
}

//
// These methods are called externally (from outside JavaScript)
//

Qortoba.prototype.instantiate = function(objectId, className, constructorArgsJson) {

    // retrieve the class from the global context
    var clazz = window[className];

    // check that the class is actually defined
    if (! clazz) {
        console.log("Qortoba error: " + clazz + " not defined.");
        return;
    }

    // args are a JSON array to be converted into an array
    var constructorArgs = constructorArgsJson 
        ? JSON.parse(constructorArgsJson)
        : [];

    // create a function for the class constructor
    var f = Function.prototype.bind.apply(clazz, constructorArgs);

    // instantiate an object from the function
    var obj = new f();

    // add the object to the map to keep it from being GCed
    objects[objectId] = obj;
};

//
// Destroy the Qortoba JS object with the specified ID
// Reference to the object are cleared and if the object
// has a "destroy" method it is called first
//
Qortoba.prototype.destroy = function(objectId) {
    var obj = this.objects[objectId];

    if (obj) {

        // if the object has a "destroy" method call it
        if (typeof obj.destroy === function) {
            obj.destroy();
        }

        // nullify reference to object to allowed to be GCed
        this.objects[objectId] = null;
    }
};

//
// Invokes the specified method on the object with the 
// specified ID passing it the JSON array of params
//
//
Qortoba.prototype.invoke = function(objectId, methodName, paramsArrayJson) {

    // retrieve the object
    var object = this.objects[objectId];

    if (! object) {
        console.log("Object with ID: " + objectId + " not found.");
        return;
    }

    // object found, find the method
    var func = object[methodName];
    if (! (typeof func === function)) {
        console.log("Method " + methodName + " not found.");
        return;
    }

    // parse the JSON params
    var args = paramsArrayJson 
        ? JSON.parse(paramsArrayJson)
        : [];

    // invoke the method    
    func.apply(object, args);
}

//
// Inokes the specified method on an Angular service 
// Angular is expected to have been initialized and
// and an 'ng-app' element is required for finding 
// the root scope
//
Qortoba.prototype.angular = function(serviceName, methodName, paramsArrayJson) {

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

window.qortoba = new Qortoba();
