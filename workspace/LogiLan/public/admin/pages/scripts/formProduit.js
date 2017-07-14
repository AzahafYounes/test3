var FormValidation = function () {

    // basic validation
    var handleValidation1 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form1 = $('#form1');
			
            var error1 = $('#errors', form1);
            var success1 = $('.alert-success', form1);
			var loginIncorrect =$('#produitExistant', form1);

            form1.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                 rules: {
                    nom: {
                        
                        required: true
                    },
                    codeBarre: {
                        number: true
                    }
                },
				messages: {
	                nom: {
	                    required: "Veuillez saisir un nom."
	                },
	                codeBarre: {
	                    required: "Veuillez saisir un N° de tel."
	                }
				
				
				
	            },
				

                invalidHandler: function (event, validator) { //display error alert on form submit              
                    success1.hide();
                    error1.show();
                    Metronic.scrollTo(error1, -200);
                },

                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
                },

                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                },

                success: function (label) {
                    label
                        .closest('.form-group').removeClass('has-error'); 
				
                },

                submitHandler: function (form) {
            
					var nom= $("#nom").val();
					var codeBarre=$("#codeBarre").val();
					var forme = $("#forme").val();
					var prix = $("#prix").val();
					var fournisseur = $("#fournisseur").val();
					var familleTarif = $("#familleTarif").val();
					if($("#isNew").is(':checked')){
						var isNew = 1;
					}
					else{
						var isNew = 0;
					}
					
					
					
					
					
					
					
					$.ajax({
        	        type : "POST",
        	        data : {
						nom: nom,
        	        	codeBarre: codeBarre,
						forme: forme,
						prix: prix,
						fournisseur: fournisseur,
						familleTarif: familleTarif,
						isNew : isNew
        	        },
        	        url : "/addNewProduit",
        	        dataType : 'json',
        	        success : function(response) {
        	        	
						success1.show();
						error1.hide();
						loginIncorrect.hide();
						
					  document.location="/Produits/A";
        	        },
        	        error : function(request, status, error) {
        	        	
						success1.hide();
						loginIncorrect.show();
        	        }
					
					});	
								
                },
				
            });
			
    }

	    var handleValidation2 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form2 = $('#form2');
			
            var error2 = $('.alert-danger', form2);
            var success2 = $('.alert-success', form2);

            form2.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                 rules: {
                	 
					nom1: {
                    
                        required: true
                    },
					
                    codeBarre1: {
                        number: true
                        
                    }
                },
				messages: {
					nom1: {
	                    required: "Veuillez saisir un nom."
	                },
	                codeBarre1: {
	                    required: "Veuillez saisir un Numéro."
	                }
	            },
				

                invalidHandler: function (event, validator) { //display error alert on form submit              
                    success2.hide();
                    error2.show();
                    Metronic.scrollTo(error2, -200);
                },

                highlight: function (element) { // hightlight error inputs
                    $(element)
                        .closest('.form-group').addClass('has-error'); // set error class to the control group
                },

                unhighlight: function (element) { // revert the change done by hightlight
                    $(element)
                        .closest('.form-group').removeClass('has-error'); // set error class to the control group
                },

                success: function (label) {
                    label
                        .closest('.form-group').removeClass('has-error'); // set success class to the control group
				
                },

                submitHandler: function (form) {
					
					var id= $("#update").val();
					var nom= $("#nom1").val();
					var codeBarre=$("#codeBarre1").val();
					var forme = $("#forme1").val();
					var prix = $("#prix1").val();
					var fournisseur = $("#fournisseur1").val();
					var familleTarif = $("#familleTarif1").val();
					if($("#isNew").is(':checked')){
						var isNew = 1;
					}
					else{
						var isNew = 0;
					}
					
					
					$.ajax({
        	        type : "POST",
        	        data : {
						id: id,
						nom: nom,
        	        	codeBarre: codeBarre,
						forme: forme,
						prix: prix,
						fournisseur: fournisseur,
						familleTarif: familleTarif,
						isNew : isNew
						
        	        },
        	        url : "/updateProduit",
        	        dataType : 'json',
        	        success : function(response) {
						success2.show();
						error2.hide();
						
					  document.location="/Produits/A";
        	        },
        	        error : function(request, status, error) {
				
						success2.hide();
						error2.show();
        	        }
					
					});	
					
					
				
					
					
				
                },
				
				
				
            });


    }

    

    // advance validation
   

    var handleWysihtml5 = function() {
        if (!jQuery().wysihtml5) {
            
            return;
        }

        if ($('.wysihtml5').size() > 0) {
            $('.wysihtml5').wysihtml5({
                "stylesheets": ["../../assets/global/plugins/bootstrap-wysihtml5/wysiwyg-color.css"]
            });
        }
    }

    return {
        //main function to initiate the module
        init: function () {

          
            handleValidation1();
			handleValidation2();

        }

    };

}();


