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
                    officine: {
                        required: true
                    }
                },
				messages: {
	                officine: {
	                    required: "Officine est requis."
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
            
					var officine= $("#officine").val();
					
					$.ajax({
        	        type : "POST",
        	        data : {
        	        	officine: officine
        	        },
        	        url : "addOfficineToGroup_ok1",
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


