lmsApp.controller("librarianController", function($scope, $window, $http, librarianService, Pagination){
	
	
	librarianService.ListAllBranchService().then(function(data){
		$scope.branches = data;
		$scope.showBranchTable = true;
		$scope.pagination = Pagination.getNew(10);
		$scope.pagination.numPages = Math.ceil($scope.branches.length/$scope.pagination.perPage);
	})
	
	$scope.getAllbranchBooks = function(branchId){
		$scope.branchId = branchId;
		librarianService.getAllbranchBooksService(branchId).then(function(data){
			$scope.books = data;
			$scope.showBranchTable = false;
			$scope.showBooksTable = true;
		})
	}

	$scope.addCopies = function(noCopies, bookId){
		librarianService.updateCopiesService($scope.branchId, bookId, noCopies).then(function(data){
			$scope.showBranchTable = true;
			$scope.showBooksTable = false;
		})
	}
	
	$scope.editBranch = function(){
		$scope.showBranchTable = false;
		$scope.showBooksTable = false;
		$scope.showEditBranchTable = true;
	}
	
	$scope.editBranchDetails = function(branchId, branchName, branchAddress){
		librarianService.updateLibraryService(branchId, branchName, branchAddress).then(function(data){
			$window.location.reload();
		})
	}
	
	$scope.searchBranches = function(){
		librarianService.serachBranchService($scope.searchString).then(function(data){
			console.log($scope.searchString);
			$scope.branches = data;
			console.log(data);
			$scope.showBranchTable = true;
			$scope.showBooksTable = false;
			$scope.showEditBranchTable = false;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.branches.length/$scope.pagination.perPage);
		})
	}
	
});




