
lmsApp.config(["$routeProvider", function($routeProvider){
	return $routeProvider.when("/", {
		redirectTo: "/home"
	}).when("/home", {
		templateUrl: "welcome.html"
	}).when("/admin", {
		templateUrl: "admin.html"
	}).when("/librarian", {
		templateUrl: "librarian.html"
	}).when("/borrower", {
		templateUrl: "borrower.html"
	}).when("/viewauthors", {
		templateUrl: "viewauthors.html"
	}).when("/author", {
		templateUrl: "Authors2.html"
	}).when("/addauthor", {
		templateUrl: "addauthor.html"
	}).when("/book", {                   
		templateUrl: "book.html"
	}).when("/addbook", {
		templateUrl: "addbook.html"
	}).when("/viewbooks", {
		templateUrl: "viewbooks.html"
	}).when("/publisher", {
		templateUrl: "PublisherPage.html"
	}).when("/addpublisher", {
		templateUrl: "addpublisher.html"
	}).when("/viewpublisher", {
		templateUrl: "viewPublisher.html"
	}).when("/MainBorrower", {
		templateUrl: "MainBorrower.html"
	}).when("/checkin", {
		templateUrl: "checkin.html"
	}).when("/checkout", {
		templateUrl: "viewborrowerbranches.html"
	}).when("/librarian",{
		templateUrl: "librarian.html"
	}).when("/borrwer",{
		templateUrl: "BorrowerPage.html"
	}).when("/addborrower",{
		templateUrl: "addborrower.html"
	}).when("/viewborrowers",{
		templateUrl: "viewBorrower.html"
	}).when("/override",{
		templateUrl: "override.html"
	})
}])