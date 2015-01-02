<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8">
		<link rel="shortcut icon" type="image/ico" href="http://www.sprymedia.co.uk/media/images/favicon.ico">
		
		<title>FixedHeader example</title>
		<style type="text/css" title="currentStyle">
			@import "css/demo_page.css";
			@import "css/demo_table.css";
			.FixedHeader_Cloned th {
				background-color: white;
			}
			th, td {
				height: 30px;
			}
			.left_cell {
				background-color: white  !important;
				border-right: 1px solid black  !important;
				text-align: center;
			}
			.right_cell {
				background-color: white !important;
				border-left: 1px solid black;
				text-align: center;
			}
			#info {
				position: absolute;
				top: 100px;
				left: 100px;
				width: 300px;
				background-color: white;
				border: 1px solid blue;
				z-index: 50;
				padding: 20px;
			}
		</style>
		<script type="text/javascript" charset="utf-8" src="js/jquery.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/jquery.dataTables.js"></script>
            <script type="text/javascript" charset="utf-8" src="js/ColVis.js"></script>
		<script type="text/javascript" charset="utf-8" src="js/FixedHeader.js"></script>
	
	</head>
	<body id="dt_example">      
		<div style="width:2000px; height: 2000px">
<table cellpadding="0" cellspacing="0" border="0" class="display" id="example">
	<thead>
		<tr>
			<th></th>
			<th>Rendering engine</th>
			<th>Browser</th>
			<th>Platform(s)</th>
			<th>Engine version</th>
			<th>CSS grade</th>
			<th></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="left_cell"> </td>
			<td>Trident</td>
			<td>Internet
				 Explorer 4.0</td>
			<td>Win 95+</td>
			<td class="center">4</td>
			<td class="center">X</td>
			<td class="right_cell"> </td>
		</tr>
		<tr>
			<td class="left_cell"> </td>
			<td>Trident</td>
			<td>Internet
				 Explorer 5.0</td>
			<td>Win 95+</td>
			<td class="center">5</td>
			<td class="center">C</td>
			<td class="right_cell"> </td>
		</tr>
		<tr >
			<td class="left_cell"> </td>
			<td>Trident</td>
			<td>Internet
				 Explorer 5.5</td>
			<td>Win 95+</td>
			<td class="center">5.5</td>
			<td class="center">A</td>
			<td class="right_cell"> </td>
		</tr>
		<tr >
			<td > </td>
			<td>Trident</td>
			<td>Internet
				 Explorer 6</td>
			<td>Win 98+</td>
			<td class="center">6</td>
			<td class="center">A</td>
			<td class="right_cell"> </td>
		</tr>
		<tr >
			<td class="left_cell"> </td>
			<td>Trident</td>
			<td>Internet Explorer 7</td>
			<td>Win XP SP2+</td>
			<td class="center">7</td>
			<td class="center">A</td>
			<td class="right_cell"> </td>
		</tr>
		<tr>
			<td class="left_cell"> </td>
			<td>Trident</td>
			<td>AOL browser (AOL desktop)</td>
			<td>Win XP</td>
			<td class="center">6</td>
			<td class="center">A</td>
			<td class="right_cell"> </td>
		</tr>
	
	
	</tbody>
	<tfoot>
		<tr>
			<th></th>
			<th>Rendering engine</th>
			<th>Browser</th>
			<th>Platform(s)</th>
			<th>Engine version</th>
			<th>CSS grade</th>
			<th></th>
		</tr>
	</tfoot>
</table>
		</div>
            <script type="text/javascript" charset="utf-8">
			$(document).ready( function () {
				var oTable = $('#example').dataTable( { 
					"iDisplayLength": -1,
					"bFilter": false,
					"bInfo": false,
					"bPaginate": false,
					"bLengthChange": false,
					"fnDrawCallback": function ( oSettings ) {
						for ( var i=0, iLen=oSettings.aiDisplay.length ; i<iLen ; i++ )
						{
							$('td:eq(0), td:eq(6)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html( i+1 );
						}
					},
					"aoColumns": [
						{"bSortable": false, "sWidth": "20px" },
						null, null, null, null, null, 
						{"bSortable": false, "sWidth": "20px" }
					]
				} );
				new FixedHeader( oTable, { "left": true, "right": true, "bottom": true } );
			} );
		</script>
	</body>
</html>