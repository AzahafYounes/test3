var TableEditable = function () {

    var handleTable = function () {

		var table = $('#sample_editable_1');
		var deleteRows= $('#deleteRows');
		
        
		function restoreRow(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);
			var id = $(jqTds[0]).find('input[type=checkbox]').val();
			oTable.fnUpdate('<input type="checkbox" class="checkboxes" rel="" value="'+id+'">', nRow, 0, false);
            for (var i = 1, iLen = jqTds.length; i < iLen; i++) {
                oTable.fnUpdate(aData[i], nRow, i, false);
            }

            oTable.fnDraw();
        }

        
		function editRow(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
			var jqTds = $('>td', nRow);
			var id = $(jqTds[0]).find('input[type=checkbox]').val();
			jqTds[0].innerHTML = '<input type="checkbox" class="checkboxes" value="'+id+'">';
			jqTds[1].innerHTML = '<input id="nomOfficine" type="text" class="form-control input" value="'+ aData[1] +'">';
            jqTds[2].innerHTML = '<input id="adress" type="text" class="form-control input" value="'+ aData[2] +'">';
			jqTds[3].innerHTML = '<input id="user" type="text" class="form-control input" value="'+ aData[3] +'">';
            
            jqTds[4].innerHTML = '<a class="edit" rel="saveEdit"><img src="../../assets/img/edit.png" title="Mettre à jour"></a> &nbsp; <a class="delete" rel="'+id+'"><img src="../../assets/img/delete.png" title="Supprimer"></a>';
           
        }
		
		
		function addRow(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);
			jqTds[0].innerHTML = '<input id="checkbox" type="checkbox" class="checkboxes" value="'+aData[0]+'">';
            jqTds[1].innerHTML = '<input id="nomOfficine" type="text" class="form-control input" value="' + aData[1] + '">';
            jqTds[2].innerHTML = '<input id="adress" type="text" class="form-control input" value="' + aData[2] + '">';
			jqTds[3].innerHTML = '<input id="user" type="text" class="form-control input" value="'+ aData[3] +'">';
            
            jqTds[4].innerHTML = '<a class="save" rel="save"><img src="../../assets/img/save.png" title="Enregistrer"></a> &nbsp; <a class="cancel" href=""><img src="../../assets/img/cancel.png" title="Annuler"></a>';
           
        }
		
        
		function saveRow(oTable, nRow) {
				var nomOfficine=$("#nomOfficine").val();
				var adress=$("#adress").val();
				var user=$("#user").val();
				$("#msg").html("enregistrement en cours veuillez attendre ...");
				$("#ajax").modal("show");
				setTimeout(function() {$("#large").modal("show").delay(2000).fadeOut();}, 1000);
        	    $.ajax({
        	        type : "POST",
        	        data : {
						nomOfficine:nomOfficine,
        	        	adress:adress,
						user:user,
						
        	        },
        	        url : "/addcont_ok",
        	        dataType : 'json',
        	        success : function(response) {
        	        	$("#ajax").modal("hide");
        	        	$("#msg1").html("Enregistrement réussi");
						$("#large").modal("show");
						setTimeout(function() {$("#large").modal("hide");}, 2000);
        	            var jqInputs = $('input', nRow);
						oTable.fnUpdate('<input type="checkbox" class="checkboxes" value="'+response.id+'">', nRow, 0, false);
        	            oTable.fnUpdate(nomOfficine, nRow, 1, false);
        	            oTable.fnUpdate(adress, nRow, 2, false);
						oTable.fnUpdate(user, nRow, 3, false);
        	            oTable.fnUpdate('<a class="edit" href=""><img src="../../assets/img/edit.png" title="Modifier"></a> &nbsp;&nbsp; <a class="delete" href="" rel="'+ response.id +'"><img src="../../assets/img/delete.png" title="Supprimer"></a>', nRow, 4, false);
        	            oTable.fnDraw();
        	        },
        	        error : function(request, status, error) {
						
        	        }
					
        	    });
        }
		
		
		
		function updateRow(oTable, nRow) {
        	  //  $("#content").html('');
				var jqTds = $('>td', nRow);
				var id = $(jqTds[0]).find('input[type=checkbox]').val();
				var nomOfficine=$("#nomOfficine").val();
				var adress=$("#adress").val();
				var user=$("#user").val();
				$("#msg").html("Mise à jour en cours veuillez attendre ...");
				$("#ajax").modal("show");
				setTimeout(function() {$("#large").modal("show").delay(2000).fadeOut();}, 1000);
        	    $.ajax({
        	        type : "POST",
        	        data : {
        	        	id: id,
						nom:nom,
        	        	prenom:prenom,
						email:email,
						tel:tel,
        	        },
        	        url : "/modcont_ok",
        	        dataType : 'json',
        	        success : function(response) {
        	        	$("#ajax").modal("hide");
        	        	$("#msg1").html("Mise à jour réussie");
						$("#large").modal("show");
						setTimeout(function() {$("#large").modal("hide");}, 2000);
        				//console.log(oTable);
        	            var jqInputs = $('input', nRow);
        	            oTable.fnUpdate('<input type="checkbox" class="checkboxes" value="'+id+'">', nRow, 0, false);
						oTable.fnUpdate(nom, nRow, 1, false);
        	            oTable.fnUpdate(prenom, nRow, 2, false);
						oTable.fnUpdate(email, nRow, 3, false);
        	            oTable.fnUpdate(tel, nRow, 4, false);
        	            oTable.fnUpdate('<a class="edit" href=""><img src="../../assets/img/edit.png" title="Modifier"></a> &nbsp;&nbsp; <a class="delete" href="" rel="'+ id +'"><img src="../../assets/img/delete.png" title="Supprimer"></a>', nRow, 5, false);
        	            oTable.fnDraw();
        	        },
        	        error : function(request, status, error) {
						
        	        }
					
        	    });
        }
			   
