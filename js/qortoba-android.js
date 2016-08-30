
//
// Android specific service to call Android code from JavaScript
//


// Expect the 'qortoba' module to be already defined

angular.module('qortoba')

.factory('Qortoba', ['$log', function($log) {
	
	// service API

	return {
		
		exec : function(service, action, argsArr) {
			
			// Android code can inject an object for each service

			var injectedJsApi = window[service];
			var fun = injectedJsApi[action];

			// invoke the method

			fun.apply(injectedJsApi, args)
		}
	};
}]);
	

