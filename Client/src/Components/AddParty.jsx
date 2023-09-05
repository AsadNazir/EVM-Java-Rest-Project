import React from 'react'
import { Link } from 'react-router-dom';

export default function AddParty() {
    const addParty = async (event) => {
        event.preventDefault();
    }

    return (
        <div className='Wrapper'>
            <form onSubmit={addParty}>

                <h1 class="text-center">Add Party</h1>

                {/* <!-- Name input --> */}
                <div class="form-outline mb-4">
                    <input type="text" id="name" required class="form-control" />
                    <label class="form-label" for="registerName">Name</label>
                </div>
                <div class="form-outline mb-4">
                    <input type="number" required pattern="^\d{13}$" id="cnic" class="form-control" />
                    <label class="form-label" form="registerName">Registration No</label>
                </div>

                {/* <!-- Password input --> */}
                <div class="form-outline mb-4">
                    <input type="password" required id="password" class="form-control" />
                    <label class="form-label" for="password">Election Sign</label>
                </div>

                {/* <!-- Submit button --> */}
                <div className="btnDiv">
                    <button type='submit' className="btn btn-primary btn-block mb-3">Add Party</button>
                    <Link to={"/admin"} type='submit' class="btn mx-4 btn-danger btn-block mb-3">Cancel</Link>
                </div>
            </form>
        </div>


    )
}
