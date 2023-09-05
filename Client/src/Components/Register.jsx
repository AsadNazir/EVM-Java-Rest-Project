import React from 'react'


export default function Register() {

    let register = async (event) => {
        event.preventDefault();
        console.log("Calling this one")
    }




    return (
        <div class="Register LoginWrapper" id="pills-register" role="tabpanel" aria-labelledby="tab-register">
            <form onSubmit={register}>

                <h1 class="text-center">Register</h1>

                {/* <!-- Name input --> */}
                <div class="form-outline mb-4">
                    <input type="text" id="name" required class="form-control" />
                    <label class="form-label" for="registerName">Name</label>
                </div>
                <div class="form-outline mb-4">
                    <input type="text" required pattern="^\d{13}$" id="cnic" class="form-control" />
                    <label class="form-label" for="registerName">CNIC</label>
                </div>

                {/* <!-- Password input --> */}
                <div class="form-outline mb-4">
                    <input type="password" required id="password" class="form-control" />
                    <label class="form-label" for="password">Password</label>
                </div>

                {/* <!-- Repeat Password input --> */}
                <div class="form-outline mb-4">
                    <input type="password" required id="registerRepeatPassword" class="form-control" />
                    <label class="form-label" for="registerRepeatPassword">Repeat password</label>
                </div>


                <div class="form-check d-flex justify-content-center mb-4">
                    <input class="form-check-input me-2" type="checkbox" value="" id="registerCheck" checked
                        aria-describedby="registerCheckHelpText" />
                    <label class="form-check-label" for="registerCheck">
                        I have read and agree to the terms
                    </label>
                </div>


                {/* <!-- Submit button --> */}
                <div className="btnDiv">
                    <button type='submit' class="btn btn-primary btn-block mb-3">Sign in</button>
                </div>
            </form>
        </div>
    )
}
