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
                    <h2 class="text-danger mb-5">Your recent reviews</h2>
                    <div class="row justify-content-center">
                        <div class="col-12">
                            <div>
                                <a href="#" class="text-danger">View submission with review preferences</a>
                            </div>
                            <div class="table-responsive bg-white">
                                <table class="table mb-0">
                                    <thead>
                                    <tr>
                                        <th scope="col">Submission ID</th>
                                        <th scope="col">Overall merit</th>
                                        <th scope="col">Date review</th>
                                        <th scope="col">Review paper</th>
                                        <th></th>
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#list reviews as review>
                                        <tr>
                                            <th scope="row" style="color: #666666;">
                                                <a href="/submission/info/${review.submissionId}"> ${review.submissionId} </a>
                                            </th>
                                            <#if review.overallMerit="REJECT">
                                                <td style="color:#9c1b02">Reject</td>
                                            <#elseif review.overallMerit="WEAK_REJECT">
                                                <td style=" color:#db2400">Weak reject</td>
                                            <#elseif review.overallMerit="WEAK_ACCEPT">
                                                <td style=" color:#c28100">Weak accept</td>
                                            <#elseif review.overallMerit="ACCEPT">
                                                <td style=" color:#9bcf3c">Accept</td>
                                            <#elseif review.overallMerit="STRONG_ACCEPT">
                                                <td style=" color:#859400">Strong accept</td>
                                            </#if>
                                            <td>
                                                <p> ${review.reviewDate}</p>
                                            </td>
                                            <td>
                                                <#if review.fileDestination?? && review.fileDestination != ''>
                                                    <a href="/paper/reviews/${review.fileDestination}">View</a>
                                                </#if>
                                            </td>
                                            <td>
                                                <a href="/review/form?submissionId=${review.submissionId}"
                                                   class="btn btn-primary">Edit</a></td>
                                            <td>
                                                <form action="/review/${review.id}" method="post">
                                                    <button type="submit" class="btn btn-primary">Delete</button>
                                                </form>
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