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
    <div class="mx-5 w-50 my-lg-5">
        <div class="mb-5"><a href="/review">All your reviews</a></div>
        <a class="text-danger" href="/paper/submission/${submission.fileDestination}"><h2
                    class="text-danger">${submission.title}</h2></a>
        <div data-mdb-input-init class="mb-4 text-primary">
            ${submission.status}
        </div>

        <div data-mdb-input-init class="mb-4">
            ${submission.abstractOfSubmission}
        </div>

        <div data-mdb-input-init class="form-outline mb-4">
            <p class="text-primary form-label">Topics of submission</p>
            <div style="max-height: 200px" class="d-flex flex-column flex-wrap">
                <#list topics as topic>
                    <div class="mx-2">
                        <p>${topic.name}</p>
                    </div>
                </#list>
            </div>
        </div>
        <form role="form" action="/review/submit?submissionId=${submission.id}" method="post"
              enctype="multipart/form-data">
            <div>
                <h5 class="text-primary">Overall merit:</h5>
                <div class="mb-2">
                    <input type="radio" id="reject"
                           name="overallMerit" ${(currentReview?? && currentReview.overallMerit == 'REJECT')?then('checked', '')}
                           value="REJECT"/>
                    <label style="font-size: 16px; font-weight: bold; color:#9c1b02" for="reject">1. Reject</label>
                </div>
                <div class="mb-2">
                    <input type="radio" id="weakReject" name="overallMerit"
                           value="WEAK_REJECT" ${(currentReview?? && currentReview.overallMerit == 'WEAK_REJECT')?then('checked', '')}/>
                    <label style="font-size: 16px; font-weight: bold; color:#db2400" for="weakReject">2. Weak
                        reject</label>
                </div>
                <div class="mb-2">
                    <input type="radio" id="weakAccept" name="overallMerit"
                           value="WEAK_ACCEPT" ${(currentReview?? && currentReview.overallMerit == 'WEAK_ACCEPT')?then('checked', '')}/>
                    <label style="font-size: 16px; font-weight: bold; color:#c28100" for="weakAccept">3. Weak
                        accept</label>
                </div>
                <div class="mb-2">
                    <input type="radio" id="accept" name="overallMerit"
                           value="ACCEPT" ${(currentReview?? && currentReview.overallMerit == 'ACCEPT')?then('checked', '')}/>
                    <label style="font-size: 16px; font-weight: bold; color:#9bcf3c" for="accept">4. Accept</label>
                </div>
                <div class="mb-2">
                    <input type="radio" id="strongAccept" name="overallMerit"
                           value="STRONG_ACCEPT" ${(currentReview?? && currentReview.overallMerit == 'STRONG_ACCEPT')?then('checked', '')}/>
                    <label style="font-size: 16px; font-weight: bold; color:#859400" for="strongAccept">5. Strong
                        accept</label>
                </div>
            </div>

            <div data-mdb-input-init class="form-outline mt-5 mb-5" style="width: 300px">
                <label class="text-primary form-label" for="paper">Load review (Optional)</label>
                <input name="reviewPaper" type="file" id="paper" class="form-control"/>
            </div>

            <div data-mdb-input-init class="form-outline mb-5">
                <label class="text-primary form-label" for="abstractOfSubmission">Paper summary</label>
                <textarea name="comment" id="abstractOfSubmission" class="form-control w-100"
                          style="height: 120px">${(currentReview??)?then(currentReview.comment,'')}</textarea>
            </div>

            <#if errors??>
                <div style="max-width: 600px;"
                     class="mb-4 p-3 text-primary-emphasis bg-primary-subtle border border-primary-subtle rounded-3">
                    <#list errors as error>
                        <p> ${error} </p>
                    </#list>
                </div>
            </#if>

            <button type="submit" data-mdb-button-init data-mdb-ripple-init class="btn btn-primary btn-block mb-4"
                    style="width: 150px; height: 50px">
                Submit review
            </button>
        </form>

    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>