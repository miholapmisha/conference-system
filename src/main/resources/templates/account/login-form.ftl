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
<div class="d-flex justify-content-center align-self-center w-100 h-100">
    <form role="form" action="/account/login" method="post" class="mt-lg-5">
        <h3>Login</h3>
        <div data-mdb-input-init class="form-outline mb-4">
            <input name="username" type="email" id="form2Example1" class="form-control"/>
            <label class="form-label" for="form2Example1">Email address</label>
        </div>

        <div data-mdb-input-init class="form-outline mb-4">
            <input name="password" type="password" id="form2Example2" class="form-control"/>
            <label class="form-label" for="form2Example2">Password</label>
        </div>

        <button type="submit" data-mdb-button-init data-mdb-ripple-init class="w-100 btn btn-primary btn-block mb-4">
            Sign in
        </button>

        <div class="text-center">
            <p>Not a member? <a href="/account/registration-form">Register</a></p>
        </div>
        <#if message??>
            <div class="p-3 text-primary-emphasis bg-primary-subtle border border-primary-subtle rounded-3">
                ${message}
            </div>
        </#if>
    </form>
</div>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
</body>
</html>