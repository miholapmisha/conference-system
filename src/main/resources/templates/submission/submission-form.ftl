<!doctype html>
<html lang="en">
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
    <form role="form" action="/submission/submit" method="post" class="mx-5 w-50 my-lg-5" enctype="multipart/form-data">
        <div class="mb-5"><a href="/submission">All your submissions</a></div>
        <h2 class="text-danger">Your submission</h2>
        <p>Enter information about your submission. Submissions must be registered by ${deadline}</p>
        <div data-mdb-input-init class="form-outline mb-4">
            <label class="text-primary form-label" for="title">Title</label>
            <input name="title" type="text" id="title" class="form-control"/>
        </div>

        <div data-mdb-input-init class="form-outline mb-4" style="width: 300px">
            <label class="text-primary form-label" for="paper">Submission (PDF, max 600MB)</label>
            <input name="paper" type="file" id="paper" class="form-control"/>
        </div>

        <div data-mdb-input-init class="form-outline mb-4">
            <label class="text-primary form-label" for="abstractOfSubmission">Abstract</label>
            <textarea name="abstractOfSubmission" id="abstractOfSubmission" class="form-control w-100"
                      style="height: 200px"></textarea>
        </div>

        <h4 class="text-primary form-label" for="form2Example1">Authors</h4>
        <p>List the authors, including email addresses and affiliations. Submission is anonymous, so reviewers will not
            see author information.</p>
        <div data-mdb-input-init class="form-outline mb-4 d-flex flex-column">
            <div class="d-flex mt-2">
                <label class="align-self-center" for="authorsEmails1">1.</label>
                <input name="authorsEmails" type="email" id="authorsEmails1" class="mx-2 w-25 form-control"/>
            </div>
            <div class="d-flex mt-2">
                <label class="align-self-center" for="authorsEmails2">2.</label>
                <input name="authorsEmails" type="email" id="authorsEmails2" class="mx-2 w-25 form-control"/>
            </div>

            <div class="d-flex mt-2">
                <label class="align-self-center" for="authorsEmails3">3.</label>
                <input name="authorsEmails" type="email" id="authorsEmails3" class="mx-2 w-25 form-control"/>
            </div>
            <div class="d-flex mt-2">
                <label class="align-self-center" for="authorsEmails4">4.</label>
                <input name="authorsEmails" type="email" id="authorsEmails4" class="mx-2 w-25 form-control"/>
            </div>
            <div class="d-flex mt-2">
                <label class="align-self-center" for="authorsEmails5">5.</label>
                <input name="authorsEmails" type="email" id="authorsEmails5" class="mx-2 w-25 form-control"/>
            </div>
        </div>

        <div data-mdb-input-init class="form-outline mb-4">
            <label class="text-primary form-label" for="form2Example1">Topics</label>
            <div style="max-height: 200px" class="d-flex flex-column flex-wrap">
                <#list topics as topic>
                    <div class="my-2 mx-2">
                        <input name="topicsIds" value="${topic.id}" type="checkbox" id="${topic.id}">
                        <label for="${topic.id}">${topic.name}</label>
                    </div>
                </#list>
            </div>
        </div>

        <div data-mdb-input-init class="form-outline mb-4">
            <label class="text-primary form-label" for="form2Example1">PC Conflicts</label>
            <p>Select the PC members who have conflicts of interest with this submission. This includes past advisors
                and students, people with the same affiliation, and any recent (~2 years) coauthors and
                collaborators.</p>
            <div style="max-height: 200px" class="d-flex flex-column flex-wrap">
                <#list program_committees as program_committee>
                    <div class="my-2 mx-2">
                        <input name="pcConflictsIds" value="${program_committee.id}" type="checkbox"
                               id="${program_committee.id}"
                               data-id="${program_committee.id}">
                        <label for="${program_committee.id}">${program_committee.firstName} ${program_committee.secondName}</label>
                        <p style="font-size: 12px">${program_committee.affiliation}</p>
                    </div>
                </#list>
            </div>
        </div>

        <p class="text-danger">! You must fill out all required fields to mark the submission as ready for review.</p>

        <#if errors??>
            <div style="max-width: 600px;"
                 class="mb-4 p-3 text-primary-emphasis bg-primary-subtle border border-primary-subtle rounded-3">
                <#list errors as error>
                    <p> ${error} </p>
                </#list>
            </div>
        </#if>

        <button type="submit" data-mdb-button-init data-mdb-ripple-init class="btn btn-primary btn-block mb-4"
                style="width: 100px; height: 50px">
            Submit
        </button>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>