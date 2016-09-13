angular.module("example", ['qortoba'])

.factory("ExampleService", function(QortobaService) {
	
	return {
		action1 : function() {
			QortobaService.exec("sample", "action1", null);
		},
		action2 : function(param1, param2) {
			QortobaService.exec("sample", "action2", [param1, param2]]);
		}		
	};
});