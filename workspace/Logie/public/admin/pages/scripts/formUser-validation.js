var FormValidation = function () {

    // basic validation
    var handleValidation1 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form1 = $('#form_sample_1');
            var error1 = $('.alert-danger', form1);
            var success1 = $('.alert-success', form1);

            form1.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                rules: {
                    Login: {
                        minlength: 4,
                        required: true
                    },
					MotDePasse: {
                        minlength: 4,
                        required: true
                    },
					cMotDePasse: {
                        minlength: 4,
                        equalTo: '#MotDePasse'
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
                    Tel: {
                        required: true,
                        number: true
                    }
                },
				messages: {
	                Login: {
	                    required: "Login est requis."
	                },
				MotDePasse: {
	                    required: "Mot de passe est requis."
	                },
				cMotDePasse: {
	                    required: "Veuillez saisir la confirmation du mot de passe."
					
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
				Tel: {
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
            
					success1.show();
                    error1.hide();
				
                }
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