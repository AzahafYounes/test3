var Notification=function(){
	var handleNotification = function () {
			var notif = $('#notif');
		
		$(document).ready(function() {
 // executes when HTML-Document is loaded and DOM is ready
		
					$.ajax({
        	        type : "POST",
        	        url : "/createNotifs",
        	        dataType : 'json',
        	        success : function(response) {
						 },
        	        error : function(request, status, error) {
						//alert("Erreur createNotifs");
					}
					
        	    });
							$.ajax({
						type : "POST",
						url : "/getNotifications",
						dataType : 'json',
						success : function(response) {
							if(response.length!=0)
								{
									for(var i=0; i<response.length;i++){
										if(response[i].notifcommercset==false)
										{
											if(response[i].type=="Observation")
												toastr.info(response[i].message,'Observation');
										
											if(response[i].type=="Rendez-vous")
												toastr.info(response[i].message,'Rendez-vous');
										
											if(response[i].type=="Echeance")
												toastr.info(response[i].message,'Echéance');
										}
										$.ajax({
											type : "POST",
											url : "setNotifFalse",
											data : {
												id : response[i].id_Notification,
											},
											dataType : 'json',
											success : function(response2) {},
										error : function(request, status, error) {
											
											}
										});
									}
								}
						},
						error : function(request, status, error) {
							
						}
					 });
        	       
				    $.ajax({
        	        type : "POST",
        	        url : "/getNotifSize",
        	        dataType : 'json',
        	        success : function(response) {
						document.getElementById("size").innerHTML = response;
        	        },
        	        error : function(request, status, error) {
						alert("Erreur");
					}
					
        	    });
        	
        });
	
	    $("#notif li").click(function(){			
			if ($(this).attr('rel')=="mod"){
			var id = $(this).find('a').attr('rel');
			var $this = this;
			  $(this).removeAttr("rel");
			  $(this).css('background-color',"#fff");
			$.ajax({
        	        type : "POST",
        	        data : {
        	        	id : id,
        	        },
					url : "/modNot_ok",
        	        dataType : 'json',
        	        success : function(response) {
						document.getElementById("size").innerHTML=parseInt(document.getElementById("size").innerHTML)-1;
					},
					error : function(request, status, error){
						//alert(error);	
					}
					
			})
			}
		});
		
}

  return {

        //main function to initiate the module
        init: function () {
            handleNotification();
        }

    };

}();


