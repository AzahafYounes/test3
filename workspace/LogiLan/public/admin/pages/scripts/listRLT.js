var TableEditable4 = function () {

    var handleTable = function () {

		var table = $('#sample_editable_5');
		var deleteRows= $('#deleteRows5');
		
        
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
			jqTds[0].innerHTML = '<input id="checkbox" type="checkbox" class="checkboxes" value="'+id+'">';
            jqTds[1].innerHTML = '<select id="reali" class="form-control input"></select>';
            jqTds[2].innerHTML = '<input id="dateD" type="date" class="form-control input" required="required" value="' + aData[2] + '">';
			jqTds[3].innerHTML = '<input id="dateF" type="date" class="form-control input" required="required" value="'+ aData[3] + '">';
            jqTds[4].innerHTML = '<input id="montant" type="text" class="form-control input" required="required" value="' + aData[4] + '">';
            jqTds[5].innerHTML = '<a class="edit" href="" rel="saveEdit"><img src="../../assets/img/edit.png" title="Mettre à jour"></a> &nbsp; <a class="delete" href="" rel="'+id+'"><img src="../../assets/img/delete.png" title="Supprimer"></a>';


			$.ajax({
        	        type : "POST",
        	        url : "/RLT",
        	        dataType : 'json',
        	        success : function(response) {
						for(var i=0;i<response.length;i++){
							if(aData[1]==response[i].intitule)
								$("#reali").append('<option value ="'+response[i].id+'" selected="selected">'+response[i].intitule+'</option>');
						else
								$("#reali").append('<option value ="'+response[i].id+'">'+response[i].intitule+'</option>');
						
						}
						
        	        	
        	        },
        	        error : function(request, status, error) {
        	        }
					
        	    });	
					
				
				
			           
        }
		
		
		function addRow(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);
			
            jqTds[0].innerHTML = '<input type="checkbox" class="checkboxes" id="checkbox" value="1">';
			jqTds[1].innerHTML = '<select id="reali" class="form-control input"></select>';
            jqTds[2].innerHTML = '<input id="dateD" type="date" class="form-control input" required="required" value="' + aData[2] + '">';
			jqTds[3].innerHTML = '<input id="dateF" type="date" class="form-control input" required="required" value="' + aData[3] + '">';
            jqTds[4].innerHTML =  '<input id="montant" type="text" class="form-control input" value="' + aData[4] + '">';
            jqTds[5].innerHTML = '<a class="save" href="" rel="save"><img src="../../assets/img/save.png" title="Enregistrer"></a> &nbsp; <a class="cancel" href=""><img src="../../assets/img/cancel.png" title="Annuler"></a>';
           $.ajax({
        	        type : "POST",
        	        url : "/RLT",
        	        dataType : 'json',
        	        success : function(response) {
						for(var i=0;i<response.length;i++){
							$("#reali").append('<option value ="'+response[i].id+'">'+response[i].intitule+'</option>');
						}
        	        },
        	        error : function(request, status, error) {
        	        }
					
        	    });
				
        }
		
        
		function saveRow(oTable, nRow) {
				var reali=$("#reali").val();
				var real=$("#reali option:selected").text();
				var dateD=$("#dateD").val();
				var dateF=$("#dateF").val();
				var montant=$("#montant").val();
				
				$("#msgRe").html("enregistrement en cours veuillez attendre ...");
				$("#ajax4").modal("show");
				setTimeout(function() {$("#large4").modal("show").delay(2000).fadeOut();}, 1000);
        	    $.ajax({
        	        type : "POST",
        	        data : {
        	        	reali:reali,
						dateD:dateD,
						dateF:dateF,
						montant:montant,
        	        },
        	        url : "/addRLT_ok",
        	        dataType : 'json',
        	        success : function(response) {
						$("#ajax4").modal("hide");
        	        	$("#msg5").html("Enregistrement réussi");
						$("#large4").modal("show");
						setTimeout(function() {$("#large4").modal("hide");}, 2000);
        	            var jqInputs = $('input', nRow);
						oTable.fnUpdate('<input type="checkbox" class="checkboxes" id="checkbox" value="'+response.idRealisationCl+'"/>', nRow, 0, false);
        	            oTable.fnUpdate(real, nRow, 1, false);
        	            oTable.fnUpdate(dateD, nRow, 2, false);
						oTable.fnUpdate(dateF, nRow, 3, false);
        	            oTable.fnUpdate(montant, nRow, 4, false);
        	            oTable.fnUpdate('<a class="edit" href=""><img src="../../assets/img/edit.png" title="Modifier"></a> &nbsp;&nbsp; <a class="delete" rel="'+ response.idRealisationCl +'"><img src="../../assets/img/delete.png" title="Supprimer"></a>', nRow, 5, false);
        	            oTable.fnDraw();
        	        },
        	        error : function(request, status, error) {
						$("#ajax4").modal("hide");
        	        	$("#msg5").html("Erreur");
						$("#large4").modal("show");
						setTimeout(function() {$("#large4").modal("hide");}, 2000);
        	        }
					
        	    });
        }
		
		
		function updateRow(oTable, nRow) {
        	  //  $("#content").html('');
				var jqTds = $('>td', nRow);
				var id = $(jqTds[0]).find('input[type=checkbox]').val();
				var reali=$("#reali").val();
				var dateD=$("#dateD").val();
				var dateF=$("#dateF").val();
				var montant=$("#montant").val();
				var real=$("#reali option:selected").text();
				
				$("#msgRe").html("Mise à jour en cours veuillez attendre ...");
				$("#ajax4").modal("show");
				setTimeout(function() {$("#large4").modal("show").delay(2000).fadeOut();}, 1000);
        	    $.ajax({
        	        type : "POST",
        	        data : {
						id:id,
        	        	reali:reali,
						dateD:dateD,
						dateF:dateF,
						montant:montant,
        	        },
        	        url : "/modRLT_ok",
        	        dataType : 'json',
        	        success : function(response) {
						$("#ajax4").modal("hide");
        	        	$("#msg5").html("Mise à jour réussie");
						$("#large4").modal("show");
						setTimeout(function() {$("#large4").modal("hide");}, 2000);
        				//console.log(oTable);
        	            var jqInputs = $('input', nRow);
        	            oTable.fnUpdate('<input type="checkbox" class="checkboxes" id="checkbox" value="'+id+'"/>',nRow,0,false);
						oTable.fnUpdate(real, nRow, 1, false);
        	            oTable.fnUpdate(dateD, nRow, 2, false);
						oTable.fnUpdate(dateF, nRow, 3, false);
        	            oTable.fnUpdate(montant, nRow, 4, false);
        	            oTable.fnUpdate('<a class="edit" href=""><img src="../../assets/img/edit.png"/></a> &nbsp;&nbsp; <a class="delete" rel="'+ id +'"><img src="../../assets/img/delete.png"/></a>', nRow, 5, false);
        	            oTable.fnDraw();
        	        },
        	        error : function(request, status, error) {
						$("#ajax4").modal("hide");
        	        	$("#msg5").html("Erreur");
						$("#large4").modal("show");
						setTimeout(function() {$("#large4").modal("hide");}, 2000);
        	        }
					
        	    });
        }
		
		   

        function cancelEditRow(oTable, nRow) {
            var jqInputs = $('input', nRow);
			oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
            oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
            oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
			oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
            oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
            oTable.fnUpdate('<a class="edit" href=""><img src="../../assets/img/edit.png"/></a>', nRow, 5, false);
            oTable.fnDraw();
        }		
		
	
		
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
		
			function isBlank(str) {
				return (!str || /^\s*$/.test(str));
			}
		
	$('#sample_editable_5 #10 th').each(function(){
		var title = $('#sample_editable_5 #9 th').eq($(this).index()).text();
		if(title.indexOf("Actions") ==-1 && !isBlank(title))
				$(this).html('<input type="text" class="form-control input" placeholder="rechercher '+title+'"/>');
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
	
	
	/*	$('#sample_editable_1 tfoot th').each( function(){
			
			var title= $('#sample_editable_1 thead th').eq($(this).index() ).text();
			if(! $(title).find('td input[type=checkbox]')){
				$(this).html('<input type="text" placeholder="rechercher '+title+'"/>');
			}
		});
		
		*/
			


        var tableWrapper = $("#sample_editable_1_wrapper");

        /*tableWrapper.find(".dataTables_length select").select2({
            showSearchInput: false //hide search box with special css class
        }); // initialize select2 dropdown*/

		
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
		
        $('#sample_editable_5_new').click(function (e) {
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
        
				var aiNew = oTable.fnAddData(['', '', '', '', '','']);
				var nRow = oTable.fnGetNodes(aiNew[0]);
				addRow(oTable, nRow);
				nEditing = nRow;
				nNew = true;
        });
		
	
         table.on('click', 'td a.delete', function (e) {
            e.preventDefault();
			 //console.log(type);
            if (confirm("Etes-vous sur de vouloir supprimer la ligne ?") == false) {
                return;
				} 
				var nRow = $(this).parents('tr');
				$("#msgRe").html("Suppression en cours veuillez attendre ...");
				$("#ajax4").modal("show");
				var id = $(this).attr('rel');
				$.ajax({
        	        type : "POST",
        	        data : {
        	        	id : id,
        	        },
					url : "/supRLT_ok",
        	        dataType : 'json',
        	        success : function(response) {
					
					oTable.fnDeleteRow(nRow);
					$("#ajax4").modal("hide");
        	        $("#msg5").html("Suppression terminée");
					$("#large4").modal("show");
					setTimeout(function() {$("#large4").modal("hide");}, 2000);
					},
					error : function(request, status, error){
						$("#ajax4").modal("hide");
        	        	$("#msg5").html("Erreur");
						$("#large4").modal("show");
						setTimeout(function() {$("#large4").modal("hide");}, 2000);
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
					url : "/supRLT_ok",
        	        dataType : 'json',
        	        success : function(response) {
					oTable.fnDeleteRow(nRow);
					},
					error : function(request, status, error){}
					
					})
		
		}
		
		
		$('#deleteRows5').on("click", function(event){
		var sel = false;
		var ch = $('tr').find('td input[type=checkbox]');
		var c = confirm('Confirmer suppression?');
		if(c){
			
				$("#msgRe").html("Suppression en cours veuillez attendre ...");
				$("#ajax4").modal("show");
				setTimeout(function() {$("#large4").modal("show").delay(2000).fadeOut();}, 1000);
				ch.each(function(){
				var $this = $(this);
				if($this.is(':checked')) {
					sel = true; //set to true if there is/are selected row
					var id = $this.val();
					deleteRow(id,$this.parents('tr'));
				}
      });

          if(!sel) {
						$("#ajax4").modal("hide");
        	        	$("#msg5").html("Aucune ligne sélectionnée");
						$("#large4").modal("show");
						setTimeout(function() {$("#large4").modal("hide");}, 2000);
							return false;
		  }
						$("#ajax4").modal("hide");
        	        	$("#msg5").html("Suppression terminée");
						$("#large4").modal("show");
						setTimeout(function() {$("#large4").modal("hide");}, 2000);
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
            var nRow = $(this).parents('tr');
            
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

