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
<div class="d-flex justify-content-center">
    <form id="registration-form" action="/account/register" method="post" class="mt-lg-3 h-100" style="width: 400px;">
        <h3>Register now</h3>
        <div data-mdb-input-init class="form-outline mb-4">
            <input name="email" type="email" id="form2Example1" class="form-control"/>
            <label class="form-label" for="form2Example1">Email address</label>
        </div>

        <div data-mdb-input-init class="form-outline mb-4">
            <input name="password" type="password" id="form2Example2" class="form-control"/>
            <label class="form-label" for="form2Example2">Password</label>
        </div>

        <div data-mdb-input-init class="form-outline mb-4">
            <input name="firstName" type="text" id="form2Example2" class="form-control"/>
            <label class="form-label" for="form2Example2">First name</label>
        </div>

        <div data-mdb-input-init class="form-outline mb-4">
            <input name="secondName" type="text" id="form2Example2" class="form-control"/>
            <label class="form-label" for="form2Example2">Second name</label>
        </div>

        <div data-mdb-input-init class="form-outline mb-4">
            <input name="affiliation" type="text" id="form2Example2" class="form-control"/>
            <label class="form-label" for="form2Example2">Affiliation</label>
        </div>

        <div data-mdb-input-init class="form-outline mb-4">
            <input name="region" type="text" id="form2Example2" class="form-control"/>
            <label class="form-label" for="form2Example2">Country/Region</label>
        </div>

        <div data-mdb-input-init class="form-outline mb-4">
            <input name="orcidId" type="text" id="form2Example2" class="form-control"/>
            <label class="form-label" for="form2Example2">ORCID iD (optional)</label>
        </div>

        <div class="row mb-4">
            <div class="col">
                <a href="#!">Forgot password?</a>
            </div>
        </div>

        <button type="submit" data-mdb-button-init data-mdb-ripple-init class="w-100 btn btn-primary btn-block mb-4">
            Register
        </button>

        <div class="text-center">
            <p>You are member? <a href="/account/login">Login</a></p>
        </div>
        <#if error??>
            <div class="p-3 text-primary-emphasis bg-primary-subtle border border-primary-subtle rounded-3">
                ${error}
            </div>
        </#if>
    </form>
</div>

<script src="/static/registration-form.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>