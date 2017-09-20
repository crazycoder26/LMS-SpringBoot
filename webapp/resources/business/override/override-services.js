/**
 * 
 */
lmsApp.factory("overrideService", function($http, borrowerConstants){
	return{
		overrideDateService: function(loans, days){
			var data = {};
			return $http({
				url: "http://localhost:8080/lms/loan/" + days,
				method: "PUT",
				data : loans
			}).success(function(data){
				data = data;
			}).then(function(){
				return data;
			})
		}
	}
	
})