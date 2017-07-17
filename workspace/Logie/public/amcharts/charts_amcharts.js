var entriesSelect = $('#chartdiv_select');
var names = [];

var entriesComm = $('#entriesComm_submit');
var clientVisit = $('#clientVisit_submit');
var modifData = $('#ModifData_submit');
var quotaDR = $('#QuotaDR_submit');
var palmares = $('#palmares_submit');
var realiproduit = $('#brancheProd_submit');
var drc = $('#DRC_submit');

$(document)
		.ready(
				function() {
					// realisation devis chute

					// Nombre de modifications pour derniere semaine
					var columns = [];
					$
							.ajax({
								type : "POST",
								url : "recordUpdateInOldData",
								dataType : 'json',
								success : function(response) {
									for (var i = 0; i < response[0].length; i++) {
										columns[i] = {
											"date" : response[0][i],
											"column-1" : response[1][i]
										};
									}
									var title = "Nombre de modifications pour cette semaine"
									AmCharts
											.makeChart(
													"chartdiv_6",
													{
														"type" : "serial",
														"categoryField" : "date",
														"dataDateFormat" : "YYYY-MM-DD",
														"categoryAxis" : {
															"parseDates" : true
														},
														"chartCursor" : {},
														"chartScrollbar" : {},
														"trendLines" : [],
														"graphs" : [ {
															"balloonColor" : "#FFFF00",
															"bullet" : "round",
															"color" : "#C0FBA7",
															"columnWidth" : 0.49,
															"cornerRadiusTop" : 2,
															"fillAlphas" : 0.58,
															"fillColors" : "#004614",
															"gradientOrientation" : "horizontal",
															"id" : "AmGraph-1",
															"lineColor" : "#B5CF78",
															"negativeFillAlphas" : 0,
															"negativeLineAlpha" : 0,
															"title" : "Nombre Modifications/jour",
															"topRadius" : 0,
															"type" : "column",
															"valueField" : "column-1"
														} ],
														"guides" : [],
														"valueAxes" : [ {
															"id" : "ValueAxis-1",
															"title" : "Axis title"
														} ],
														"allLabels" : [],
														"balloon" : {},
														"legend" : {
															"useGraphSettings" : true
														},
														"titles" : [ {
															"id" : "Title-1",
															"size" : 15,
															"text" : title
														} ],
														"dataProvider" : columns
													});

								},
								error : function(request, status, error) {
									console.log("Erreur");

								}

							});

					var palmares = [];
					$.ajax({
						type : "POST",
						url : "palmaresCommPerProduct",
						dataType : 'json',
						success : function(response) {
							var title = 'Derniere semaine';
							for (var i = 0; i < response[0].length; i++) {
								palmares[i] = {
									"category" : response[0][i],
									"column-1" : response[1][i]
								};
							}
							var title = 'Pour la dernière semaine'
							AmCharts.makeChart("chartdiv_5", {
								"type" : "serial",
								"categoryField" : "category",
								"startDuration" : 1,
								"theme" : "light",
								"categoryAxis" : {
									"gridPosition" : "start"
								},
								"chartCursor" : {},
								"chartScrollbar" : {},
								"trendLines" : [],
								"graphs" : [ {
									"columnWidth" : 0.37,
									"fillAlphas" : 1,
									"id" : "AmGraph-1",
									"title" : "graph 1",
									"type" : "column",
									"valueField" : "column-1"
								} ],
								"guides" : [],
								"valueAxes" : [ {
									"id" : "ValueAxis-1",
									"title" : "Nombre palmares"
								} ],
								"allLabels" : [],
								"balloon" : {},
								"titles" : [ {
									"id" : "Title-1",
									"size" : 15,
									"text" : title
								} ],
								"dataProvider" : palmares
							});
						},
						error : function(request, status, error) {
							console.log("Erreur");

						}

					});
					// executes when HTML-Document is loaded and DOM is ready

					/*
					 * $.ajax({ type : "POST", url : "inputsPerTime", dataType :
					 * 'json', success : function(response) { var inputWeek=[];
					 * for(var i=0; i<response[0].length;i++){
					 * 
					 * for(var k=0;k<response[2].length;k++) {var j =k+1;
					 * inputWeek[i]={ "date": response[0][i], "column-1":
					 * response[1][0][i], "column-2": response[1][1][i] };
					 * names[k]= { "bullet": "round", "id": "AmGraph-"+j,
					 * "title": response[2][k], "valueField": "column-"+j }; } }
					 * 
					 * AmCharts.makeChart("chartdiv", { "type": "serial",
					 * "categoryField": "date", "dataDateFormat": "YYYY-MM-DD",
					 * "categoryAxis": { "parseDates": true }, "chartCursor":
					 * {}, "chartScrollbar": {}, "trendLines": [], "graphs":
					 * names, "guides": [], "valueAxes": [ { "id":
					 * "ValueAxis-1", "title": "Axis title" } ], "allLabels":
					 * [], "balloon": {}, "legend": { "useGraphSettings": true },
					 * "titles": [ { "id": "Title-1", "size": 15, "text":
					 * "Nombre d'entr\351es par commercial" } ], "dataProvider":
					 * inputWeek } ); }, error : function(request, status,
					 * error) { console.log("Erreur");
					 *  }
					 * 
					 * });
					 * 
					 * 
					 */
					// Récupérer commerciaux
					$
							.ajax({
								type : "POST",
								url : "getCommerciaux",
								dataType : 'json',
								success : function(response) {
									$(entriesSelect).html('');
									$(entriesSelect)
											.append(
													'<option id="0">Selectionner Commercial*</option>');
									for (var i = 0; i < response.length; i++) {
										$(entriesSelect).append(
												'<option id="' + response[i].id
														+ '">'
														+ response[i].nom + ' '
														+ response[i].prenom
														+ '</option>');
									}
								},
								error : function(request, status, error) {
									$(entriesSelect)
											.html(
													'<option id="-1">none available</option>');
								}

							});

					// Quota devis realisation pour cette semaine
					$
							.ajax({
								type : "POST",
								url : "quotaDevisRealisation",
								dataType : 'json',
								success : function(response) {
									var title = 'Quota pour la derniere semaine';

									AmCharts
											.makeChart(
													"chartdiv_4",
													{
														"type" : "pie",
														"balloonText" : "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
														"titleField" : "category",
														"valueField" : "column-1",
														"allLabels" : [],
														"balloon" : {},
														"legend" : {
															"align" : "center",
															"markerType" : "circle"
														},
														"titles" : [ {
															"id" : "Title-1",
															"size" : 15,
															"text" : title
														} ],
														"dataProvider" : [
																{
																	"category" : "Devis",
																	"column-1" : response[0]
																},
																{
																	"category" : "R\351alisations",
																	"column-1" : response[1]
																} ]
													});
								},
								error : function(request, status, error) {
									console.log("Erreur");

								}

							});

					$.ajax({
						type : "POST",
						url : "clientVisitPerDay",
						dataType : 'json',
						success : function(response) {
							var visitWeek = [];
							for (var i = 0; i < response[0].length; i++) {
								visitWeek[i] = {
									"date" : response[0][i],
									"column-1" : response[1][i]
								};
							}
							var title = "Visites pour la dernière semaine"
							AmCharts.makeChart("chartdiv_1", {
								"type" : "serial",
								"categoryField" : "date",
								"dataDateFormat" : "YYYY-MM-DD",
								"categoryAxis" : {
									"parseDates" : true
								},
								"chartCursor" : {},
								"chartScrollbar" : {},
								"trendLines" : [],
								"graphs" : [ {
									"fillAlphas" : 0.7,
									"id" : "AmGraph-1",
									"lineAlpha" : 0,
									"title" : "Jours",
									"valueField" : "column-1"
								} ],
								"guides" : [],
								"valueAxes" : [ {
									"id" : "ValueAxis-1",
									"title" : "Nombre clients"
								} ],
								"allLabels" : [],
								"balloon" : {},
								"legend" : {},
								"titles" : [ {
									"id" : "Title-1",
									"size" : 15,
									"text" : title
								} ],
								"dataProvider" : visitWeek
							});
						},
						error : function(request, status, error) {
							console.log("Erreur");

						}

					});

				});

