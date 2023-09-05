import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import "../CSS/login.css"
import routes from '../routes';

export default function AdminLogin() {
    const navigate = useNavigate();

    let login = async (event) => {

        event.preventDefault();

        let username = document.getElementById("username").value;
        let password = document.getElementById("loginPassword").value;

        let data = {
            name: username,
            password: password
        }

        const response = await fetch(routes.adminLogin, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        }
        )

        let res = await response.json();
        console.log(res);

        if (!res.error) {
            sessionStorage.setItem("token", res.data);
            navigate("/admin");
        }

        else {
            alert("Invalid Credentials");
            return;
        }
    }

    return (
        <div className="LoginWrapper">
            <div className="Login" id="pills-login" role="tabpanel" aria-labelledby="tab-login">
                <h1 className=''>E-Voting Admin Login</h1>
                <form onSubmit={login}>
                    {/* <!-- CNIC input --> */}
                    <div className="form-outline mb-4">
                        <input type="text" id="username" required className="form-control" />
                        <label className="form-label" htmlFor="username">Username</label>
                    </div>

                    {/* <!-- Password input --> */}
                    <div className="form-outline mb-4">
                        <input type="password" id="loginPassword" required className="form-control" />
                        <label className="form-label" htmlFor="loginPassword">Password</label>
                    </div>

                    {/* <!-- 2 column grid layout --> */}
                    <div className="row mb-4">

                        <div className="col-md-12 d-flex justify-content-center">
                            {/* <!-- Simple link --> */}
                            <a href="#!">Forgot password?</a>
                        </div>
                    </div>

                    {/* <!-- Submit button --> */}
                    <button type="submit" className="btn btn-primary btn-block mb-4">Sign in</button>

                    {/* <!-- Register buttons --> */}
                    <div className="text-center">
                        <p>Are You a Voter ? <Link to={"/"}>Voter Login</Link></p>
                    </div>
                </form>
            </div>
        </div >
    )
}
