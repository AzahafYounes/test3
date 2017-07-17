var TableEditable2 = function () {

    var handleTable = function () {

		var table = $('#sample_editable_3');
		var deleteRows= $('#deleteRows3');
		
        
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
			jqTds[0].innerHTML = '<input type="checkbox" class="checkboxes" id="checkbox2" value="'+id+'">';
            jqTds[1].innerHTML = '<select id="nomOfficine" class="form-control select2"></select>';
            jqTds[2].innerHTML = '<input id="adress" type="text" class="form-control input" value="'+ aData[2] +'">';
            jqTds[3].innerHTML = '<select id="responsable" class="form-control select" ></select>';
            jqTds[4].innerHTML = '<a class="edit" href="" rel="saveEdit"><img src="../../assets/img/edit.png" title="Mettre à jour"></a> &nbsp; <a class="delete" href="" rel="'+id+'"><img src="../../assets/img/delete.png" title="Supprimer"></a>';


			$.ajax({
        	        type : "POST",
        	        url : "/officines",
        	        dataType : 'json',
        	        success : function(response) {
						for(var i=0;i<response.length;i++){
							if(aData[1]==response[i].intitule)
								$("#nomOfficine").append('<option value ="'+response[i].id+'" selected="selected">'+response[i].nomOfficine+'</option>');
						else
								$("#nomOfficine").append('<option value ="'+response[i].id+'">'+response[i].nomOfficine+'</option>');
						
						}
						
        	        	
        	        },
        	        error : function(request, status, error) {
        	        }
					
        	    });	
					$.ajax({
        	        type : "POST",
        	        url : "/allUsers",
        	        dataType : 'json',
        	        success : function(response) {
						for(var i=0;i<response.length;i++){
							if(aData[3]==response[i])
								$("#responsable").append('<option value ="'+response[i]+'" selected="selected">'+response[i]+'</option>');
						else
								$("#responsable").append('<option value ="'+response[i]+'">'+response[i]+'</option>');
						
						}
						
        	        	
        	        },
        	        error : function(request, status, error) {
        	        }
					
        	    });	
				
				
			           
        }
		
		
		function addRow(oTable, nRow) {
			
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);
            
            var id = $("#nomOfficine").val();
            
           
			jqTds[0].innerHTML = '<input type="checkbox" class="checkboxes" id="checkbox2" value="1" >';
            jqTds[1].innerHTML = '<input type="text" id="nomOfficine1" class="form-control " disabled="disabled"/>';
            jqTds[2].innerHTML = '<input id="adress1" type="text" class="form-control" disabled="disabled" />';
            jqTds[3].innerHTML = '<input id="responsable1" type="text" class="form-control" disabled="disabled" />';
            jqTds[4].innerHTML = '<a class="save" href="" rel="save"><img src="../../assets/img/save.png" title="Enregistrer"></a> &nbsp; <a class="cancel" href="" ><img src="../../assets/img/cancel.png" title="Annuler"></a>';

            
            $.ajax({
            	type : "POST",
    	        data : {
    	        	id : id,
    	        },
				url : "getOfficineJson",
    	        dataType : 'json',
    	        success : function(response) {
    	        	alert("aaaaaaaaaaa")
    	        	$("#nomOfficine1").val(response.nomOfficine);
    	        	$("#adress1").val(response.adress);
    	        	$("#responsable1").val(response.user.nom+' '+response.user.prenom);
    	        	$("#checkbox2").val(response.id);	
							
						
						
        	        	
        	        },
        	        error : function(request, status, error) {
        	        	alert("bbbbbbbbbbbbb")
        	        }
					
        	    });
				
        }
		
        
		function saveRow(oTable, nRow) {
				var idOfficine=$("#checkbox2").val();
				alert(idOfficine)
				$("#msgRi").html("enregistrement en cours veuillez attendre ...");
				$("#ajax2").modal("show");
				setTimeout(function() {$("#large2").modal("show").delay(2000).fadeOut();}, 1000);
        	    $.ajax({
        	        type : "POST",
        	        data : {
        	        	idOfficine:idOfficine
        	        },
        	        url : "/addOfficineToGroup_ok",
        	        dataType : 'json',
        	        success : function(response) {
        	        	
        	        	var id =response.id;
        	        	var nomOfficine1=$("#nomOfficine1").val();
        	        	var adress1=$("#adress1").val();
        	        	var responsable1=$("#responsable1").val();
							
						$("#ajax2").modal("hide");
        	        	$("#msg3").html("Enregistrement réussi");
						$("#large2").modal("show");
						setTimeout(function() {$("#large2").modal("hide");}, 2000);
        	            var jqInputs = $('input', nRow);
						oTable.fnUpdate('<input type="checkbox" class="checkboxes" id="checkbox2" value="'+response.id+'">', nRow, 0, false);
        	            oTable.fnUpdate(nomOfficine1, nRow, 1, false);
        	            oTable.fnUpdate(adress1, nRow, 2, false);
        	            oTable.fnUpdate(reponsable1, nRow, 3, false);
        	            oTable.fnUpdate('<a class="edit" href=""><img src="../../assets/img/edit.png" title="Modifier"></a> &nbsp;&nbsp; <a class="delete" rel="'+ response.id +'"><img src="../../assets/img/delete.png" title="Supprimer"></a>', nRow, 4, false);
        	            oTable.fnDraw();
        	        },
        	        error : function(request, status, error) {
						$("#ajax").modal("hide");
        	        	$("#msg3").html("Erreur : "+ error);
						$("#large2").modal("show");
						setTimeout(function() {$("#large2").modal("hide");}, 2000);
        	        }
					
        	    });
        }
		
		
		function updateRow(oTable, nRow) {
        	    var jqTds = $('>td', nRow);
				var id = $(jqTds[0]).find('input[type=checkbox]').val();
				var nomOfficine=$("#nomOfficine1").val();
				var adress=$("#adress1").val();
				var responsable=$("#responsable1").val();
				
				$("#msgRi").html("Mise à jour en cours veuillez attendre ...");
				$("#ajax2").modal("show");
				setTimeout(function() {$("#large2").modal("show").delay(2000).fadeOut();}, 1000);
        	    $.ajax({
        	        type : "POST",
        	        data : {
						id:id,
        	        	nomOfficine:nomOfficine,
						adress:adress,
						responsable:responsable,
        	        },
        	        url : "/modRsq_ok",
        	        dataType : 'json',
        	        success : function(response) {
						$("#ajax2").modal("hide");
        	        	$("#msg3").html("Mise à jour réussie");
						$("#large2").modal("show");
						setTimeout(function() {$("#large2").modal("hide");}, 2000);
        				
        	            var jqInputs = $('input', nRow);
        	            oTable.fnUpdate('<input type="checkbox" class="checkboxes" id="checkbox2" value="'+id+'">',nRow,0,false);
						oTable.fnUpdate(risque1, nRow, 1, false);
        	            oTable.fnUpdate(date, nRow, 2, false);
        	            oTable.fnUpdate(priorite, nRow, 3, false);
        	            oTable.fnUpdate('<a class="edit" href=""><img src="../../assets/img/edit.png" title="Modifier"></a> &nbsp;&nbsp; <a class="delete" rel="'+ id +'"><img src="../../assets/img/delete.png" title="Supprimer"></a>', nRow, 4, false);
        	            oTable.fnDraw();
        	        },
        	        error : function(request, status, error) {
						$("#ajax2").modal("hide");
        	        	$("#msg3").html("Erreur");
						$("#large2").modal("show");
						setTimeout(function() {$("#large2").modal("hide");}, 2000);
        	        }
					
        	    });
        }
		
		   

        function cancelEditRow(oTable, nRow) {
            var jqInputs = $('input', nRow);
			oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
            oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
            oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
            oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
            oTable.fnUpdate('<a class="edit" href=""><img src="../../assets/img/edit.png"/></a>', nRow, 4, false);
            oTable.fnDraw();
        }		
		
	
		
        var oTable = table.dataTable({

           
            "lengthMenu": [
                [5, 15, 20, -1],
                [5, 15, 20, "All"] 
            ],

            "pageLength": 5,

            "language": {
            	url: "//cdn.datatables.net/plug-ins/1.10.15/i18n/French.json"
            },
            "columnDefs": [{ 
                'orderable': true,
                'targets': [0]
            }, {
                "searchable": true,
                "targets": [0]
            }],
            "order": [
                [0, "asc"]
            ] 
			
        });
		
	
			
	function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}
		
	$('#sample_editable_3 #6 th').each(function(){
		var title = $('#sample_editable_3 #5 th').eq($(this).index()).text();
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
		
        $('#sample_editable_3_new').click(function (e) {
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
        
				var aiNew = oTable.fnAddData(['', '', '', '', '']);
				var nRow = oTable.fnGetNodes(aiNew[0]);
				addRow(oTable, nRow);
				nEditing = nRow;
				nNew = true;
        });
		
		
		

	
         table.on('click', 'td a.delete', function (e) {
            e.preventDefault();
			 
            if (confirm("Etes-vous sur de vouloir supprimer la ligne ?") == false) {
                return;
				} 
				var nRow = $(this).parents('tr');
				$("#msgRi").html("Suppression en cours veuillez attendre ...");
				$("#ajax").modal("show");
				var id = $(this).attr('rel');
				$.ajax({
        	        type : "POST",
        	        data : {
        	        	id : id,
        	        },
					url : "/supRsq_ok",
        	        dataType : 'json',
        	        success : function(response) {
					
					oTable.fnDeleteRow(nRow);
					oTable.fnDraw();
						$("#ajax").modal("hide");
        	        	$("#msg3").html("Suppression terminée");
						$("#large2").modal("show");
						setTimeout(function() {$("#large2").modal("hide");}, 2000);
					},
					error : function(request, status, error){
						$("#ajax").modal("hide");
        	        	$("#msg3").html("Erreur");
						$("#large2").modal("show");
						setTimeout(function() {$("#large2").modal("hide");}, 2000);
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
					url : "/supRsq_ok",
        	        dataType : 'json',
        	        success : function(response) {
					oTable.fnDeleteRow(nRow);
					},
					error : function(request, status, error){}
					
					})
		
		}
		
		
		$('#deleteRows3').on("click", function(event){
		var sel = false;
		var ch = $('tr').find('td input[type=checkbox]');
		var c = confirm('Confirmer suppression?');
		if(c){
			
				$("#msgRi").html("Suppression en cours veuillez attendre ...");
				$("#large2").modal("show");
				setTimeout(function() {$("#large2").modal("hide");}, 2000);
				ch.each(function(){
				var $this = $(this);
				if($this.is(':checked')) {
					sel = true; //set to true if there is/are selected row
					var id = $this.val();
					deleteRow(id,$this.parents('tr'));
				}
      });

          if(!sel) {
							$("#ajax").modal("hide");
        	        	$("#msg3").html("Aucune ligne sélectionnée");
						$("#large2").modal("show");
						setTimeout(function() {$("#large2").modal("hide");}, 2000);
							return false;
		  }
						$("#ajax2").modal("hide");
        	        	$("#msg3").html("Suppression terminée");
						$("#large2").modal("show");
						setTimeout(function() {$("#large2").modal("hide");}, 2000);
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