/*
        function cancelEditRow(oTable, nRow) {
            var jqInputs = $('input', nRow);
			oTable.row().data(jqInputs[0].value, nRow, 0, false);
            oTable.row().data(jqInputs[1].value, nRow, 1, false);
            oTable.row().data(jqInputs[2].value, nRow, 2, false);
            oTable.row().data('<a class="edit" href=""><img src="../../assets/img/edit.png"/></a>', nRow, 3, false);
            oTable.fnDraw();
        }		
		
*/	

        var oTable = table.dataTable({

            // Uncomment below line("dom" parameter) to fix the dropdown overflow issue in the datatable cells. The default datatable layout
            // setup uses scrollable div(table-scrollable) with overflow:auto to enable vertical scroll(see: assets/global/plugins/datatables/plugins/bootstrap/dataTables.bootstrap.js). 
            // So when dropdowns used the scrollable div should be removed. 
            //"dom": "<'row'<'col-md-6 col-sm-12'l><'col-md-6 col-sm-12'f>r>t<'row'<'col-md-5 col-sm-12'i><'col-md-7 col-sm-12'p>>",

            "lengthMenu": [
                [5, 15, 20, -1],
                [5, 15, 20, "All"] // change per page values here
            ],

            // Or you can use remote translation file
            //"language": {
            //   url: '//cdn.datatables.net/plug-ins/3cfcc339e89/i18n/Portuguese.json'
            //},

            // set the initial value
            "pageLength": 5,

            "language": {
                "lengthMenu": " _MENU_ enregistrements"
            },
            "columnDefs": [{ // set default column settings
                'orderable': true,
                'targets': [0]
            }, {
                "searchable": true,
                "targets": [0]
            }],
            "order": [
                [0, "asc"]
            ] // set first column as a default sort by asc
			
        });
			    var tableWrapper = $("#sample_editable_1_wrapper");

       /*tableWrapper.find(".dataTables_length select").select2({
            showSearchInput: false //hide search box with special css class
        }); // initialize select2 dropdown*/

		
			
			function isBlank(str) {
    return (!str || /^\s*$/.test(str));
	}
		
	$('#sample_editable_1 #2 th').each(function(){
		var title = $('#sample_editable_1 #1 th').eq($(this).index()).text();
		if(title.indexOf("Actions") ==-1 && !isBlank(title))
				$(this).html('<input type="text" class="form-control input" placeholder="rechercher '+title+'">');
	});
	
		oTable.api().columns().every(function () {
		var datatableColumn = this;
		var searchTextBoxes = $(this.header()).find('input');
		searchTextBoxes.on('keyup change', function(){
			datatableColumn.search(this.value).draw();
			
			var nNew = false;
		});
		searchTextBoxes.on('click', function(e){
			e.stopPropagation();
		});
		
	});


    
        var nEditing = null;
        var nNew = false;

		table.find('.group-checkable').change(function () {
            var set = jQuery(this).attr("data-set");
            var checked = jQuery(this).is(":checked");
            jQuery(set).each(function () {
                if (checked) {
                    $(this).attr("checked", true);
                    $(this).parents('tr').addClass("active");
                } else {
                    $(this).attr("checked", false);
                    $(this).parents('tr').removeClass("active");
                }
            });
            jQuery.uniform.update(set);
        });
		
        $('#sample_editable_1_new').click(function (e) {
            e.preventDefault();

            if (nNew && nEditing) {
                if (confirm("Ligne précédente non engegistée. Enregistrer maintenant ?")) {
					if($(this).attr('rel')=="saveEdit"){
					updateRow(oTable, nEditing); // update
                    $(nEditing).find("td:first").html("Untitled");
                    nEditing = null;
                    nNew = false;
					} else {
					saveRow(oTable, nEditing); // update
                    $(nEditing).find("td:first").html("Untitled");
                    nEditing = null;
                    nNew = false;
					}
					
                } else {
                    oTable.fnDeleteRow(nEditing); // cancel
                    nEditing = null;
                    nNew = false;
                    
                    return;
				}}
        
				var aiNew = oTable.fnAddData(['', '', '', '','','']);
				var nRow = oTable.fnGetNodes(aiNew[0]);
				addRow(oTable, nRow);
				nEditing = nRow;
				nNew = true;
        });
		
	
         table.on('click', '.delete', function (e) {
            e.preventDefault();
			 //console.log(code);
            if (confirm("Etes-vous sur de vouloir supprimer la ligne ?") == false) {
                return;
				} 
				var nRow = $(this).parents('tr');
				$("#msg").html("Suppression en cours veuillez attendre ...");
				$("#ajax").modal("show");
				var id = $(this).attr('rel');
				console.log(id);
				$.ajax({
        	        type : "POST",
        	        data : {
        	        	id : id,
        	        },
					url : "/supCont_ok",
        	        dataType : 'json',
        	        success : function(response) {
					
					oTable.fnDeleteRow(nRow);
					
					$("#ajax").modal("hide");
        	        $("#msg1").html("Ligne supprimée");
					$("#large").modal("show");
					setTimeout(function() {$("#large").modal("hide");}, 2000);
					},
					error : function(request, status, error){
							
					}
					
					})
		});
	
		function deleteRow(v, nRow)
		{
	
				$.ajax({
        	        type : "POST",
        	        data : {
        	        	id : v,
        	        },
					url : "/supCont_ok",
        	        dataType : 'json',
        	        success : function(response) {
					oTable.fnDeleteRow(nRow);
					},
					error : function(request, status, error){}
					
					})
		
		}
		
		
		$('#deleteRows').on("click", function(event){
		var sel = false;
		var ch = $('tr').find('td input[type=checkbox]');
		var c = confirm('Confirmer suppression?');
		if(c){
			
				$("#msg").html("Suppression en cours veuillez attendre ...");
				$("#ajax").modal("show");
				setTimeout(function() {$("#large").modal("show").delay(2000).fadeOut();}, 1000);
				ch.each(function(){
				var $this = $(this);
				if($this.is(':checked')) {
					sel = true; //set to true if there is/are selected row
					var id = $this.val();
					console.log(id);
					deleteRow(id,$this.parents('tr'));
				}
      });

          if(!sel) {
			  
							$("#msg1").html("Aucune ligne sélectionnée");
							$("#ajax").modal("hide");
							$("#large").modal("show");
							setTimeout(function() {$("#large").modal("hide");}, 2000);
							return false;
		  }
							$("#msg1").html("Supression terminée");
							$("#ajax").modal("hide");
							$("#large").modal("show");
							setTimeout(function() {$("#large").modal("hide");}, 2000);
		}
	});

		
		table.on('change', 'tbody tr .checkboxes', function () {
            $(this).parents('tr').toggleClass("active");
		});

      table.on('click', '.cancel', function (e) {
            e.preventDefault();
            if (nNew) {
                oTable.fnDeleteRow(nEditing);
                nEditing = null;
                nNew = false;
            } else {
                restoreRow(oTable, nEditing);
                nEditing = null;
            }
        });


         table.on('click', '.edit', function (e) {
            e.preventDefault();

            /* Get the row as a parent of the link that was clicked on */
            var nRow = $(this).parents('tr')[0];
            if (nEditing !== null && nEditing != nRow) {
                /* Currently editing - but not this row - restore the old before continuing to edit mode */
                restoreRow(oTable, nEditing);
                editRow(oTable, nRow);
                nEditing = nRow;
            } else if (nEditing == nRow && $(this).attr('rel')=="saveEdit") {
                /* Editing this row and want to save it */
                updateRow(oTable, nEditing);
                nEditing = null;
            } else {
                /* No edit in progress - let's start one */
                editRow(oTable, nRow);
                nEditing = nRow;
            }
        });
		
		table.on('click', '.save', function (e) {
            e.preventDefault();

            /* Get the row as a parent of the link that was clicked on */
            var nRow = $(this).parents('tr')[0];
            
            saveRow(oTable, nEditing);
            nEditing = null;
            
        });
    }

    return {

        //main function to initiate the module
        init: function () {
		handleTable();			
				
        }

    };

}();

