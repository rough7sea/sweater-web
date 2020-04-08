<#import "parts/common.ftl" as c>

<@c.page>
    <div class="form-row">
        <div class="form-group com-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="filter" class="form-control" value="${filter!}" placeholder="Search">
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false" aria-controls="collapseExample">
        Add new message
    </a>

    <div class="collapse" id="collapseExample">
        <div class="form-group">
            <form method="post" action="/main" enctype="multipart/form-data">
                <div class="form-group mt-3">
                    <input type="text" name="tag" class="form-control" placeholder="Тэг">
                </div>
                <div class="form-group">
                    <input type="text" name="text" class="form-control" placeholder="Введите сообщение" />
                </div>
                <div class="custom-file mb-3">
                    <input type="file" name="file" id="customFile">
                    <label class="custom-file-label" for="customFile">Choose file</label>
                </div>

                <input type="hidden" name="_csrf" value="${_csrf.token}" />
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">>Добавить</button>
                </div>
            </form>
        </div>
    </div>


    <div class="card-columns">
        <#list messages as message>
            <div class="card my-3">

                <#if message.filename??>
                    <img src="/img/${message.filename}" alt="" class="card-img-top">
                </#if>
                <div class="m-2">
                    <span>${message.text}</span>
                    <i>${message.tag}</i>
                </div>
                <div class="card-footer text-muted">
                    ${message.authorName}
                </div>
            </div>
        <#else>
            No Messages
        </#list>
    </div>

</@c.page>