lmsApp.controller("addPublisherController", function($scope, $window, $http, publisherService, Pagination, $filter, bookService){

	publisherService.initPublisherService().then(function(data){
		$scope.publisher = data;
		console.log($scope.publisher);
	})
	
	
	
	publisherService.initBookService().then(function(data){
		$scope.book = data;
	})
	
	publisherService.getAllBooksService().then(function(data){
		$scope.books = data;
		$scope.pagination = Pagination.getNew(10);
		$scope.pagination.numPages = Math.ceil($scope.books.length/$scope.pagination.perPage);
	})
	
	$scope.searchBooks = function(){
		bookService.searchBooksService($scope.searchString).then(function(data){
			$scope.books = data;
			$scope.pagination = Pagination.getNew(10);
    		$scope.pagination.numPages = Math.ceil($scope.books.length/$scope.pagination.perPage);
		})
	}
	
	publisherService.initBookListService().then(function(data){
		$scope.bookList = data;
	})
	
	var vals = [];
	$scope.addBook = function(bookId){
		document.getElementById("myButton1").value="Selected";
		publisherService.initBookService().then(function(data){
			$scope.book = data;
		})
		vals.push(bookId);
		$scope.book.bookId = bookId;
		console.log($scope.book);
		$scope.bookList.push($scope.book);
	}
	console.log(vals);
	
	
	
	$scope.addPublisher = function(){
		$scope.publisher.books = $scope.bookList;
		console.log($scope.publisher);
		publisherService.addPublisherService($scope.publisher).then(function(data){
			$window.location.href = "#/viewpublisher"
		})
	}
	
	console.log($scope.publisher);
	
	$scope.searchPublishers = function(){
		publisherService.getPublishersByName($scope.searchString).then(function(data){
			$scope.publishers = data;
			$scope.pagination = Pagination.getNew(10);
    		$scope.pagination.numPages = Math.ceil($scope.publishers.length/$scope.pagination.perPage);
		})
		
    }
	
})


lmsApp.controller("listPublishersController", function($scope, $window, $http, publisherService, Pagination, $filter){

	
	publisherService.initPublisherService().then(function(data){
		$scope.publisher = data;
		console.log($scope.publisher);
	})


	publisherService.getAllPublishersService().then(function(data){
		$scope.publishers = data;
		$scope.pagination = Pagination.getNew(10);
		$scope.pagination.numPages = Math.ceil($scope.publishers.length/$scope.pagination.perPage);
		$scope.showPublisher = true;
    	$scope.showEditPublisher = false;
	})
	
	
	
    $scope.searchPublishers = function(){
		publisherService.getPublishersByName($scope.searchString).then(function(data){
			$scope.publishers = data;
			$scope.showPublisher = true;
        	$scope.showEditPublisher = false;
			$scope.pagination = Pagination.getNew(10);
    		$scope.pagination.numPages = Math.ceil($scope.publishers.length/$scope.pagination.perPage);
		})
    }
    
    $scope.sortPublishers = function(){
    	$scope.publishers = $filter("orderBy")($scope.publishers, "publisherName");
    }
    
    $scope.sortPublishersA = function(){
    	$scope.publishers = $filter("orderBy")($scope.publishers, "publisherAddress");
    }
    
    $scope.deletePublisher = function(publisherId){
    	$scope.publisher.publisherId = publisherId
    	publisherService.deletePublisherService($scope.publisher).then(function(){
    		$window.location.href = "#publisher"
    	})
    }
    
    $scope.editPublisher = function(){
    	$scope.showPublisher = false;
    	$scope.showEditPublisher = true;
    }
    
    $scope.editPublisherDetails = function(id, name, add, phone){
    	$scope.publisher.publisherName = name;
    	$scope.publisher.publisherId = id;
    	$scope.publisher.publisherAddress = add;
    	$scope.publisher.publisherPhone = phone;
    	publisherService.updatePublisherService($scope.publisher).then(function(){
    		$window.location.reload();
    	})
    }
    
});
