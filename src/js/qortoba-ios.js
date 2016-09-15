
//
// iOS specific service to call Objective-C / Swift code from JavaScript
//

angular.module('qortoba', [])

.factory('QortobaService', ['$log', function($log) {
	
	// Standard qortoba service API

	return {
		
		exec : function(service, action, argsArr) {

			// Call to native iOS is done via naigation
			// to a URL that encodes the action and params

			// URI-encode the action as it is an objective-C selector
			// that may contain ':'

			var url = 'qortoba' + '://' + service + '.' + encodeURIComponent(action);

			// check if there are arguments

			if (argsArr) {
				
				// Encode the params as a JSON array 	

				var encodedParams = JSON.stringify(argsArr);				

				url = url + '?' + encodedParams; 
			}
			
			// "navigate" to the action

			document.location.href = url;
		}
	};
}]);
	

