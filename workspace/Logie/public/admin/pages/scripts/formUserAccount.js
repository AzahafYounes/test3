var FormValidation = function () {

    // basic validation
    var handleValidation1 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form1 = $('#form1');
	
			
            var error1 = $('.alert-danger', form1);
            var success1 = $('.alert-success', form1);

            form1.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                rules: {
                    identifiant: {
                        minlength: 4,
                        required: true
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
                },
				messages: {
	                identifiant: {
	                    required: "Login est requis."
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
            
				var identifiant= $("#identifiant").val();
					var nom = $("#nom").val();
					var prenom = $("#prenom").val();
					var email = $("#email").val();
					var tel = $("#tel").val();
					console.log(identifiant);
					console.log(nom);
					console.log(prenom);
					console.log(email);
					console.log(tel);
					$.ajax({
        	        type : "POST",
        	        data : {
						identifiant: identifiant,
        	        	nom: nom,
						prenom: prenom,
						email: email,
						tel: tel
        	        },
        	        url : "/updateInfos",
        	        dataType : 'json',
        	        success : function(response) {
						success1.show();
						error1.hide();
					
        	        },
        	        error : function(request, status, error) {
						
						success1.hide();
						error1.show();
					}
					
        	    });	

				 	
					
                },
				
				
				
            });


    }
	
    
    
    
    
    
    
	var handleValidation2 = function(){
		
		var form2 = $('#form2');
		var error2 = $('.alert-danger', form2);
        var success2 = $('.alert-success', form2);
		
				form2.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                rules: {
                    password: {
                        minlength: 4,
                        required: true
                    },
					newpassword: {
						minlength: 4,
                        required: true
                    },
					cnewpassword: {
                        required: true,
						equalTo: "#newpassword"
                    },
				
                },
				messages: {
	                password: {
	                    required: "Mot de passe actuel est requis."
	                },

				newpassword: {
	                    required: "Veillez saisir un nouveau mot de passe."
	                },
				cnewpassword: {
	                    equalTo: "Les mots de passes ne sont pas identiques."
	                },
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
            
				 	
					var password = $("#password").val();
					var newpassword = $("#newpassword").val();
					$.ajax({
        	        type : "POST",
        	        data : {
						password: password,
						newpassword: newpassword
        	        },
        	        url : "/updatePass",
        	        dataType : 'json',
        	        success : function(response) {
					success2.show();
					error2.hide();
        	        },
        	        error : function(request, status, error) {
						success2.hide();
						error2.show();
						alert("Le mot de passe saisi est incorrect");
        	        }
					
        	    });	
					
                },
				
            });

	}
	
	
	
	
	var handleValidation4 = function() {
        // for more info visit the official plugin documentation: 
            // http://docs.jquery.com/Plugins/Validation

            var form4 = $('#form4');
	
			
            var error4 = $('.alert-danger', form4);
            var success4 = $('.alert-success', form4);

            form4.validate({
                errorElement: 'span', //default input error message container
                errorClass: 'help-block help-block-error', // default input error message class
                focusInvalid: false, // do not focus the last invalid input
                ignore: "",  // validate all fields including form hidden input
                rules: {
                    nomOfficine: {
                        required: true
                    },
					inpe: {
                    
                        required: true
                    },
                },
				messages: {
	                nomOfficine: {
	                    required: "nom de l'officne est requis."
	                },

				inpe: {
	                    required: "Veuillez saisir votre inpe."
	                },
				 },
                invalidHandler: function (event, validator) { //display error alert on form submit              
                    success4.hide();
                    error4.show();
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
					var inpe = $("#inpe").val();
					var fix = $("#fix").val();
					
					$.ajax({
        	        type : "POST",
        	        data : {
						nomOfficine: nomOfficine,
        	        	inpe: inpe,
						fix: fix
        	        },
        	        url : "/updateOfficine1",
        	        dataType : 'json',
        	        success : function(response) {
						success4.show();
						error4.hide();
					
        	        },
        	        error : function(request, status, error) {
						
						success4.hide();
						error4.show();
					}
					
        	    });	

				 	
					
                },
				
				
				
            });


    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	

   $('#photosrcmod').click(function updatephoto() {
 	var src=$("#photosrcmod").attr('src');
	$("#fotoProfil").attr('src',src);
    var data= new FormData();
    data.append( 'picture', $('#upload').get(0).files[0] );
       $("#msg").html("&nbsp;&nbsp; Editing Avatar ... ");
  	 $("#ajax").modal("show");

    $.ajax({
        url: '/updateProfilePicture',
         data: data,
        processData: false,
        type: 'POST',
		 mimeType:"multipart/form-data",
            contentType: false,
            cache: false,
            processData:false,  
        success: function ( response ) {    	
        	document.location="/accountSettings";
        },
		 error : function(request, status, error) {
			 $("#msg1").html("<b>Error</b>: "+error);
				$("#large").modal("show");
		}
    });

});
	

    return {
        //main function to initiate the module
        init: function () {

            handleValidation1();
			handleValidation2();
			handleValidation4();
			
			

        }

    };

}();

