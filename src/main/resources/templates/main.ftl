<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as logout>

<@c.page>

    <@logout.logout/>
    <span><a href="/user">User List</a></span>

    <div>
        <form method="post" action="/main" enctype="multipart/form-data">
            <input type="text" name="text" placeholder="Введите сообщение" />
            <input type="text" name="tag" placeholder="Тэг">
            <input type="file" name="file">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <button type="submit">Добавить</button>
        </form>
    </div>

    <div>Список сообщений</div>
    <form method="get" action="/main">
        <input type="text" name="filter" value="${filter!}">
<#--        <input type="hidden" name="_csrf" value="${_csrf.token}" />-->
        <button type="submit">Найти</button>
    </form>

    <#list messages as message>
        <div>
            <b>${message.id}</b>
            <span>${message.text}</span>
            <i>${message.tag}</i>
            <strong>${message.authorName}</strong>
            <div>
                <#if message.filename??>
                    <img src="/img/${message.filename}" alt="">
                </#if>
            </div>
        </div>
    <#else>
        No Messages
    </#list>


</@c.page>