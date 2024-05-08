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
<div style="height: 100px" class="bg-primary text-white d-flex justify-content-center">

    <#if role == "ADMIN">
        <div class="align-self-center mx-2">
            <a class="btn btn-danger" href="/account/list/all">Users</a>
        </div>
    <#else>
        <div class="align-self-center mx-2">
            <a class="btn btn-danger" href="/account/list/program-committee">Program committees</a>
        </div>
    </#if>

    <div class="align-self-center mx-2">
        <a class="btn btn-danger" href="/submission">Submissions</a>
    </div>
    <#if role == "ADMIN" || role == "PROGRAM_COMMITTEE">

        <div class="align-self-center mx-2">
            <a class="btn btn-danger" href="/review">Reviews</a>
        </div>
        <div class="align-self-center mx-2">
            <a class="btn btn-danger" href="/submission/assigned">Assigns</a>
        </div>
    </#if>
</div>
<section class="mt-5 intro">
    <div class="gradient-custom-1 h-100">
        <div class="mask d-flex align-items-center h-100">
            <div class="container">
                <div>
                    <h2 class="text-danger mb-5">Your assigns</h2>

                    <div><p>Below displayed submissions that was assigned by PC chairs and not reviewed yet</p></div>

                    <div class="row justify-content-center">
                        <div class="col-12">
                            <div class="table-responsive bg-white">
                                <table class="table mb-0" style="width: 700px;">
                                    <thead>
                                    <tr>
                                        <th scope="col">ID</th>
                                        <th scope="col">Name</th>
                                        <th scope="col"></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#list submissions as submission>
                                        <tr>
                                            <th scope="row" style="color: #666666;">${submission.id}</th>
                                            <td>
                                                <a href="/paper/submission/${submission.fileDestination}"> ${submission.title}</a>
                                            </td>
                                            <td><a class="btn btn-primary"
                                                   href="/review/form?submissionId=${submission.id}">Review</a>
                                            </td>
                                        </tr>
                                    </#list>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>