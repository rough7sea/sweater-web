<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as form>

<@c.page>
    Login page
    <@form.login "/login"/>
    <a href="/registration">Add new user</a>
</@c.page>