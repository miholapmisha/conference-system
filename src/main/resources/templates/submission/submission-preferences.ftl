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
    <div class="align-self-center mx-2">
        <a class="btn btn-danger" href="/account/list/program-committee">Program committee</a>
    </div>

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
</body>
</html>