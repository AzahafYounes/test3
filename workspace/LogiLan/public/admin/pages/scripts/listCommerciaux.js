var InputCommerc = function () {

    var handleInputs = function () {

		var inputs = $('#item_list');
		var tags = $("#tags_1");
		var name = $("#names");
		
		
		
		
        $(document).ready(function() {
 // executes when HTML-Document is loaded and DOM is ready
    
				    $.ajax({
        	        type : "POST",
        	        url : "getCommerciaux",
        	        dataType : 'json',
        	        success : function(response) {
								for(var i=0;i<response.length;i++)
								{
									$(inputs).append('<div class="item"><div class="item-head"><div class="item-details"><img class="item-pic" src="'+response[i].photo+'"><a id="names" rel="'+response[i].id+'"class="item-name primary-link">'+response[i].nom+' '+response[i].prenom+'</a></div></div></div>');
									console.log(response[i].id);
								}
        	        },
        	        error : function(request, status, error) {
						
					}
					
        	    });
        	
        });
		
		
	$("#observation").on('click', '#submit', function (e) {
		e.preventDefault();
		var tags= $("#tags_1").tagsinput('items');
		var idCommerc="";
		var objet= $('#objet').val();
		var message = $('#message').val();
		if(tags.length)
			for(var i=0;i<tags.length-1;i++)
			{idCommerc = idCommerc+tags[i]["id"]+",";
			}
			idCommerc = idCommerc+tags[i]["id"];
			if(idCommerc!=null){
				$("#msg").html("Envoi en cours, veuillez attendre ...");
				$("#ajax").modal("show");
				$.ajax({
						type : "POST",
						data:{
							idCommerc: idCommerc,
							objet: objet,
							message: message
							},
						url : "sendObservation",
						dataType : 'json',
						success : function(response) {
							$( '#observForm' ).each(function(){
								this.reset();
							});
							$("#tags_1").tagsinput('removeAll');
							$("#ajax").modal("hide");
							$("#msg1").html("Observation envoyée");
							$("#large").modal("show");
							setTimeout(function() {$("#large").modal("hide");}, 2000);
						},
						error : function(request, status, error) {
						$("#ajax").modal("hide");
        	        	$("#msg1").html("Erreur");
						$("#large").modal("show");
						setTimeout(function() {$("#large").modal("hide");}, 2000);
						}
				});
			}
  
		});
	
		
		
	inputs.on('click', '#names', function (e) {
            e.preventDefault();
			var fname= $(this).text();
			var fid = $(this).attr('rel');
			console.log(fid);
			console.log(fname);
	
	$('#tags_1').tagsinput('add', { id: fid, text: fname });
			
        });

    }

    return {

        //main function to initiate the module
        init: function () {
		handleInputs();			
				
        }

    };

}();

