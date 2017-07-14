var TableEditable = function () {

    var handleTable = function () {
	
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

        /*function cancelEditRow(oTable, nRow) {
            var jqInputs = $('input', nRow);
            oTable.fnUpdate(jqInputs[0].value, nRow, 0, false);
            oTable.fnUpdate(jqInputs[1].value, nRow, 1, false);
            oTable.fnUpdate(jqInputs[2].value, nRow, 2, false);
            oTable.fnUpdate(jqInputs[3].value, nRow, 3, false);
            oTable.fnUpdate('<a class="edit" href="">Modifier</a>', nRow, 4, false);
            oTable.fnDraw();
        }*/

        var table = $('#sample_editable_1');

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
            "pageLength": 10,

            "language": {
                "lengthMenu": " _MENU_ records"
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
		
	$('#sample_editable_1 #2 th').each(function(){
		var title = $('#sample_editable_1 #1 th').eq($(this).index()).text();
		if(title.indexOf("Actions") ==-1 && !isBlank(title))
				$(this).html('<input type="text" class="form-control input"/>');
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

        /*tableWrapper.find(".dataTables_length select").select2({
            showSearchInput: false //hide search box with special css class
        }); // initialize select2 dropdown*/

        var nEditing = null;
        var nNew = false;

        $('#sample_editable_1_new').click(function (e) {
            e.preventDefault();

            if (nNew && nEditing) {
                if (confirm("Ligne précédente non engegistée. Enregistrer maintenant ?")) {
                    saveRow(oTable, nEditing); // save
                    $(nEditing).find("td:first").html("Untitled");
                    nEditing = null;
                    nNew = false;

                } else {
                    oTable.fnDeleteRow(nEditing); // cancel
                    nEditing = null;
                    nNew = false;
                    
                    return;
                }
            }

            var aiNew = oTable.fnAddData(['', '', '', '', '', '','','','']);
            var nRow = oTable.fnGetNodes(aiNew[0]);
            editRow(oTable, nRow);
            nEditing = nRow;
            nNew = true;
        });
		
		
		

        table.on('click', '.delete', function (e) {
            e.preventDefault();
            if (confirm("Etes-vous sur de vouloir supprimer la ligne ?") == false) {
                return;
            }
			
            var nRow = $(this).parents('tr')[0];
            oTable.fnDeleteRow(nRow);
            alert("Ligne supprimée");
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
            } else if (nEditing == nRow && this.innerHTML == "Enregistrer") {
                /* Editing this row and want to save it */
                saveRow(oTable, nEditing);
                nEditing = null;
                alert("Ligne mise à jour.");
            } else {
                /* No edit in progress - let's start one */
                editRow(oTable, nRow);
                nEditing = nRow;
            }
        });
		
		table.on('click', 'a.chute', function (e) {
			var nRow = $(this).parents('tr')[0];
			var id = $(this).attr('rel');
			$.ajax({
        	        type : "POST",
					data : {
        	        	id : id,
        	        },
        	        url : "/rdCh",
        	        dataType : 'json',
        	        success : function(response) {
					oTable.fnUpdate("chute",nRow,7,false);
					oTable.fnDraw();
					},
        	        error : function(request, status, error) {
        	        }
					
        	    });			
		
		});
		
		
			table.on('click', '.cl', function (e) {
		var nRow = $(this).parents('tr')[0];
			var id = $(this).attr('rel');
			$.ajax({
        	        type : "POST",
					data : {
        	        	id : id,
        	        },
        	        url : "/rdCl",
        	        dataType : 'json',
        	        success : function(response) {
						oTable.fnUpdate("client",nRow,7,false);
						oTable.fnDraw();
        	        },
        	        error : function(request, status, error) {
						
        	        }
					
        	    });	
		
		});
		
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
		
		function updateRowType(v,nRow){
			$.ajax({
        	        type : "POST",
        	        data : {
        	        	id : v,
        	        },
					url : "/rdCl",
        	        dataType : 'json',
        	        success : function(response) {
						oTable.fnUpdate(response.type,nRow,7,false);
						oTable.fnUpdate(response.assure,nRow,6,false);
					},
					error : function(request, status, error){}
					
			});
			
		}
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
						oTable.fnUpdate(response.type,nRow,7,false);
						oTable.fnUpdate(response.assure,nRow,6,false);
					},
					error : function(request, status, error){}
					
			});
			
		}
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
		
		
	
		
		$("#search").click(function (e) {
document.getElementById("haa").style.display="none"
  var gg=$("#derdv").val();

	
		
		
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
			
			
			var derdv=$("#derdv").val();
			var Ardv=$("#Ardv").val();
			var Deech=$("#deech").val();
			var Aech=$("#Aech").val();
			var deaj=$("#deaj").val();
			var Aaj=$("#Aaj").val();
			
			
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
						derdv:derdv,
						Ardv:Ardv,
						Deech:Deech,
						Aech:Aech,
						deaj:deaj,
						Aaj:Aaj,
        	        },
        	        url : "Search",
        	        dataType : 'json',
        	        success : function(response) {
					
						oTable.fnClearTable();
						for(var i=0;i<response.length;i++){
							var aiNew = oTable.fnAddData(['', '', '', '', '', '','','','']);
							var nRow = oTable.fnGetNodes(aiNew[i]);
							var jqInputs = $('input', nRow);
							  oTable.fnUpdate('<input type="checkbox" class="checkboxes" value="'+response[i].id+'"/>', nRow, 0, false);
							 oTable.fnUpdate(response[i].nom +" "+response[i].prenom , nRow, 1, false);
							 oTable.fnUpdate(response[i].zone, nRow, 2, false);
							oTable.fnUpdate(response[i].tel, nRow, 3, false);
							oTable.fnUpdate(response[i].assure, nRow, 4, false);
							 oTable.fnUpdate(response[i].type, nRow, 5, false);
							 oTable.fnUpdate(response[i].type, nRow, 6, false);
							oTable.fnUpdate(response[i].type, nRow, 7, false);
							oTable.fnUpdate('<a onClick="edit('+response[i].id+')" "><img src="../../assets/img/edit.png"/></a><a  onclick="viewPro('+response[i].id+')"><img src="../../assets/img/detail.png" title="view" data-toggle="modal" data-target="#myModal"></a>&nbsp;&nbsp;<a class="cl" href="" rel="'+ response[i].id +'"><img src="../../assets/img/cl.png"/></a>;<a class="chute" href="" rel="'+ response[i].id +'"><img src="../../assets/img/chute.png"/></a>', nRow, 8, false);
							oTable.fnDraw();
							
						}
									document.getElementById("myModalB").style.display="none"
			

					},
					error : function(request, status, error) {
								 
					}
				
			});
			
			
			
			
		
	
	
}
			});

	
		
		
    }
	

    return {

        //main function to initiate the module
        init: function () {
            handleTable();
        }

    };

}();
function edit(id)
	
	document.cookie="idProspect="+id;
	document.location="modifierPro";
}

