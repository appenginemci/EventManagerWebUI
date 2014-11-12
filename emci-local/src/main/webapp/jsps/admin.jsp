<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>

<!DOCTYPE html>
<html ng-app="myApp" ng-app lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">

<title>MCI Event Manager</title>
<style type="text/css"> .ui-helper-hidden-accessible{display:none} </style>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>

<link href="/resources/styles/bootstrap.min.css" rel="stylesheet"></link>
<style type="text/css">
    ul>li, a{cursor: pointer;}
    input.ng-invalid {
    border-color: #FA787E;
  }

  input.ng-valid {
    border-color: #78FA89;
  }
  
  .blue {
  	color: #2a6496;
  }
</style>

<script src="/resources/js/angular.min.js"></script>
<script src="/resources/js/jquery-1.11.1.min.js"></script>
<script src="/resources/js/admin.js"></script>        
<script type="text/javascript" src="/resources/js/ui-bootstrap-tpls-0.11.0.min.js"></script>

</head>

<body ng-controller="listCtrl">
<nav class="navbar navbar-inverse navbar-static-top" role="navigation">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<a class="navbar-brand" href="#">MCI Event Manager</a>
			</div>

			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown"><c:out value="${session.email}" escapeXml="false"/> <span class="caret"></span></a>
					</li>
				</ul>
			</div>
			<!-- /.navbar-collapse -->
  		</div><!-- /.container-fluid -->
	</nav>
	
	
	<div id="tables">
		
			<button type="button" class="btn btn-primary" ng-click="eventViewMode = !eventViewMode">&nbsp;Toggle User / Event view</button>
			Filter: <input ng-model="query">
			<div ng-show="eventViewMode">
			<table class="table table-condensed">
				<thead>
					<tr class="border">
						<th>Event</th>
						<th>User</th>
						<th>New</th>
						<th>In Progress</th>
						<th>For Approval</th>
					</tr>			
				</thead>
				<tbody>
					<!-- <tr ng-repeat="row in events | orderBy:'name' | groupBy:['name']" >
						<td ng-show="row.group_by_CHANGED">{{row.name}}</td>
						<td ng-hide="row.group_by_CHANGED">&nbsp;</td> -->
     				<tr ng-repeat="row in events | filter:query" >
						
						<td>{{row.name}}</td>
						<td>{{row.user}}</td>
						<td>
							<span ng-hide="row.incoming.folderLink == ''"><a href="{{row.incoming.folderLink}}" target="_blank">{{row.incoming.number}}</a></span>
							<span ng-show="row.incoming.folderLink == ''">{{row.incoming.number}}</span>
						</td>
						<td>
							<span ng-hide="row.in_progress.folderLink == ''"><a href="{{row.in_progress.folderLink}}" target="_blank">{{row.in_progress.number}}</a></span>
							<span ng-show="row.in_progress.folderLink == ''">{{row.in_progress.number}}</span>
						</td>
						<td>
							<span ng-hide="row.validation_ask.folderLink == ''"><a href="{{row.validation_ask.folderLink}}" target="_blank">{{row.validation_ask.number}}</a></span>
							<span ng-show="row.validation_ask.folderLink == ''">{{row.validation_ask.number}}</span>
						</td>
					</tr>
				</tbody>
			</table>
			</div>
			<div ng-hide="eventViewMode">
			<table class="table table-condensed">
				<thead>
					<tr class="border">
						<th>User</th>
						<th>Event</th>
						<th>New</th>
						<th>In Progress</th>
						<th>For Approval</th>
					</tr>			
				</thead>
				<tbody>
					<!-- <tr ng-repeat="userrow in events | orderBy:'user' | groupBy:['user']" >
						<td ng-show="userrow.group_by_CHANGED">{{userrow.user}}</td>
						<td ng-hide="userrow.group_by_CHANGED">&nbsp;</td> -->
					<tr ng-repeat="userrow in users | filter:query" >
						<td>{{userrow.user}}</td>
						<td>{{userrow.name}}</td>
						<td>
							<span ng-hide="userrow.incoming.folderLink == ''"><a href="{{userrow.incoming.folderLink}}" target="_blank">{{userrow.incoming.number}}</a></span>
							<span ng-show="userrow.incoming.folderLink == ''">{{userrow.incoming.number}}</span>
						</td>
						<td>
							<span ng-hide="userrow.in_progress.folderLink == ''"><a href="{{userrow.in_progress.folderLink}}" target="_blank">{{userrow.in_progress.number}}</a></span>
							<span ng-show="userrow.in_progress.folderLink == ''">{{userrow.in_progress.number}}</span>
						</td>
						<td>
							<span ng-hide="userrow.validation_ask.folderLink == ''"><a href="{{userrow.validation_ask.folderLink}}" target="_blank">{{userrow.validation_ask.number}}</a></span>
							<span ng-show="userrow.validation_ask.folderLink == ''">{{userrow.validation_ask.number}}</span>
						</td>
					</tr>
				</tbody>
			</table>
			</div>
		</div>

	
