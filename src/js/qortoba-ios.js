
//
// iOS specific service to call Objective-C / Swift code from JavaScript
//

angular.module('qortoba', [])

.factory('QortobaService', ['$log', function($log) {
	
	// Standard qortoba service API

	return {
		
		exec : function(service, action, argsArr) {
			
			// Encode the params as a JSON array 

			var encodedParams = JSON.stringify(argsArr);

			// Call to native iOS is done via naigation
			// to a URL that encodes the action and params

			var url = 'qortoba' + '://' service + '.' action + '?' + encodedParams;
			
			document.location.href = url;
		}
	};
}]);
	

