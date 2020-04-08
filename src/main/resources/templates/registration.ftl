<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as form>

<@c.page>
Registration Page
    <#if message??>
        ${message}
    </#if>
    <@form.login "/registration"/>

</@c.page>