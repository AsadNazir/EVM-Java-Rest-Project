import React from 'react'
import { useNavigate, Link } from 'react-router-dom'
import '../CSS/Login.css'

export default function Login() {
    let navigate = useNavigate();
    let login = async (event) => {

        event.preventDefault();

    }



    return (
        <div className="LoginWrapper">
            <div className="Login" id="pills-login" role="tabpanel" aria-labelledby="tab-login">
                <h1 className=''>E-Voting Login</h1>
                <div>
                    {/* <!-- CNIC input --> */}
                    <div className="form-outline mb-4">
                        <input type="number" id="cnic" className="form-control" />
                        <label className="form-label" htmlFor="cnic">CNIC</label>
                    </div>

                    {/* <!-- Password input --> */}
                    <div className="form-outline mb-4">
                        <input type="password" id="loginPassword" className="form-control" />
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
                    <button type="submit" onClick={login} className="btn btn-primary btn-block mb-4">Sign in</button>

                    {/* <!-- Register buttons --> */}
                    <div className="text-center">
                        <p>Not a member? <Link to={"/register"}>Register</Link></p>
                    </div>
                </div>
            </div>
        </div >
    )
}
