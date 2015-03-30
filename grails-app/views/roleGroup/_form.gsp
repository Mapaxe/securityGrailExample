<%@ page import="com.mapaxe.security.RoleGroup" %>



<div class="fieldcontain ${hasErrors(bean: roleGroupInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="roleGroup.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${roleGroupInstance?.name}"/>

</div>

