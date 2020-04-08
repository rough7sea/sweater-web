<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as form>

<@c.page>
    <div class="mb-1">
        Registration Page
    </div>
    <#if message??>
        ${message}
    </#if>
    <@form.login "/registration" true/>

</@c.page>