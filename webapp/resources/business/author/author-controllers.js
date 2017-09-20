lmsApp.controller("addAuthorController", function($scope, $window, $http, Pagination, authorService){
	
	authorService.initAuthorService().then(function(data){
		$scope.author = data;
	})
	
	authorService.getBooksService().then(function(data){
		$scope.books = data;
		$scope.pagination = Pagination.getNew(10);
		$scope.pagination.numPages = Math.ceil($scope.books.length/$scope.pagination.perPage);
	})
	
	authorService.initBookService().then(function(data){
		$scope.book = data;
	})

	
	authorService.initBookListService().then(function(data){
		$scope.bookList = data;
	})
	var vals = [];
	$scope.addBook = function(bookId){
		document.getElementById("myButton1").value = "selected";
		authorService.initBookService().then(function(data){
			$scope.book = data;
		})
		vals.push(bookId);
		$scope.book.bookId = bookId;
		console.log($scope.book);
		$scope.bookList.push($scope.book);
	}
	
	$scope.saveAuthor = function(){
		console.log($scope.author);
		$scope.author.books = $scope.bookList;
		authorService.saveAuthorService($scope.author).then(function(data){
			$window.location.href = "#/viewauthors";
		})
	}
	
	$scope.searchBooks = function(){
		authorService.searchBooksService($scope.searchString).then(function(data){
			$scope.books = data;
			$scope.pagination = Pagination.getNew(10);
    		$scope.pagination.numPages = Math.ceil($scope.books.length/$scope.pagination.perPage);
		})
	}
	
});



lmsApp.controller("listAuthorsController", function($scope, $window, $http, authorService, publisherService, Pagination, $filter){
    
	authorService.initAuthorService().then(function(data){
		$scope.author = data;
	})
	
	authorService.getAllAuthorsService().then(function(data){
		$scope.authors = data;
		$scope.pagination = Pagination.getNew(10);
		$scope.pagination.numPages = Math.ceil($scope.authors.length/$scope.pagination.perPage);
		$scope.showAuthorsTable = true;
    	$scope.showEditauthor = false;
	})
	
	
	
    $scope.searchAuthors = function(){
    	authorService.getAuthorsByName($scope.searchString).then(function(data){
    		if($scope.searchString == ""){
    			$window.location.href = "#/viewauthors";
    		}
    		$scope.authors = data;
    		$scope.pagination = Pagination.getNew(10);
    		$scope.pagination.numPages = Math.ceil($scope.authors.length/$scope.pagination.perPage);
    	})
    }
    
    $scope.sortAuthors = function(){
    	$scope.authors = $filter("orderBy")($scope.authors, "authorName");
    }
    
    $scope.deleteAuthor = function(authorId){
    	$scope.author.authorId = authorId;
    	authorService.deleteAuthorService($scope.author).then(function(data){
    		$window.location.reload();
    	})
    }
    
    $scope.editAuthor = function(){
    	$scope.showAuthorsTable = false;
    	$scope.showEditauthor = true;
    }
    
    var list = [];
    publisherService.initBookListService().then(function(data){
		$scope.bookList = data;
	})
    
    $scope.editAuthorDetails = function(id, name){
    	$scope.author.authorName = name;
    	$scope.author.authorId = id;
    	$scope.bookList = list;
    	$scope.author.books = $scope.bookList;
    	authorService.editAuthorService($scope.author).then(function(data){
    		$window.location.reload();
    	})
    }
    
});
