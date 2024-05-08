<!doctype html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<div class="w-100 h-100">
    <div class="mx-5 w-50 my-lg-5">
        <h2 class="text-danger">${submission.title}</h2>

        <div data-mdb-input-init class="form-outline mb-4" style="width: 300px">
            <#if role == 'ADMIN' || role == 'PROGRAM_COMMITTEE'>
                <a href="/review">Back to reviews</a>
            <#else>
                <a href="/submission">Back to submissions</a>
            </#if>
        </div>

        <div data-mdb-input-init class="form-outline mb-4" style="width: 300px">
            <a href="/paper/submission/${submission.fileDestination}">File</a>
        </div>

        <div data-mdb-input-init class="form-outline mb-4">
            <p class="text-primary form-label">Abstract</p>
            <div class="form-control w-100" style="max-height: 200px">${submission.abstractOfSubmission}</div>
        </div>

        <div data-mdb-input-init class="mb-4">
            <p class="text-primary">Topics</p>
            <div style="max-height: 200px" class="d-flex flex-column flex-wrap">
                <#list topics as topic>
                    <div class="my-2 mx-2">
                        <p>${topic.name}</p>
                    </div>
                </#list>
            </div>
        </div>

        <#if role == "ADMIN">
            <div data-mdb-input-init class="mb-4">
                <p style="font-size: 18px" class="text-primary">Authors</p>
                <#list authors as author>
                    <div class="my-2 mx-2">
                        <span class="border-bottom border-primary">${author.firstName} ${author.secondName} (${author.email})</span>
                    </div>
                </#list>
            </div>

            <div data-mdb-input-init class="mb-4">
                <p style="font-size: 18px" class="text-primary">PC conflicts</p>
                <#list pcConflicts as pcConflicts>
                    <div class="my-2 mx-2">
                        <span class="border-bottom border-primary fw-bold">${pcConflicts.firstName} ${pcConflicts.secondName} (${pcConflicts.email})</span>
                    </div>
                </#list>
            </div>

            <button type="button" onclick="openModal()" class="btn btn-primary" data-toggle="modal">
                Assign
            </button>
        </#if>

    </div>

</div>
<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog"
     aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div style="width: 600px;" class="modal-dialog" role="document">
        <form action="/submission/assign" method="post">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">Assignment</h5>
                </div>
                <div class="modal-body">
                    <div style="max-height: 400px;  overflow: auto">
                        <div class="input-group-prepend">
                            <label class="input-group-text" for="inputGroupSelect01">Program committees</label>
                        </div>
                        <input name="submissionId" type="hidden" value="${submissionId}">
                        <ul class="list-group">
                            <#list accounts?keys as key>
                                <li class="list-group-item">
                                    <input name="programCommitteesIds" ${accounts?values[key_index]?then('checked','')} id="${key.id}"
                                                                                            type="checkbox"
                                                                                            value="${key.id}">
                                    <label for="${key.id}">
                                        ${key.email} - (${key.firstName} ${key.secondName})
                                    </label>
                                </li>
                            </#list>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" onclick="closeModal()" data-dismiss="modal">Close
                    </button>
                    <button type="submit" class="btn btn-primary">Save changes</button>
                </div>

            </div>
        </form>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous">
</script>

<script>
    function openModal() {
        $('#exampleModal').modal('show');
    }

    function closeModal() {
        $('#exampleModal').modal('hide');
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>