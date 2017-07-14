var oTable;
var asInitVals = new Array();

$(document).ready(function() {
	
	$("tbody .search input").keyup( function () {
		/* Filter on the column (the index) of this element */ 
		oTable.fnFilter( this.value, $("tbody .search input").index(this) );
	} );
	
	
	
	/*
	 * Support functions to provide a little bit of 'user friendlyness' to the textboxes in 
	 * the footer
	 */
	$("tbody .search input").each( function (i) {
		asInitVals[i] = this.value;
	} );
	
	$("tbody .search input").focus( function () {
		if ( this.className == "search_init" )
		{
			this.className = "";
			this.value = "";
		}
	} );
	
	$("tbody .search input").blur( function (i) {
		if ( this.value == "" )
		{
			this.className = "search_init";
			this.value = asInitVals[$("tbody .search input").index(this)];
		}
	} );
} );