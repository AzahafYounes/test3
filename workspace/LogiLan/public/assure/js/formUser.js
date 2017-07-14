var FormValidationn = function () {
 
    
    var handleValidation1 = function() {
        
            var form1 = $('#formm');
			
            var error1 = $('#errors', form1);
            var success1 = $('.alert-success', form1);
			var loginIncorrect =$('#userExistant', form1);

            form1.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                 rules: {
                    login: {
                        minlength: 4,
                        required: true
                    },
					password: {
                        minlength: 4,
                        required: true
                    },
					cpassword: {
                        minlength: 4,
                        equalTo: "#password"
                    },
					nom: {
                        required: true
                    },
					prenom: {
                        required: true
                    },
					
                    email: {
                        required: true,
                        email: true
                    },
                    tel: {
                        required: true,
                        number: true
                    }
                },
				messages: {
	                login: {
	                    required: "Login est requis."
	                },
				password: {
	                    required: "Mot de passe est requis."
	                },
				cpassword: {
	                    equalTo: "les mots de passe ne sont pas identiques."
	                },
				nom: {
	                    required: "Veuillez saisir un nom."
	                },
				prenom: {
	                    required: "Veuillez saisir un prénom."
	                },
				email: {
	                    required: "Veuillez saisir un Email valide."
	                },
				tel: {
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
                        .closest('.form-group').removeClass('has-error'); // set success class to the control group
				
                },

                submitHandler: function (form) {
            
					var login= $("#login").val();
					var pass=$("#password").val();
					var nom = $("#nom").val();
					var prenom = $("#prenom").val();
					var email = $("#email").val();
					var tel = $("#tel").val();
					var role = $("#role").val();
					var mac = $("#mac").val();			
					
					
					
					$.ajax({
        	        type : "POST",
        	        data : {
						login: login,
        	        	pass: pass,
						nom: nom,
						prenom: prenom,
						email: email,
						tel: tel,
						role: role,
						mac: mac
        	        },
        	        url : "/addNewUser",
        	        dataType : 'json',
        	        success : function(response) {
        	        	alert("je suis la");
						success1.show();
						error1.hide();
						loginIncorrect.hide();
						
					  document.location="authentification";
        	        },
        	        error : function(request, status, error) {
        	        	//alert("je erreur");
        	        	success1.show();
						error1.hide();
						loginIncorrect.hide();
						  document.location="authentification";

						//success1.hide();
						//loginIncorrect.show();
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
			 
        }

    };

}();


