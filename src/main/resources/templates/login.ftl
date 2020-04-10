<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as form>

<@c.page>

<#--    <#if Session?? && Session.SPRING_SECURITY_LAST_EXCEPTION??>-->
<#--        <div class="alert alert-danger" role="alert">-->
<#--            ${SPRING_SECURITY_LAST_EXCEPTION.message}-->
<#--        </div>-->
<#--    </#if>-->

    <#if message??>
        <div class="alert alert-${messageType}" role="alert">
            ${message}
        </div>
    </#if>

    <@form.login "/login" false/>
</@c.page>