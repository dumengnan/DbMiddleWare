<%--
  Created by IntelliJ IDEA.
  User: mee
  Date: 15-12-28
  Time: 下午6:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>DbMiddleware</title>
    <script type="text/javascript" src="./jquery.js"></script>
    <script type="text/javascript">
        $(function () {

            $("#selectsubmit").click(function () {

                var inputString = $("input[name='inputSentence']").val();    //获取输入的查询语句

                if (inputString == null) {   //如果用户输入为空,不执行任何操作
                    alert("please input the SQL statement");
                    return;
                }

                var tempdata = {searchString: inputString};

                var searchJson = JSON.stringify(tempdata);

                $.ajax({
                    type: "POST",
                    datatype: "json",
                    data: searchJson,
                    url: "search",
                    contentType: "application/json",
                    success: function (result) {

                        console.log(result);

                        //window.location.href="result.jsp";
                        var parse_result = JSON.stringify(result);

                        console.log(parse_result);

                        $("#searchBox").hide();
                        document.getElementById("showData").style.visibility='visible';
                        document.getElementById("resultData").innerText = parse_result;

                        console.log(parse_result["searchString"]);
                    }
                });
            });

            $("#insertsubmit").click(function () {

                var inputString = $("input[name='inputSentence']").val();    //获取输入的查询语句

                if (inputString == null) {   //如果用户输入为空,不执行任何操作
                    alert("please input the SQL statement");
                    return;
                }

                var tempdata = {updateString: inputString};

                var updateJson = JSON.stringify(tempdata);

                $.ajax({
                    type: "POST",
                    datatype: "json",
                    data: updateJson,
                    url: "update",
                    contentType: "application/json",
                    success: function (result) {
                        $("#searchBox").hide();
                        var parse_result = JSON.stringify(result);

                        document.getElementById("showData").style.visibility='visible';
                        document.getElementById("resultData").innerText = parse_result;

                    }
                });
            });
        });
    </script>
</head>
<body>
<div id="searchBox">
    <form action="/search" method="post" style="padding-top: 200px;width:100%;text-align: center">
        <p style="font-family: Arial;font-size: 25px">DbMiddleware</p>
        <input type="text" name="inputSentence" style="width:450px;height: 30px"/></br>
        <div style="padding-top: 20px;margin-left: auto;margin-right: auto">
            <input type="button" id="selectsubmit" value="查询数据" style="width:80px;height:30px;margin-right: 10px;font-family: Arial, sans-serif;"/>
            <input type="button" id="insertsubmit" value="更新数据" style="width:80px;height:30px;margin-left: 10px;font-family: Arial, sans-serif;"/>
        </div>
    </form>
</div>
<div id="showData" style="visibility: hidden">
    <p id="resultData" style="width: 100%;text-align: center;padding-top: 200px;">

    </p>
</div>
</body>
</html>
