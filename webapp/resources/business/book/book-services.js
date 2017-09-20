lmsApp.factory("bookService", function($http, bookConstants){
	return{
		initBookService: function(){
			var getBookData = {};
			return $http({
				url: bookConstants.INIT_BOOK_URL
			}).success(function(data){
				getBookData = data;
			}).then(function(){
				return getBookData;
			})
		},
//        saveBookService: function(book){
//			var saveBookData = {};
//			book = JSON.stringify(book);
//			return $http({
//				url: bookConstants.ADD_BOOK_URL,
//                method: "POST",
//                data: book
//			}).success(function(data){
//				saveBookData = data;
//			}).then(function(){
//				return saveBookData;
//			})
//		},
		saveBookService: function(title, a, g, p){
			var saveBookData = {};
			return $http({
				url: "http://localhost:8080/lms/saveBook/" + title + "/" + a + "/" + g + "/" + p
			}).success(function(data){
				saveBookData = data;
			}).then(function(){
				return saveBookData;
			})
		},
        getAllBooksService: function(){
			var getBooksData = {};
			return $http({
				url: bookConstants.GET_ALL_BOOKS_URL
			}).success(function(data){
				getBooksData = data;
			}).then(function(){
				return getBooksData;
			})
		},
		searchBooksService: function(bookName){
			var getBooksData = {};
			return $http({
				url: bookConstants.SERACH_BOOKS_URL +"/" +bookName
			}).success(function(data){
				getBooksData = data;
			}).then(function(){
				return getBooksData;
			})
		},
		getBookByPk: function(bookId){
			var getBookByPkData = {};
			return $http({
				url: bookConstants.GET_BOOK_BY_PK_URL+"/"+bookId
			}).success(function(data){
				getBookByPkData = data;
			}).then(function(){
				return getBookByPkData;
			})
		},
		getAllGenresService: function(){
			var genreData = {};
			return $http({
				url: bookConstants.READ_GENRES_URL
			}).success(function(data){
				genreData = data;
			}).then(function(){
				return genreData;
			})
		},
		initAuthorList: function(){
			var authorData = {};
			return $http({
				url: bookConstants.INIT_AUTHORLIST_URL
			}).success(function(data){
				authorData = data;
			}).then(function(){
				return authorData;
			})
		},
		initGenreList: function(){
			var genreData = {};
			return $http({
				url: bookConstants.INIT_GENRELIST_URL
			}).success(function(data){
				genreData = data;
			}).then(function(){
				return genreData;
			})
		},
		initGenreService: function(){
			var genreData = {};
			return $http({
				url: bookConstants.INIT_GENRE_URL
			}).success(function(data){
				genreData = data;
			}).then(function(){
				return genreData;
			})
		},
		deleteBookService: function(bookId){
			var bookData = {};
			return $http({
				url: bookConstants.DELETE_BOOK_URL + "/" + bookId
			}).success(function(data){
				bookData = data;
			}).then(function(){
				return bookData;
			})
		}
	}
})