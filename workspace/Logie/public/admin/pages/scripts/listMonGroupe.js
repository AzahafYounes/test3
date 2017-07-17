var TableEditable = function () {

    var handleTable = function () {

		var table = $('#sample_editable_1');
		var deleteRows= $('#deleteRows');
		
        
		function restoreRow(oTable, nRow) {
            var aData = oTable.fnGetData(nRow);
            var jqTds = $('>td', nRow);

            for (var i = 0, iLen = jqTds.length; i < iLen; i++) {
                oTable.fnUpdate(aData[i], nRow, i, false);
            }

            oTable.fnDraw();
        }
   

        function cancelEditRow(oTable, nRow) {
            var jqInputs = $('input', nRow);
			oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
            oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
            oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
            oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
            oTable.fnUpdate(jqInputs[4].value, nRow, 4, false);
			oTable.fnUpdate(jqInputs[5].value, nRow, 5, false);
            oTable.fnUpdate('<a class="edit" href=""><img src="../../assets/img/edit.png"/></a>', nRow, 6, false);
            oTable.fnDraw();
        }	
		
	
		
        var oTable = table.dataTable({

            // Uncomment below line("dom" parameter) to fix the dropdown overflow issue in the datatable cells. The default datatable layout
            // setup uses scrollable div(table-scrollable) with overflow:auto to enable vertical scroll(see: assets/global/plugins/datatables/plugins/bootstrap/dataTables.bootstrap.js). 
            // So when dropdowns used the scrollable div should be removed. 
            //"dom": "<'row'<'col-md-6 col-sm-12'l><'col-md-6 col-sm-12'f>r>t<'row'<'col-md-5 col-sm-12'i><'col-md-7 col-sm-12'p>>",

            "lengthMenu": [
                [-1, , 15, 50],
                ["Tous", 15 , 50] // change per page values here
            ],

            // Or you can use remote translation file
            //"language": {
            //   url: '//cdn.datatables.net/plug-ins/3cfcc339e89/i18n/Portuguese.json'
            //},

            // set the initial value
            "pageLength": 15,

            "language": {
            	url: "//cdn.datatables.net/plug-ins/1.10.15/i18n/French.json"
				
            },
            "columnDefs": [{ // set default column settings
                'orderable': true,
                'targets': [0]
            }, {
                "searchable": false,
                "targets": [0]
            }],
            "order": [
                [0, "asc"]
            ] // set first column as a default sort by asc
			
        });
			

        var tableWrapper = $("#sample_editable_1_wrapper");

        tableWrapper.find(".dataTables_length select").select2({
            showSearchInput: false //hide search box with special css class
        }); // initialize select2 dropdown

		
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
		

	
         table.on('click', 'td a.delete', function (e) {
            e.preventDefault();
			 
            if (confirm("Etes-vous sur de vouloir supprimer cette officine ?") == false) {
                return;
				} 
				var nRow = $(this).parents('tr');
				$("#msg").html("Suppression en cours veuillez attendre ...");
				$("#ajax").modal("show");
				var id = $(this).attr('rel');
				
				$.ajax({
        	        type : "POST",
        	        data : {
        	        	id : id,
        	        },
					url : "deleteOfficineGR",
        	        dataType : 'json',
        	        success : function(response) {
					oTable.fnDeleteRow(nRow);
					$("#ajax").modal("hide");
							$("#msg").html("Suppression terminée");
							$("#large2").modal("show");
							setTimeout(function() {$("#large2").modal("hide");}, 2000);
					},
					error : function(request, status, error){
							$("#ajax").modal("hide");
							$("#msg").html("Erreur lors de la suppression");
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
					url : "deleteOfficineGR",
        	        dataType : 'json',
        	        success : function(response) {
						oTable.fnDeleteRow(nRow);
						$("#ajax").modal("hide");
							$("#msg").html("Suppression terminée");
							$("#large2").modal("show");
							setTimeout(function() {$("#large2").modal("hide");}, 2000);
					},
					error : function(request, status, error){
						
						$("#ajax").modal("hide");
							$("#msg").html("Erreur lors de la suppression");
							$("#large2").modal("show");
							setTimeout(function() {$("#large2").modal("hide");}, 2000);
					}
					
						
					});
		
		}
		
		$('#deleteRows').on("click", function(event){
		var sel = false;
		var ch = $('tr').find('td input[type=checkbox]');
		var c = confirm('Confirmer suppression?');
		if(c){
				$("#msg").html("Suppression en cours veuillez attendre ...");
				$("#ajax").modal("show");
				setTimeout(function() {$("#large2").modal("show").delay(2000).fadeOut();}, 1000);
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
							$("#msg").html("Aucune ligne sélectionnée");
							$("#large2").modal("show");
							setTimeout(function() {$("#large2").modal("hide");}, 2000);
							return false;
					}
							$("#ajax").modal("hide");
							$("#msg").html("Suppression terminée");
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

	$('input[id="switch"]').on('switchChange.bootstrapSwitch', function(event, state) {
			
			var id = $(this).val();
			changeStatus(id);
			
	});
		


 
