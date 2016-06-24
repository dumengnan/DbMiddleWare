<%--
  Created by IntelliJ IDEA.
  User: mee
  Date: 16-3-4
  Time: 上午10:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ModifySource</title>
    <script type="text/javascript" src="./jquery.js"></script>
    <script type="text/javascript">
      $(function () {

        $(document).ready(function () {
          $.ajax({
            type: "GET",
            datatype: "text",
            url: "modifysource?type=getsource",
            contentType: "text/xml",
            success: function (result) {
              drawTable(result);
            }
          });
        });

        $("#AddDatasource").click(function () {

          var row = $("<tr />");

          $("#dataSourceTable").append(row);
          row.append($("<td contenteditable='true'>" + " " + "</td />"));
          row.append($("<td contenteditable='true'>" + " " + "</td />"));
          row.append($("<td contenteditable='true'>" + " " + "</td />"));
          row.append($("<td contenteditable='true'>" + " " + "</td />"));
          row.append($("<td contenteditable='true'>" + " " + "</td />"));
          row.append($("<td contenteditable='true'>" + " " + "</td />"));
          row.append($("<td contenteditable='true'>" + " " + "</td />"));
          row.append($("<td contenteditable='true'>" + " " + "</td />"));
        });

        $("#SubmitData").click(function(){


          var table = document.getElementById("dataSourceTable");

          var lastRow = table.rows[table.rows.length-1];

          var type = lastRow.cells[0].innerText;
          var name = lastRow.cells[1].innerText;
          var driver = lastRow.cells[2].innerText;
          var url = lastRow.cells[3].innerText;
          var username = lastRow.cells[4].innerText;
          var password = lastRow.cells[5].innerText;
          var maxconn = lastRow.cells[6].innerText;
          var minconn = lastRow.cells[7].innerText;


          var sourceData = {type:type,name:name,driver:driver,
                              url:url,username:username,password:password,
                              maxconn:maxconn,minconn:minconn};

          var sourceData = JSON.stringify(sourceData);

          $.ajax({
            type: "POST",
            data:sourceData,
            datatype: "json",
            url: "modifysource?type=submitdata",
            contentType: "application/json",
            success: function (result) {
              window.alert("Submit Success!");
            }
          });
        });

        function drawTable(data) {
          for (var i = 0; i < data.length; i++) {
            drawRow(data[i]);
          }
        }

        function drawRow(rowData) {
          var row = $("<tr />");

          $("#dataSourceTable").append(row);
          row.append($("<td >" + rowData.type + "</td />"));
          row.append($("<td >" + rowData.name + "</td />"));
          row.append($("<td >" + rowData.driver + "</td />"));
          row.append($("<td >" + rowData.url + "</td />"));
          row.append($("<td >" + rowData.username + "</td />"));
          row.append($("<td >" + rowData.password + "</td />"));
          row.append($("<td >" + rowData.maxconn + "</td />"));
          row.append($("<td >" + rowData.minconn + "</td />"));
        }
      });
    </script>

    <style type="text/css">
      table {
        border: 1px solid #666;
        width: 100%;
      }
      th {
        background: #f8f8f8;
        font-weight: bold;
        padding: 2px;
      }
    </style>
</head>
<body>

  <table id="dataSourceTable">
    <tr>
      <th>Type</th>
      <th>Name</th>
      <th>Driver</th>
      <th>Url</th>
      <th>Username</th>
      <th>Password</th>
      <th>MaxConn</th>
      <th>MinConn</th>
    </tr>
  </table>
  <div style="padding-top: 20px;margin-left: auto;margin-right: auto">
    <input type="button" id="AddDatasource" value="增加数据源" style="width:80px;height:30px;margin-right: 10px;font-family: Arial, sans-serif;"/>
    <input type="button" id="SubmitData" value="提交" style="width:80px;height:30px;margin-right: 10px;font-family: Arial, sans-serif;"/>
  </div>

</body>
</html>
