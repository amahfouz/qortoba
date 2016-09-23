
//
// iOS specific service to call Objective-C / Swift code
// from JavaScript
//

//
// Invokes an Objective-C selector by navigating to a URL of
// the form:
//
//    qortoba://<service_name>.<selector_name>?<encoded_args_array>
//
// The navigation is supposed to be intercepted by the delegate
// of the corresponding UIView and routed to Qortoba for handling
//

angular.module('qortoba', [])

.factory('QortobaService', ['$log', function($log) {
	
	// Standard qortoba service API

	return {
		
		exec : function(service, action, argsArr) {

			// Call to native iOS is done via naigation
			// to a URL that encodes the action and params

			// URI-encode the action which is an identifier
            // for an objective-C selector excluding the ':'

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
	

