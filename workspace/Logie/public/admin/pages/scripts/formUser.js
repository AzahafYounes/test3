var FormValidation = function () {

    // basic validation
    var handleValidation1 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form1 = $('#form1');
			
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
                    },
					mac: {
                        required:checkMac(mac)
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
        	        url : "addNewUser",
        	        dataType : 'json',
        	        success : function(response) {
						success1.show();
						error1.hide();
						loginIncorrect.hide();
						
					  document.location="allUsers";
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
                    login1: {
                        minlength: 4,
                        required: true
                    },
					password1: {
                        minlength: 4,
                        required: true
                    },
					cnpassword: {
                        equalTo: "#npassword"
                    },
					nom1: {
                    
                        required: true
                    },
					prenom1: {
                    
                        required: true
                    },
					
                    email1: {
                        required: true,
                        email: true
                    },
                    tel1: {
                        required: true,
                        number: true
                    },
					mac1: {
                        required:checkMac(mac)
                    }
                },
				messages: {
	                login1: {
	                    required: "Login est requis."
	                },
				password1: {
	                    required: "Mot de passe est requis."
	                },
				cnpassword: {
	                    equalTo: "les mots de passe ne sont pas identiques."
	                },
				nom1: {
	                    required: "Veuillez saisir un nom."
	                },
				prenom1: {
	                    required: "Veuillez saisir un prénom."
	                },
				email1: {
	                    required: "Veuillez saisir un Email valide."
	                },
				tel1: {
	                    required: "Veuillez saisir un N° de tel."
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
					var login= $("#login1").val();
					var pass=$("#password1").val();
					var npassword =$("#npassword").val();
					var nom = $("#nom1").val();
					var prenom = $("#prenom1").val();
					var email = $("#email1").val();
					var tel = $("#tel1").val();
					var role = $("#role1").val();
					var mac = $("#mac1").val();			
					
					$.ajax({
        	        type : "POST",
        	        data : {
						id: id,
						login: login,
        	        	pass: pass,
						npass : npassword,
						nom: nom,
						prenom: prenom,
						email: email,
						tel: tel,
						role: role,
						mac: mac
        	        },
        	        url : "updateUser",
        	        dataType : 'json',
        	        success : function(response) {
						success2.show();
						error2.hide();
						
					  document.location="allUsers";
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


function checkMac(cValue) {
		var bResult = false;
		if(cValue == "Commercial") { 
		this.getField("mac").required= true; 
		bResult = true;
		}
	return bResult;
}