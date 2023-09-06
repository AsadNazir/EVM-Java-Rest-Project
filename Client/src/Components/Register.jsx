import React from 'react'
import routes from '../routes';
import { Modal } from 'bootstrap';

export default function Register() {

    const [loader, setLoader] = React.useState(false);

    let register = async (event) => {
        event.preventDefault();
        setLoader(true);
        let name = document.getElementById('name').value;
        let cnic = document.getElementById('cnic').value;
        let password = document.getElementById('password').value;
        let email = document.getElementById('email').value;

        let data = {
            name,
            cnic,
            password,
            email
        }

        let response = await fetch(routes.regsiterVoter, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data),
        })

        let result = await response.json();
        console.log(result);
        if (result.error === false) {

            alert('Registered Successfully');
        } else {
            alert('Registration Failed');
        }

        setLoader(false);
    }




    return (

        <div class="Register LoginWrapper" id="pills-register" role="tabpanel" aria-labelledby="tab-register">
            {loader ? <div class="spinner-border text-primary" role="status">
                <span class="sr-only"></span>

            </div> : <form onSubmit={register}>

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
                <div class="form-outline mb-4">
                    <input type="email" required id="email" class="form-control" />
                    <label class="form-label" for="registerName">Email</label>
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
            </form>}

        </div>
    )
}
