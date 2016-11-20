angular.module("alert", ['qortoba'])

.factory("AlertService", function(QortobaService) {
	
	return {
		show : function(msg) {
			QortobaService.exec("test", "greet", ["Hello"]);
		}
	};
});