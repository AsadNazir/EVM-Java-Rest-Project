import React from 'react'
import '../CSS/navbar.css'
import { Link } from 'react-router-dom'


export default function Navbar() {
    const logout = () => {
        sessionStorage.removeItem("token");
    }


    return (
        <nav class="navbar navbar-light bg-light justify-content-center">
            <Link to={"/home"} class="navbar-brand">E Voting System</Link>
            <div class="form-inline">
                <Link to={"/"} onClick={logout} class="btn btn-danger my-2 mx-4 my-sm-0" type="button">Logout</Link>
            </div>
        </nav>
    )
}