<!-- 

<div class="container">
	<div>
  		<alert ng-repeat="alert in alerts" type="{{alert.type}}" close="closeAlert()">{{alert.msg}}</alert>
	</div>

	<div class="form-group">
		<label for="typeSelection">Select a datasource type</label>
		<select id="typeSelection" ng-model="selectedType" ng-change="getDatasourcesForType(selectedType)" ng-options="type for type in types" class="form-control">
		</select>
	</div>

	 <div class="row">
		<div class="col-md-12" ng-show="optionalCols.length > 0">
			
			<form name="addForm" class="css-form" novalidate>
			<table class="table table-striped table-bordered">
			<thead>
				<tr>		
					<th><button type="button" class="btn btn-primary" ng-click="addMode = !addMode"><i class="glyphicon glyphicon-plus">&nbsp;Add datasource</i></button></th>
					<th ng-repeat="column in mandatoryCols">
						<a href="" ng-click="reverse=!reverse;order(column, reverse)">{{column}}</a>
						<span class="glyphicon glyphicon-arrow-down blue" ng-if="sortColDirection.col == column && sortColDirection.desc"></span>
						<span class="glyphicon glyphicon-arrow-up blue" ng-if="sortColDirection.col == column && !sortColDirection.desc"></span>
					</th>
					<th ng-repeat="column in optionalCols">
						<a href="" ng-click="reverse=!reverse;order(column, reverse)">{{column}}</a>
						<span class="glyphicon glyphicon-arrow-down blue" ng-if="sortColDirection.col == column && sortColDirection.desc"></span>
						<span class="glyphicon glyphicon-arrow-up blue" ng-if="sortColDirection.col == column && !sortColDirection.desc"></span>
					</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-show="addMode" ng-init="addMode = false">
					<td>
						<div class="btn-group ng-scope">
							<button class="btn btn-default" ng-click="addDatasource();" ng-disabled="addForm.$invalid"><span class="glyphicon glyphicon-save"></span></button>
							<button class="btn btn-default" ng-click="cancelAddDatasource()"><span class="glyphicon glyphicon-remove" ></span></button>
						</div>
					</td>
					<td ng-repeat="col1 in mandatoryCols">
						<input ng-if="col1 == 'id'" type="text" ng-model='lineValues[col1]' class="form-control" disabled/>
						<input ng-if="col1 != 'id'" type="text" ng-model='lineValues[col1]' class="form-control" required/>
					</td>
					<td ng-repeat="col2 in optionalCols">
						<input type="text" ng-model='lineValues[col2]' class="form-control" required/>
					</td>
				</tr>
				<tr ng-repeat="table in datasources" ng-init="table.editMode=false">
					<td>
						<div class="btn-group ng-scope">
							<button class="btn btn-default" ng-click="table.editMode = !table.editMode" ng-hide="table.editMode"><span class="glyphicon glyphicon-edit" ></span></button>
							<button class="btn btn-default" ng-click="removeDatasource(table,$index)" ng-hide="table.editMode"><span class="glyphicon glyphicon-trash" ></span></button>
						</div>
						<div class="btn-group ng-scope">
							<button class="btn btn-default" ng-click="updateDatasource(table)" ng-show="table.editMode"><span class="glyphicon glyphicon-save" ></span></button>
							<button class="btn btn-default" ng-click="cancelUpdateDatasource(table,$index)" ng-show="table.editMode"><span class="glyphicon glyphicon-remove" ></span></button>
						</div>
					</td>
					<td ng-repeat="col1 in mandatoryCols">
						{{table[col1]}}
					</td>
					<td ng-repeat="col2 in optionalCols">
						<input type="text" ng-model='table[col2]' ng-show="table.editMode" ng-value="table[col2]" class="form-control" required/>
						<span ng-hide="table.editMode">{{table[col2]}}</span>
					</td>
				</tr>
			</tbody>
			</table>
			</form>
		</div>
		
		<div class="col-md-12" ng-show="datasources.length == 0">
			<div class="col-md-12">
				<h4>No datasource found for the selected type</h4>
			</div>
		</div>
		
	</div>
</div>
 -->
</body>
</html>