import React from 'react'
import { Link } from 'react-router-dom';
import routes from '../routes';

export default function AddParty() {

    const [loader, setLoader] = React.useState(false);

    const addParty = async (event) => {

        event.preventDefault();
        setLoader(true);

        let partyName = document.getElementById('partyName').value;
        let partyLeader = document.getElementById('partyLeader').value;
        let regNo = document.getElementById('regNo').value;
        let electionSign = document.getElementById('electionSign').value;

        let data = {
            partyName,
            partyLeader,
            regNo,
            electionSign
        }

        let response = await fetch(routes.addParty, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + sessionStorage.getItem("token")
            },
            body: JSON.stringify(data),
        })

        if (response.status === 401) {
            alert('Session Expired');
            setLoader(false);
            return;
        }

        let result = await response.json();

        if (result.error === false) {
            alert('Party Added Successfully');
        } else {
            alert('Party Addition Failed');
        }

        setLoader(false);



    }

    return (
        <div className='Wrapper'>


            {loader ? <div class="spinner-border text-primary" role="status">
                <span class="sr-only"></span>

            </div> : <form onSubmit={addParty}>

                <h1 class="text-center">Add Party</h1>

                {/* <!-- Name input --> */}
                <div class="form-outline mb-4">
                    <input type="text" id="partyName" required class="form-control" />
                    <label class="form-label" for="registerName">Party Name</label>
                </div>
                {/* <!-- Password input --> */}
                <div class="form-outline mb-4">
                    <input type="text" required id="partyLeader" class="form-control" />
                    <label class="form-label" for="password">Party Leader</label>
                </div>
                <div class="form-outline mb-4">
                    <input type="number" required id="regNo" class="form-control" />
                    <label class="form-label" form="registerName">Registration No</label>
                </div>

                {/* <!-- Password input --> */}
                <div class="form-outline mb-4">
                    <input type="text" required id="electionSign" class="form-control" />
                    <label class="form-label" for="password">Election Sign</label>
                </div>

                {/* <!-- Submit button --> */}
                <div className="btnDiv">
                    <button type='submit' className="btn btn-primary btn-block mb-3">Add Party</button>
                    <Link to={"/admin"} type='submit' class="btn mx-4 btn-danger btn-block mb-3">Cancel</Link>
                </div>
            </form>}

        </div>


    )
}
