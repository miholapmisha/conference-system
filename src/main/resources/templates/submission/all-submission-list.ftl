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
                    <div class="row justify-content-center">
                        <div class="d-flex justify-content-center">
                            <form id="search-form" method="get">
                                <div style="width: 600px; height: 30px" class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <button class="input-group-text" id="basic-addon1">Search</button>
                                    </div>
                                    <input id="search-input" name="search" type="text" class="form-control"
                                           aria-label="Search"
                                           aria-describedby="basic-addon1">
                                </div>
                            </form>
                        </div>
                        <div class="col-12">
                            <div class="table-responsive bg-white">
                                <table class="table mb-0">
                                    <thead>
                                    <tr>
                                        <th scope="col">ID</th>
                                        <th scope="col">Name</th>

                                        <#if role == "ADMIN">
                                            <th scope="col">Total estimation</th>
                                            <th scope="col"></th>
                                            <th scope="col">Status</th>
                                        </#if>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <#list submissions?keys as key>
                                        <tr>
                                            <th scope="row" style="color: #666666;">
                                                <a href="/submission/info/${key.id}"> ${key.id} </a>
                                            </th>
                                            <td>
                                                <a href="/paper/submission/${key.fileDestination}"> ${key.title}</a>
                                            </td>

                                            <#if role == "ADMIN">
                                                <td>
                                                    <#if submissions?values[key_index] != 0>
                                                        ${submissions?values[key_index]}
                                                    <#else>
                                                        -
                                                    </#if>
                                                </td>
                                                <td>
                                                    <button type="button" class="btn btn-primary"
                                                            onclick="handleChangeSubmissionStatusClick('${key.status}',${key.id})">
                                                        Change status
                                                    </button>
                                                </td>
                                                <#if key.status="SUBMITTED">
                                                    <td class="text-primary">${key.status}</td>
                                                <#elseif key.status="REJECTED">
                                                    <td class="fw-bold text-danger">${key.status}</td>
                                                <#elseif key.status="ACCEPTED">
                                                    <td class="fw-bold text-success">${key.status}</td>
                                                </#if>
                                            </#if>
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
<!-- Modal -->
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <label for="submissionStatusSelect">Select status:</label>
                <select onchange="handleChange()" class="form-control" id="submissionStatusSelect">
                    <option value="SUBMITTED">Submitted</option>
                    <option value="ACCEPTED">Accepted</option>
                    <option value="REJECTED">Rejected</option>
                </select>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <form action="/submission/update" method="post">
                    <input id="submissionIdHolder" name="submissionId" type="hidden" value="">
                    <input id="selectedSubmissionStatus" name="status" type="hidden" value="">
                    <button type="submit" class="btn btn-primary">Save changes</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"
        integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous">
</script>

<script>

    function handleChange() {
        document.getElementById("selectedSubmissionStatus").value = document.getElementById("submissionStatusSelect").value;
    }

    function handleChangeSubmissionStatusClick(submissionStatus, submissionId) {
        openModal(submissionStatus);
        document.getElementById("submissionIdHolder").value = submissionId;
    }

    function openModal(selectedSubmission) {
        let submissionStatusSelect = document.getElementById("submissionStatusSelect");

        for (let i = 0; i < submissionStatusSelect.options.length; i++) {
            if (submissionStatusSelect.options[i].value === selectedSubmission) {
                submissionStatusSelect.options[i].selected = true;
            }
        }

        $('#exampleModal').modal('show');
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous">
</script>
</body>
</html>