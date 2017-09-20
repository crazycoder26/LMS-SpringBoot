/**
 * 
 */
lmsApp.controller("override", function($scope, $window, $http, borrowerService, overrideService,  Pagination){
	
	borrowerService.initLoansService().then(function(data){
		$scope.loan = data;
	})
	
	borrowerService.getAllLoansService().then(function(data){
		$scope.loans = data;
		$scope.pagination = Pagination.getNew(10);
		$scope.pagination.numPages = Math.ceil($scope.loans.length/$scope.pagination.perPage);
	})
	
	$scope.override = function(bookId, branchId, cardNo, days){
		$scope.loan.bookId = bookId;
		$scope.loan.branchId = branchId;
		$scope.loan.cardNo = cardNo
		overrideService.overrideDateService($scope.loan, days).then(function(data){
			$window.location.reload();
		})
	}
	
});