//

realiproduit.click(function() {

	var From = $('#brancheProdFrom').val();
	var To = $('#brancheProdTo').val();
	if (From == 0) {
		alert("Veuillez choisir une date d\351but");
		return false;
	}

	if (To == 0 || To - From == "NaN") {
		alert("Veuillez choisir une date Fin convenable");
		return false;
	}
	var palmares = [];
	$.ajax({
		type : "POST",
		url : "realisationProduct",
		data : {
			From : From,
			To : To
		},
		dataType : 'json',
		success : function(response) {
			var title = 'Derniere semaine';
			for (var i = 0; i < response[0].length; i++) {
				palmares[i] = {
					"category" : response[0][i],
					"column-1" : response[1][i]
				};
			}
			var title = 'Entre ' + From + ' et ' + To;
			AmCharts.makeChart("chartdiv_7", {
				"type" : "serial",
				"categoryField" : "category",
				"startDuration" : 1,
				"theme" : "light",
				"categoryAxis" : {
					"gridPosition" : "start"
				},
				"chartCursor" : {},
				"chartScrollbar" : {},
				"trendLines" : [],
				"graphs" : [ {
					"columnWidth" : 0.37,
					"fillAlphas" : 1,
					"id" : "AmGraph-1",
					"title" : "graph 1",
					"type" : "column",
					"valueField" : "column-1"
				} ],
				"guides" : [],
				"valueAxes" : [ {
					"id" : "ValueAxis-1",
					"title" : "Nombre realisations"
				} ],
				"allLabels" : [],
				"balloon" : {},
				"titles" : [ {
					"id" : "Title-1",
					"size" : 15,
					"text" : title
				} ],
				"dataProvider" : palmares
			});
		},
		error : function(request, status, error) {
			console.log("Erreur");

		}

	});
})

