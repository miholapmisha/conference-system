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
                <div><p>Welcome to the ${conferenceName} submissions site.</p></div>
                <div>
                    <h2 class="text-danger mb-5">Your submissions</h2>
                    <div class="row justify-content-center">
                        <div class="col-12">
                            <div class="table-responsive bg-white">
                                <table class="table mb-0">
                                    <thead>
                                    <tr>
                                        <th scope="col">ID</th>
                                        <th scope="col">Name</th>
                                        <th scope="col">Status</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#list submissions as submission>
                                        <tr>
                                            <th scope="row" style="color: #666666;">${submission.id}</th>
                                            <td>
                                                <a href="/paper/submission/${submission.fileDestination}"> ${submission.title}</a>
                                            </td>
                                            <#if submission.status="SUBMITTED">
                                                <td class="text-primary">${submission.status}</td>
                                            <#elseif submission.status="REJECTED">
                                                <td class="text-danger">${submission.status}</td>
                                            <#elseif submission.status="ACCEPTED">
                                                <td class="text-success">${submission.status}</td>
                                            </#if>
                                        </tr>
                                    </#list>
                                    </tbody>
                                </table>
                                <div class="mt-5">
                                    <a href="/submission/form" type="submit" data-mdb-button-init data-mdb-ripple-init
                                       class="btn btn-primary btn-block mb-4">
                                        New submission
                                    </a>

                                    <p>
                                        Submission deadline:<span class="text-danger">  ${deadline}</span>
                                        <br>
                                        Submissions must be ready by this deadline to be reviewed.
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div>
                    <a class="text-danger fw-bold" href="/submission/all">See all submissions</a>
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