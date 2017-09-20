/**
 * 
 */
lmsApp.controller("addBookController", function($scope, $window, $http, authorService, Pagination, bookService, publisherService){
	
	bookService.getAllBooksService().then(function(data){
		$scope.book = data;
	})
	
	authorService.getAllAuthorsService().then(function(data){
		$scope.authors = data;
		$scope.pagination = Pagination.getNew(10);
		$scope.pagination.numPages = Math.ceil($scope.authors.length/$scope.pagination.perPage);
		$scope.showAuthorTable = true;
		$scope.showPublisherTable = false;
	})
	
	$scope.searchAuthors = function(){
    	authorService.getAuthorsByName($scope.searchString).then(function(data){
    		$scope.authors = data;
    		console.log($scope.authors);
    		$scope.pagination = Pagination.getNew(10);
    		$scope.pagination.numPages = Math.ceil($scope.authors.length/$scope.pagination.perPage);
    		$scope.showAuthorTable = true;
    		$scope.showPublisherTable = false;
    	})
    }
	
	$scope.showPublishers = function(){
		publisherService.getAllPublishersService().then(function(data){
			$scope.publishers = data;
			$scope.pagination = Pagination.getNew(10);
			$scope.pagination.numPages = Math.ceil($scope.publishers.length/$scope.pagination.perPage);
			$scope.showAuthorTable = false;
			$scope.showPublisherTable = true;
			$scope.showGenresTable = false;
		})
	}
	
    $scope.searchPublishers = function(){
		publisherService.getPublishersByName($scope.searchString).then(function(data){
			$scope.publishers = data;
			$scope.pagination = Pagination.getNew(10);
    		$scope.pagination.numPages = Math.ceil($scope.publishers.length/$scope.pagination.perPage);
    		$scope.showAuthorTable = false;
			$scope.showPublisherTable = true;
			$scope.showGenresTable = false;
		})
		
    }
	
    $scope.showGenres = function(){
    	bookService.getAllGenresService().then(function(data){
    		$scope.genres = data;
    		$scope.pagination = Pagination.getNew(10);
    		$scope.pagination.numPages = Math.ceil($scope.publishers.length/$scope.pagination.perPage);
    		$scope.showAuthorTable = false;
			$scope.showPublisherTable = false;
			$scope.showGenresTable = true;
    	})
    }
    
    bookService.initAuthorList().then(function(data){
    	$scope.authorList = data;
    })
    
    bookService.initGenreList().then(function(data){
    	$scope.genreList = data;
    })
   
	authorService.initAuthorService().then(function(data){
    		$scope.author = data;
    	})
    
    var a = [];	
    $scope.addAuthor = function(authorId){
    	authorService.initAuthorService().then(function(data){
    		$scope.author = data;
    	})
    	a.push(authorId);
    	$scope.author.authorId = authorId;
    	console.log($scope.author);
    	$scope.authorList.push($scope.author);
    }
    
    bookService.initGenreService().then(function(data){
		$scope.genre = data;
	})
    
	var g = [];
    $scope.addGenre = function(genreId){
    	bookService.initGenreService().then(function(data){
    		$scope.genre = data;
    	})
    	$scope.genre.genreId = genreId;
    	g.push(genreId);
    	console.log($scope.genre);
    	$scope.genreList.push($scope.genre);
    }
    
    publisherService.initPublisherService().then(function(data){
		$scope.publisher = data;
		console.log($scope.publisher);
	})
    
    $scope.addPublisher = function(publisherId){
    	$scope.publisher.publisherId  = publisherId;
    	console.log($scope.publisher);
    }
	
	$scope.saveBook = function(){
		$scope.book.authors = $scope.authorList;
		$scope.book.genres = $scope.genreList;
		$scope.book.publisher = $scope.publisher;
		console.log($scope.book);
		bookService.saveBookService($scope.book.title, a, g, $scope.publisher.publisherId).then(function(data){
			$window.location.href = "#/viewbooks";
		})
	}
	
});

lmsApp.controller("listBooksController", function($scope, $window, $http, bookService, Pagination, $filter){
    
	bookService.getAllBooksService().then(function(data){
		$scope.books = data;
		$scope.pagination = Pagination.getNew(10);
		$scope.pagination.numPages = Math.ceil($scope.books.length/$scope.pagination.perPage);
		$scope.bookslist = true;
		$scope.editBook = false;
	})
	
	$scope.editBookModal = function(bookId){
    	bookService.getBookByPk(bookId).then(function(data){
    		$scope.book = data;
    		$scope.showEditBookModal = true;
    	})
    	
    }
	$scope.searchBooks = function(){
		bookService.searchBooksService($scope.searchString).then(function(data){
			$scope.books = data;
			$scope.pagination = Pagination.getNew(10);
    		$scope.pagination.numPages = Math.ceil($scope.books.length/$scope.pagination.perPage);
		})
	}
    
    $scope.sortBooks = function(){
    	$scope.books = $filter("orderBy")($scope.books, "title");
    }
    
    $scope.deleteBook = function(bookId){
    	bookService.deleteBookService(bookId).then(function(data){
    		$window.location.href = "#/book"
    	})
    }
    
    $scope.editBooks = function(){
    	$scope.editBook = true;
    	$scope.bookslist = false;
    	$scope.activeHome = false;
    }
    
});