//

palmares.click(function() {

	var From = $('#palmaresFrom').val();
	var To = $('#palmaresTo').val();
	if (From == 0) {
		alert("Veuillez choisir une date d\351but");
		return false;
	}

	if (To == 0 || To - From == "NaN") {
		alert("Veuillez choisir une date Fin convenable");
		return false;
	}
	var palmares = [];
	$.ajax({
		type : "POST",
		url : "palmaresCommPerProduct",
		data : {
			From : From,
			To : To
		},
		dataType : 'json',
		success : function(response) {
			var title = 'Derniere semaine';
			for (var i = 0; i < response[0].length; i++) {
				palmares[i] = {
					"category" : response[0][i],
					"column-1" : response[1][i]
				};
			}
			var title = 'Entre ' + From + ' et ' + To;
			AmCharts.makeChart("chartdiv_5", {
				"type" : "serial",
				"categoryField" : "category",
				"startDuration" : 1,
				"theme" : "light",
				"categoryAxis" : {
					"gridPosition" : "start"
				},
				"chartCursor" : {},
				"chartScrollbar" : {},
				"trendLines" : [],
				"graphs" : [ {
					"columnWidth" : 0.37,
					"fillAlphas" : 1,
					"id" : "AmGraph-1",
					"title" : "graph 1",
					"type" : "column",
					"valueField" : "column-1"
				} ],
				"guides" : [],
				"valueAxes" : [ {
					"id" : "ValueAxis-1",
					"title" : "Nombre palmares"
				} ],
				"allLabels" : [],
				"balloon" : {},
				"titles" : [ {
					"id" : "Title-1",
					"size" : 15,
					"text" : title
				} ],
				"dataProvider" : palmares
			});
		},
		error : function(request, status, error) {
			console.log("Erreur");

		}

	});
})
entriesComm.click(function() {
	var id = $(entriesSelect).children(":selected").attr("id");
	var From = $('#entriesFrom').val();
	var To = $('#entriesTo').val();
	console.log(From);
	console.log(To);
	if (From == 0) {
		alert("Veuillez choisir une date d\351but");
		return false;
	}

	if (To == 0 || To - From == "NaN") {
		alert("Veuillez choisir une date Fin convenable");
		return false;
	}
	if (id == 0) {
		alert('Veuillez choisir un commercial');
	} else {
		$.ajax({

			type : "POST",
			url : "inputsPerTimeCommercial",
			data : {
				id : id,
				From : From,
				To : To
			},
			dataType : 'json',
			success : function(response) {
				var inputWeek = [];
				// alert(response[0][5]);

				for (var i = 0; i < response[0].length; i++) {
					inputWeek[i] = {
						"date" : response[0][i],
						"column-1" : response[1][i]
					};
				}

				var inputWeekk = [];
				// alert(response[0][5]);

				for (var i = 0; i < response[3].length; i++) {
					inputWeekk[i] = {
						"date" : response[3][i],
						"column-1" : response[4][i]
					};
				}

				AmCharts.makeChart("chartdiv", {
					"type" : "serial",
					"categoryField" : "date",
					"dataDateFormat" : "YYYY-MM-DD",
					"categoryAxis" : {
						"parseDates" : true
					},
					"chartCursor" : {},
					"chartScrollbar" : {},
					"trendLines" : [],
					"graphs" : [ {
						"bullet" : "round",
						"id" : "AmGraph-1",
						"title" : response[2],
						"valueField" : "column-1"
					}  ],

					"guides" : [],
					"valueAxes" : [ {
						"id" : "ValueAxis-1",
						"title" : "Nombre d'entrées"
					} ],
					"allLabels" : [],
					"balloon" : {},
					"legend" : {
						"useGraphSettings" : true
					},
					"titles" : [],
					"dataProvider" : inputWeek
					 
				});
			},
			error : function(request, status, error) {
				console.log("Erreur");

			}

		});

	}

});

