lmsApp.factory("authorService", function($http, authorConstants){
	return{
		initAuthorService: function(){
			var getAuthorData = {};
			return $http({
				url: authorConstants.INIT_AUTHOR_URL
			}).success(function(data){
				getAuthorData = data;
			}).then(function(){
				return getAuthorData;
			})
		},
		saveAuthorService: function(author){
			console.log(author);
			var saveAuthorData = {};
			return $http({
				url: authorConstants.SAVE_AUTHOR_URL,
                method: "POST",
                data: author
			}).success(function(data){
				saveAuthorData = data;
			}).then(function(){
				return saveAuthorData;
			})
		},
		editAuthorService: function(author){
			var authorData = {};
			return $http({
				url: authorConstants.UPDATE_AUTHOR_URL + "/" + author.authorId,
				method: "PUT",
                data: author
			}).success(function(data){
				authorData = data;
			}).then(function(){
				return authorData;
			})
		},
		initBookService: function(){
			var getBookData = {};
			return $http({
				url: authorConstants.INIT_BOOK_URL
			}).success(function(data){
				getBookData = data;
			}).then(function(){
				return getBookData;
			})
		},
		initBookListService: function(){
			var listdata = {};
			return $http({
				url: authorConstants.GET_BOOKLIST_URL
			}).success(function(data){
				listdata = data;
			}).then(function(){
				return listdata;
			})
		},
		getBooksService: function(){
			var bookData = {};
			return $http({
				url: authorConstants.GET_ALL_BOOKS_URL
			}).success(function(data){
				bookData = data;
			}).then(function(data){
				return bookData;
			})
		},
        getAllAuthorsService: function(){
			var getAuthorsData = {};
			return $http({
				url: authorConstants.GET_ALL_AUTHORS_URL
			}).success(function(data){
				getAuthorsData = data;
			}).then(function(){
				return getAuthorsData;
			})
		},
		getAuthorsByName: function(authorName){
			console.log(authorName);
			var getAuthorsData = {};
			return $http({
				url: authorConstants.SEARCH_AUTHOR_BY_NAME +"/"+authorName
			}).success(function(data){
				getAuthorsData = data;
			}).then(function(){
				return getAuthorsData;
			})
		},
		getAuthorByPk: function(authorId){
			var getAuthorByPkData = {};
			return $http({
				url: authorConstants.GET_AUTHOR_BY_PK_URL+"/"+authorId
			}).success(function(data){
				getAuthorByPkData = data;
			}).then(function(){
				return getAuthorByPkData;
			})
		},
		searchBooksService: function(bookName){
			var booksData = {};
			return $http({
				url: authorConstants.SERACH_BOOKS_URL + "/" + bookName
			}).success(function(data){
				booksData = data;
			}).then(function(){
				return booksData;
			})
		},
		deleteAuthorService: function(author){
			var authorData = {};
			return $http({
				url: authorConstants.UPDATE_AUTHOR_URL + "/" + author.authorId,
				method: "PUT",
				data: author
			}).success(function(data){
				authorData = data;
			}).then(function(){
				return authorData;
			})
		}
	}
})