<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as form>

<@c.page>
    ${message!}
    <@form.login "/login" false/>
</@c.page>