// debut

drc.click(function() {
	var From = $('#DRCFrom').val();
	var To = $('#DRCTo').val();
	if (From == 0) {
		alert("Veuillez choisir une date d\351but");
		return false;
	}
	if (To == 0 || To - From == "NaN") {
		alert("Veuillez choisir une date Fin convenable");
		return false;
	}

	$.ajax({
		type : "POST",
		url : "countActions",
		data : {
			From : From,
			To : To
		},
		dataType : 'json',
		success : function(response) {
			AmCharts.makeChart("chartdiv_3",

			{
				"type" : "funnel",
				"balloonText" : "[[title]]:<b>[[value]]</b>",
				"labelPosition" : "right",
				"rotate" : true,
				"marginLeft" : 15,
				"marginRight" : 160,
				"titleField" : "title",
				"valueField" : "value",
				"allLabels" : [],
				"balloon" : {},
				"titles" : [],

				"dataProvider" : [ {
					"title" : "Nombre de devis",
					"value" : response[0]
				}, {
					"title" : "Nombre de r\351alisations",
					"value" : response[1]
				}, {
					"title" : "Nombre de chutes",
					"value" : response[2]
				} ]

			});
		},
		error : function(request, status, error) {
			console.log("Erreur");

		}

	});
});

$.ajax({
	type : "POST",
	url : "countActions",
	dataType : 'json',
	success : function(response) {
		AmCharts.makeChart("chartdiv_3",

		{
			"type" : "funnel",
			"balloonText" : "[[title]]:<b>[[value]]</b>",
			"labelPosition" : "right",
			"rotate" : true,
			"marginLeft" : 15,
			"marginRight" : 160,
			"titleField" : "title",
			"valueField" : "value",
			"allLabels" : [],
			"balloon" : {},
			"titles" : [],

			"dataProvider" : [ {
				"title" : "Nombre de devis",
				"value" : response[0]
			}, {
				"title" : "Nombre de r\351alisations",
				"value" : response[1]
			}, {
				"title" : "Nombre de chutes",
				"value" : response[2]
			} ]

		});
	},
	error : function(request, status, error) {
		console.log("Erreur");

	}

});

		// fin

		clientVisit.click(function() {
			var From = $('#visitsFrom').val();
			var To = $('#visitsTo').val();
			if (From == 0) {
				alert("Veuillez choisir une date d\351but");
				return false;
			}
			if (To == 0 || To - From == "NaN") {
				alert("Veuillez choisir une date Fin convenable");
				return false;
			}

			$.ajax({
				type : "POST",
				url : "clientVisitPerDay",
				data : {
					From : From,
					To : To
				},
				dataType : 'json',
				success : function(response) {
					var visitWeek = [];
					for (var i = 0; i < response[0].length; i++) {
						visitWeek[i] = {
							"date" : response[0][i],
							"column-1" : response[1][i]
						};
					}

					AmCharts.makeChart("chartdiv_1", {
						"type" : "serial",
						"categoryField" : "date",
						"dataDateFormat" : "YYYY-MM-DD",
						"categoryAxis" : {
							"parseDates" : true
						},
						"chartCursor" : {},
						"chartScrollbar" : {},
						"trendLines" : [],
						"graphs" : [ {
							"fillAlphas" : 0.7,
							"id" : "AmGraph-1",
							"lineAlpha" : 0,
							"title" : "Jours",
							"valueField" : "column-1"
						} ],
						"guides" : [],
						"valueAxes" : [ {
							"id" : "ValueAxis-1",
							"title" : "Nombre clients"
						} ],
						"allLabels" : [],
						"balloon" : {},
						"legend" : {},
						"titles" : [],
						"dataProvider" : visitWeek
					});
				},
				error : function(request, status, error) {
					console.log("Erreur");

				}

			});

		}),

		quotaDR
				.click(function() {
					var From = $('#QuotaDRFrom').val();
					var To = $('#QuotaDRTo').val();
					if (From == 0) {
						alert("Veuillez choisir une date d\351but");
						return false;
					}
					if (To == 0 || To - From == "NaN") {
						alert("Veuillez choisir une date Fin convenable");
						return false;
					}

					$
							.ajax({
								type : "POST",
								url : "quotaDevisRealisation",
								data : {
									From : From,
									To : To
								},
								dataType : 'json',
								success : function(response) {
									var title = 'Entre ' + From + ' et ' + To;

									AmCharts
											.makeChart(
													"chartdiv_4",
													{
														"type" : "pie",
														"balloonText" : "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>",
														"titleField" : "category",
														"valueField" : "column-1",
														"allLabels" : [],
														"balloon" : {},
														"legend" : {
															"align" : "center",
															"markerType" : "circle"
														},
														"titles" : [ {
															"id" : "Title-1",
															"size" : 15,
															"text" : title
														} ],
														"dataProvider" : [
																{
																	"category" : "Devis",
																	"column-1" : response[0]
																},
																{
																	"category" : "R\351alisations",
																	"column-1" : response[1]
																} ]
													});
								},
								error : function(request, status, error) {
									console.log("Erreur");

								}

							});

				}),

		modifData.click(function() {
			var columns = [];
			var From = $('#ModifDataFrom').val();
			var To = $('#ModifDataTo').val();
			if (From == 0) {
				alert("Veuillez choisir une date d\351but");
				return false;
			}
			if (To == 0 || To - From == "NaN") {
				alert("Veuillez choisir une date Fin convenable");
				return false;
			}

			$.ajax({
				type : "POST",
				url : "recordUpdateInOldData",
				data : {
					From : From,
					To : To
				},
				dataType : 'json',
				success : function(response) {
					for (var i = 0; i < response[0].length; i++) {
						columns[i] = {
							"date" : response[0][i],
							"column-1" : response[1][i]
						};
					}
					AmCharts.makeChart("chartdiv_6", {
						"type" : "serial",
						"categoryField" : "date",
						"dataDateFormat" : "YYYY-MM-DD",
						"categoryAxis" : {
							"parseDates" : true
						},
						"chartCursor" : {},
						"chartScrollbar" : {},
						"trendLines" : [],
						"graphs" : [ {
							"balloonColor" : "#FFFF00",
							"bullet" : "round",
							"color" : "#C0FBA7",
							"columnWidth" : 0.49,
							"cornerRadiusTop" : 2,
							"fillAlphas" : 0.58,
							"fillColors" : "#004614",
							"gradientOrientation" : "horizontal",
							"id" : "AmGraph-1",
							"lineColor" : "#B5CF78",
							"negativeFillAlphas" : 0,
							"negativeLineAlpha" : 0,
							"title" : "Nombre Modifications/jour",
							"topRadius" : 0,
							"type" : "column",
							"valueField" : "column-1"
						} ],
						"guides" : [],
						"valueAxes" : [ {
							"id" : "ValueAxis-1",
							"title" : "Axis title"
						} ],
						"allLabels" : [],
						"balloon" : {},
						"legend" : {
							"useGraphSettings" : true
						},
						"titles" : [],
						"dataProvider" : columns
					});

				},
				error : function(request, status, error) {
					console.log("Erreur");

				}

			});
		})
