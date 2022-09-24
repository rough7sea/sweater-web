<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as form>

<@c.page>

    <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
            <li class="breadcrumb-item active"
                aria-current="page">
                Registration Page
            </li>
        </ol>
    </nav>

    <#if message??>
        <div class="alert alert-danger">
            ${message}
        </div>
    </#if>
    <@form.login "/registration" true/>

</@c.page>