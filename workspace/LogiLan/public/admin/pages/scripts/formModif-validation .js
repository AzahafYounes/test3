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
                messages: {
                    select_multi: {
                        maxlength: jQuery.validator.format("Max {0} items allowed for selection"),
                        minlength: jQuery.validator.format("At least {0} items must be selected")
                    }
                },
                rules: {
                    nom: {
                        required: true
                    },
					prenom: {
                        required: true
                    },
                    email: {
                        email: true
                    },
                    assure: {
                        required: true,
                    },
                    tel: {
						number:true,
						pattern:"/^\+?([0-9]{2})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{3})$/",
                    },
					fix:{
						number:true,
						pattern:"/^\+?([0-9]{2})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{3})$/",
					},
					
					fax:{
						number:true,
						pattern:"/^\+?([0-9]{2})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{3})$/",
					},
					
                   
                },
				messages: {
	                nom: {
	                    required: "veillez saisir le nom."
	                },

				prenom: {
	                    required: "veillez saisir le prenom."
	                },
				tel: {
	                    pattern:"verifier format tel"
	                },
				fix: {
	                    pattern:"verifier format fix"
	                },
				fax: {
	                    pattern:"verifier format fax"
	                },
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

    // validation using icons
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

            handleWysihtml5();
            handleValidation1();
           

        }

    };

}();