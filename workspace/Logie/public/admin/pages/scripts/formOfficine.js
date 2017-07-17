var FormValidation = function () {

    var handleValidation1 = function() {

            var form1 = $('#form1');
			
            var error1 = $('#errors', form1);
            var success1 = $('.alert-success', form1);
			var loginIncorrect =$('#officineExistant', form1);

            form1.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                 rules: {
                    nomOfficine: {
                        minlength: 4,
                        required: true
                    }
                },
				messages: {
	                nomOfficine: {
	                    required: "nom de l'officine est obligatoire."
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
            
					var nomOfficine= $("#nomOfficine").val();
					var fix= $("#fix").val();
					var inpe= $("#inpe").val();
					var adress=$("#adress").val();
					var user = $("#user").val();
					var manager=0;
					
					
					
					
					$.ajax({
        	        type : "POST",
        	        data : {
						nomOfficine: nomOfficine,
        	        	adress: adress,
						user: user,
						manager: manager,
						fix : fix,
						inpe: inpe
        	        },
        	        url : "addNewOfficine",
        	        dataType : 'json',
        	        success : function(response) {
						success1.show();
						error1.hide();
						loginIncorrect.hide();
						
					  document.location="officines";
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
                    nomOfficine1: {
                        minlength: 4,
                        required: true
                    }
                },
				messages: {
	                nomOfficine1: {
	                    required: "nom de l'officine est obligatoire."
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
					
                	var nomOfficine= $("#nomOfficine1").val();
					var adress=$("#adress1").val();
					var user = $("#user1").val();
					var inpe= $("#inpe1").val();
					var fix= $("#fix1").val();
					
					var manager=0;
					
                	var id= $("#update").val();
					$.ajax({
        	        type : "POST",
        	        data : {
						id: id,
						nomOfficine: nomOfficine,
        	        	adress: adress,
						manager : manager,
						user: user,
						inpe: inpe,
						fix: fix
        	        },
        	        url : "updateOfficine",
        	        dataType : 'json',
        	        success : function(response) {
						success2.show();
						error2.hide();
						
					  document.location="officines";
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