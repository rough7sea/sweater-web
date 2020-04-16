<a class="btn btn-primary" data-toggle="collapse"
   href="#collapseExample" role="button"
   aria-expanded="false" aria-controls="collapseExample">
    Message Edit
</a>

<div class="collapse <#if message??>show</#if>" id="collapseExample">
    <div class="form-group">
        <form method="post" action="/main" enctype="multipart/form-data">
            <div class="form-group mt-3">
                <input type="text" name="tag" class="form-control" placeholder="Тэг"
                       value="<#if message??>${message.tag}</#if>">
                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" name="text" value="<#if message??>${message.text}</#if>"
                       class="form-control ${(textError??)?string('is-invalid', '')}"
                       placeholder="Введите сообщение" />
                <#if textError??>
                    <div class="invalid-feedback">
                        ${textError}
                    </div>
                </#if>
            </div>
            <div class="custom-file mb-3">
                <input type="file" name="file" id="customFile">
                <label class="custom-file-label" for="customFile">Choose file</label>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}" />
            <input type="hidden" name="id" value="<#if message??> ${message.id}</#if>" />
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Save message</button>
            </div>
        </form>
    </div>
</div>