import React from 'react'
import { useNavigate, Link } from 'react-router-dom'
import '../CSS/Login.css'
import routes from '../routes';
import Modal from './Modal';
export default function Login() {

    let navigate = useNavigate();
    let login = async (event) => {

        event.preventDefault();

        let cnic = document.getElementById("cnic").value;
        let password = document.getElementById("loginPassword").value;

        let data = {
            cnic: cnic,
            password: password
        }

        const response = await fetch(routes.voterLogin, {
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
           navigate("/voter");
        }

        else {
            alert("Invalid Credentials");

            return;
        }

        ;
    }

    const modalHandler =()=>
    {
        //do nothing
    }

    return (
        <div className="LoginWrapper">
            <div className="Login" id="pills-login" role="tabpanel" aria-labelledby="tab-login">
                <h1 className=''>E-Voting Login</h1>
                <form onSubmit={login}>
                    {/* <!-- CNIC input --> */}
                    <div className="form-outline mb-4">
                        <input type="text" id="cnic" required pattern="^\d{13}$" className="form-control" />
                        <label className="form-label" htmlFor="cnic">CNIC (wihtout dashes)</label>
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
                        <p>Not a member? <Link to={"/register"}>Register</Link></p>
                    </div>
                </form>
            </div>
        </div >
    )
}
