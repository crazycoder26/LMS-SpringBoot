lmsApp.factory("librarianService", function($http, librarianConstants){
	return{
		initBranchService: function(){
			var getBranchData = {};
			return $http({
				url: librarianConstants.INIT_BRANCH_URL
			}).success(function(data){
				getBranchData = data;
			}).then(function(){
				return getBranchData;
			})
		},
		ListAllBranchService: function(){
			var branchData = {};
			return $http({
				url: librarianConstants.READ_BRANCHES_URL
			}).success(function(data){
				branchData = data;
			}).then(function(){
				return branchData;
			})
		},
		getAllbranchBooksService: function(branchId){
			var booksData = {};
			return $http({
				url: librarianConstants.READ_BRANCHBOOKS_URL + "/" + branchId
			}).success(function(data){
				booksData = data;
			}).then(function(data){
				return booksData;
			})
		},
		updateCopiesService: function(branchId, bookId, noCopies){
			var copiesData = {};
			return $http({
				url: librarianConstants.UPADTE_COPIES_URL +"/"+ branchId +"/"+ bookId +"/"+ noCopies
			}).success(function(data){
				copiesData = data;
			}).then(function(){
				return copiesData;
			})
		},
		updateLibraryService: function(branchId, name, address){
			var details = {};
			return $http({
				url: librarianConstants.UPDATE_LIBRARY_URL + "/" + branchId + "/" + name + "/" + address
			}).success(function(data){
				details = data;
			}).then(function(){
				return details;
			})
		},
		serachBranchService: function(name){
			var result = {};
			return $http({
				url: librarianConstants.SEARCH_BRANCHES_URL + "/" + name
			}).success(function(data){
				result = data;
			}).then(function(){
				return result;
			})
		}
		
	}

})