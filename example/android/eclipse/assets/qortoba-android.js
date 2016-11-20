
//
// Android specific service to call Android code from JavaScript
//

angular.module('qortoba', [])

.factory('QortobaService', ['$log', function($log) {
	
	// Service API

	return {
		
		exec : function(service, action, argsArr) {
			
			// Android code can inject an object for each service
			
			var injectedJsApi = window[service];
			
			if (! injectedJsApi) {
				$log("QORTOBA: No injected object named " + service + ".");
				return;
			}
			
			var fun = injectedJsApi[action];
			
			if (! fun) {
				$log("QORTOBA: No method named " + action + ".");
				return;
			}
			
			// invoke the method

			fun.apply(injectedJsApi, argsArr)
		}
	};
}]);
	

