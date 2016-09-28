//
// Angular module and service used for demonstration
//
// It is supposed to be called from outside 
// JavaScript using Qortoba.callback function
//
// When called, it simply forwards the call
// back to the native code using the exec()
// function of the QortobaService
//

angular.module("example", ['qortoba'])

//
// Functions of the example service simply forward calls to
// the native methods of the same name.
//

.factory("ExampleService", function(QortobaService) {
	
	return {
		action1 : function() {
			QortobaService.exec("sample", "action1", null);
		},
		action2 : function(param1, param2) {
			QortobaService.exec("sample", "action2", [param1, param2]);
		}		
	};
});
