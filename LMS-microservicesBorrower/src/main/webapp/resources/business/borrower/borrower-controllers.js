lmsApp.controller("ListAllBorrowerBranches", function($scope, $window, $http, borrowerService, Pagination, librarianService){
	
	borrowerService.initBorrowerService().then(function(data){
		$scope.borrower = data;
	})
	
	borrowerService.initLoansService().then(function(data){
		$scope.loan = data;
	})
	
	$scope.validateCard = function(cardNo){
		borrowerService.validateCardService(cardNo).then(function(data){
			console.log(data);
			var val = data;
			if(val == true){
				$scope.showBranchTable = true;
				$scope.loan.cardNo = cardNo;
				$scope.notvalid = false;
			}else{
				$scope.notvalid = true;
			}
		})
	}
	
	
	
	borrowerService.ListAllBranchService().then(function(data){
		$scope.branches = data;
		$scope.pagination = Pagination.getNew(10);
		$scope.pagination.numPages = Math.ceil($scope.branches.length/$scope.pagination.perPage);
	})
	
	$scope.getAllbranchBooks = function(branchId){
		$scope.loan.branchId = branchId;
		
		borrowerService.getAllbranchBooksService(branchId).then(function(data){
			$scope.books = data;
			$scope.showBranchTable = false;
			$scope.showBooksTable = true;
		})
	}
	
	$scope.checkOutBook = function(bookId){
		$scope.loan.bookId = bookId;
		console.log($scope.loan);
		borrowerService.checkOutBookService($scope.loan).then(function(data){
			$window.location.href = "#/MainBorrower"
		})
	}
	
	$scope.searchBranches = function(){
		
		librarianService.serachBranchService($scope.searchString).then(function(data){
			console.log($scope.searchString);
			$scope.branches = data;
			$scope.showBranchTable = true;
			console.log(data);
			$scope.showBooksTable = false;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.branches.length/$scope.pagination.perPage);
		})
	}
	
});

lmsApp.controller("checkIn", function($scope, $window, $http, borrowerService, Pagination){
	
	borrowerService.initBorrowerService().then(function(data){
		$scope.borrower = data;
	})
	
	borrowerService.initLoansService().then(function(data){
		$scope.loan = data;
	})
	
	var cardno = 0; 
	$scope.validateCard = function(cardNo){
		cardno = cardNo;
		borrowerService.validateCardService(cardNo).then(function(data){
			console.log(data);
			var val = data;
			if(val == true){
				borrowerService.ListAllLoans(cardno).then(function(data){
					console.log(cardno);
					$scope.loans = data;
					console.log(data);
					$scope.pagination = Pagination.getNew(10);
					$scope.pagination.numPages = Math.ceil($scope.loans.length/$scope.pagination.perPage);
				})
				$scope.notvalid = false;
				$scope.showBranchTable = true;
			}else{
				$scope.notvalid = true;
			}
		})
	}
	
	$scope.checkIn = function(bookId, branchId, cardNo){
		$scope.loan.bookId = bookId;
		$scope.loan.branchId = branchId;
		$scope.loan.cardNo = cardNo;
		console.log($scope.loan);
		borrowerService.checkInBookService($scope.loan).then(function(data){
			$window.location.href = "#/MainBorrower"
		})
	}
});


lmsApp.controller("viewBorrowerController", function($scope, $window, $http, borrowerService, Pagination){
	
	borrowerService.initBorrowerService().then(function(data){
		$scope.borrower = data;
	})
	
	borrowerService.getAllBorrowerService().then(function(data){
		$scope.borrowers = data;
		$scope.pagination = Pagination.getNew(10);
		$scope.pagination.numPages = Math.ceil($scope.borrowers.length/$scope.pagination.perPage);
		$scope.borrowerTable = true;
		$scope.showEditBorrowers = false;
	})
	
	$scope.deleteBorrower = function(cardNo){
		$scope.borrower.cardNo = cardNo;
		borrowerService.deleteCardService($scope.borrower).then(function(data){
			$window.location.reload();
		})
	}
	
//	$scope.searchBooks = function(){
//		bookService.searchBooksService($scope.searchString).then(function(data){
//			$scope.books = data;
//			$scope.pagination = Pagination.getNew(10);
//    		$scope.pagination.numPages = Math.ceil($scope.books.length/$scope.pagination.perPage);
//		})
//	}
	
	$scope.searchBorrowers = function(){
		borrowerService.searchBorrowerService($scope.searchString).then(function(data){
			$scope.borrowers = data;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.borrowers.length/$scope.pagination.perPage);
			$scope.borrowerTable = true;
			$scope.showEditBorrowers = false;
		})
	}
	
	$scope.editBorrower = function(){
		$scope.borrowerTable = false;
		$scope.showEditBorrowers = true;
	}
	
	$scope.editBorrowerDetails = function(cardNo, name, address, phone){
		$scope.borrower.cardNo = cardNo;
		$scope.borrower.name = name;
		$scope.borrower.address = address;
		$scope.borrower.phone = phone;
		borrowerService.editBorrowerService($scope.borrower).then(function(data){
			$window.location.reload();
		})
	}
	
});


lmsApp.controller("addBorrowerController", function($scope, $window, $http, borrowerService){
	
	borrowerService.initBorrowerService().then(function(data){
		$scope.borrower = data;
	})
	
	$scope.addBorrower = function(){
		borrowerService.addBorrowerService($scope.borrower).then(function(data){
			$window.location.href = "#/viewborrowers"
		})
	}
	
});
