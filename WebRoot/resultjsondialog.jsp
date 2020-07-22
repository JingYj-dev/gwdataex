<%@ page language="java" pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="ww" uri="webwork" %>
<script>
(function(){
	var data = <ww:property value="result"/>;
	$css.closeDialog();
	$css.alert(data.msg);
})();
</script>