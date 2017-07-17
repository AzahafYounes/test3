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

        $('#cl1').on("click", function(event){
		var sel = false;
		var ch = $('tr').find('td input[type=checkbox]');
				ch.each(function(){
					var $this = $(this);
					if($this.is(':checked')) {
						sel = true; 
						var id = $this.val();
						updateRowType(id,$this.parents('tr'));
					}
				});
		
		});
		
		$('#chute1').on("click", function(event){
		var sel = false;
		var ch = $('tr').find('td input[type=checkbox]');
				ch.each(function(){
					var $this = $(this);
					if($this.is(':checked')) {
						sel = true; 
						var id = $this.val();
						updateRowTypeCh(id,$this.parents('tr'));
					
					}
				});
		});
		
		$('#prospect1').on("click", function(event){
			var sel = false;
			var ch = $('tr').find('td input[type=checkbox]');
					ch.each(function(){
						var $this = $(this);
						if($this.is(':checked')) {
							sel = true; 
							var id = $this.val();
							updateRowTypePr(id,$this.parents('tr'));
						
						}
					});
			});
		
		
			function updateRowTypeCh(v,nRow){
			$.ajax({
        	        type : "POST",
        	        data : {
        	        	id : v,
        	        },
					url : "/rdCh",
        	        dataType : 'json',
        	        success : function(response) {
						oTable.fnUpdate(response.type,nRow,5,false);
					},
					error : function(request, status, error){}
					
			});
			
		}
			
			function updateRowTypePr(v,nRow){
				$.ajax({
	        	        type : "POST",
	        	        data : {
	        	        	id : v,
	        	        },
						url : "/rdPr",
	        	        dataType : 'json',
	        	        success : function(response) {
							oTable.fnUpdate(response.type,nRow,5,false);
						},
						error : function(request, status, error){}
						
				});
				
			}
			
			function updateRowType(v,nRow){
			$.ajax({
        	        type : "POST",
        	        data : {
        	        	id : v,
        	        },
					url : "/rdCl",
        	        dataType : 'json',
        	        success : function(response) {
						oTable.fnUpdate(response.type,nRow,5,false);
					},
					error : function(request, status, error){}
					
			});
			
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
                [1, "asc"]
            ] // set first column as a default sort by asc
			
        });
		
			function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}
		
	$('#sample_editable_1 #2 th').each(function(){
		var title = $('#sample_editable_1 #1 th').eq($(this).index()).text();
		if(title.indexOf("Actions") ==-1 && !isBlank(title))
				$(this).html('<input type="text" class="form-control input" placeholder="Rechercher"/>');
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
		
		
			table.on('click', 'a.chute', function (e) {
		
			var id = $(this).attr('rel');
			var nRow = $(this).parents('tr')[0];
			var jqTds = $('>td', nRow);
			$.ajax({
        	        type : "POST",
					data : {
        	        	id : id,
        	        },
        	        url : "/rdCh",
        	        dataType : 'json',
        	        success : function(response) {
						oTable.fnUpdate("chute",nRow,5,false);
						oTable.fnDraw();
        	        },
        	        error : function(request, status, error) {
        	        }
					
        	    });			
		
		});
			
	table.on('click', 'a.prospect1', function (e) {
				
				var id = $(this).attr('rel');
				var nRow = $(this).parents('tr')[0];
				var jqTds = $('>td', nRow);
				$.ajax({
	        	        type : "POST",
						data : {
	        	        	id : id,
	        	        },
	        	        url : "/rdPr",
	        	        dataType : 'json',
	        	        success : function(response) {
							oTable.fnUpdate("prospect",nRow,5,false);
							oTable.fnDraw();
	        	        },
	        	        error : function(request, status, error) {
	        	        }
						
	        	    });			
			
			});
			
		
			table.on('click', '.cl', function (e) {
		
			var id = $(this).attr('rel');
			var nRow = $(this).parents('tr')[0];
			$.ajax({
        	        type : "POST",
					data : {
        	        	id : id,
        	        },
        	        url : "/rdCl",
        	        dataType : 'json',
        	        success : function(response) {
						oTable.fnUpdate("client",nRow,5,false);
						oTable.fnDraw();
					},
        	        error : function(request, status, error) {
						
        	        }
					
        	    });	
		
		});
			
			$('#prospect1').on("click", function(event){
				var sel = false;
				var ch = $('tr').find('td input[type=checkbox]');
						ch.each(function(){
							var $this = $(this);
							if($this.is(':checked')) {
								sel = true; 
								var id = $this.val();
								updateRowTypeCh(id,$this.parents('tr'));
							
							}
						});

		        
				
				});
		
		$('#chute1').on("click", function(event){
		var sel = false;
		var ch = $('tr').find('td input[type=checkbox]');
				ch.each(function(){
					var $this = $(this);
					if($this.is(':checked')) {
						sel = true; 
						var id = $this.val();
						updateRowTypeCh(id,$this.parents('tr'));
					
					}
				});

        
		
		});
		
		function updateRowTypeCh(v,nRow){
			$.ajax({
        	        type : "POST",
        	        data : {
        	        	id : v,
        	        },
					url : "/rdCh",
        	        dataType : 'json',
        	        success : function(response) {
						oTable.fnUpdate(response.type,nRow,5,false);
					},
					error : function(request, status, error){}
					
			});
			
		}
		
		function updateRowTypePr(v,nRow){
			$.ajax({
        	        type : "POST",
        	        data : {
        	        	id : v,
        	        },
					url : "/rdPr",
        	        dataType : 'json',
        	        success : function(response) {
						oTable.fnUpdate(response.type,nRow,5,false);
					},
					error : function(request, status, error){}
					
			});
			
		}
		
			$("#search").click(function (e) {
		
			var d = document.getElementById("ha");
			d.className ="expand";
			document.getElementById("haa").style.display="none";
			
			/* var From = $('#entriesFrom').val();
			var To = $('#entriesTo').val();
			console.log(From);
			console.log(To);
			if(From==0){
			alert("Veuillez choisir une date d\351but");
				return false;
			}
		
			if(To==0 || To - From=="NaN")
			{alert("Veuillez choisir une date Fin convenable");
				return false;
			}
		 */
			
			var cl=document.getElementsByName("type")[0].checked;
			if(cl) var client="client";
			else var client="";
			var pros=document.getElementsByName("type")[1].checked;
			if(pros) var prospect ="prospect";
			else var prospect ="";
			var ch=document.getElementsByName("type")[2].checked;
			if(ch)	var chute="chute";
			else var chute="";
			var risque=$("#risque").val();
			var categorie=$("#categorie").val();
			var ass=document.getElementsByName("assure")[0].checked;
			var ass1=document.getElementsByName("assure")[1].checked;
			if(ass){
				var assure="Oui";
			}
			else if(ass1){ 
			var assure="Non";
			}
			else var assure="";
			var nom=$("#nom").val();
			var prenom=$("#prenom").val();
			var zone=$("#zone").val();
			var tel=$("#tel").val();
			var fix=$("#fix").val();
			var fax=$("#fax").val();
			var adresse=$("#adresse").val();
			var email=$("#email").val();
			var nomEntr=$("#nomEnt").val();
			var inter=$("#inter").val();
			var source=$("#source").val();
			var score=$("#score").val();
		
			
			$.ajax({
				type:"POST",
				data : {
        	        	client:client,
						prospect:prospect,
						chute:chute,
						risque:risque,
						categorie:categorie,
						assure:assure,
						nom:nom,
						prenom:prenom,
						zone:zone,
						tel:tel,
						fix:fix,
						fax:fax,
						adresse:adresse,
						email:email,
						nomEntr:nomEntr,
						inter:inter,
						source:source,
						score:score,
        	        },
        	        url : "Search",
        	        dataType : 'json',
        	        success : function(response) {
						
						oTable.fnClearTable();
						for(var i=0;i<response.length;i++){
							var aiNew = oTable.fnAddData(['', '', '', '', '', '','','','']);
							var nRow = oTable.fnGetNodes(aiNew[i]);
							var start = new Date(response[i].creationDate);
							var jqInputs = $('input', nRow);
							 oTable.fnUpdate('<input type="checkbox" class="checkboxes" value="'+response[i].id+'"/>', nRow, 0, false);
							 oTable.fnUpdate(response[i].nom +" "+response[i].prenom , nRow, 1, false);
							 oTable.fnUpdate(response[i].zone, nRow, 2, false);
							oTable.fnUpdate(response[i].tel, nRow, 3, false);
							oTable.fnUpdate(response[i].assure, nRow, 4, false);
							 oTable.fnUpdate(response[i].type, nRow, 5, false);
							 oTable.fnUpdate(response[i].user.nom+" "+response[i].user.prenom, nRow, 6, false);
							oTable.fnUpdate(start.getDate()+"/"+(start.getMonth()+1)+"/"+start.getFullYear(), nRow, 7, false);
							oTable.fnUpdate('<a class="edit" href="/modifierPro"><img src="../../assets/img/edit.png" title="Modifier"></a>&nbsp;&nbsp;<a class="cl" href="" rel="'+ response[i].id +'"><img src="../../assets/img/cl.png" title="Rendre client"></a>;<a class="chute" href="" rel="'+ response[i].id +'"><img src="../../assets/img/chute.png" title="Rendre chute"></a>;<a class="prospect1" href="" rel="'+ response[i].id +'"><img src="../../assets/img/prospect1.png" title="prospect"></a>', nRow, 8, false);
							oTable.fnDraw();
						
						
						}
							
						
					},
					error : function(request, status, error) {
								 
					}
				
			});
			
			
			
			
		});
	

	
		
    }

    return {

        //main function to initiate the module
        init: function () {
		handleTable();			
				
        }

    };

}();
function edit(id){
	document.cookie="idProspect="+id;
	document.location="modifierPro";
}

