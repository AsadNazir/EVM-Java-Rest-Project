import React from 'react'
import routes from '../routes'
import services from '../services'
import { useState, useEffect } from 'react'
import Modal from './Modal';


export default function ViewVoters() {

    const [voters, setVoters] = useState([]);
    const [loading, setLoading] = useState(false);
    const [selectedVoter, setSelectedVoter] = useState(null);
    const [votingStarted, setVotingStarted] = useState(false);

    useEffect(() => {

        async function checkAdmin() {
            setLoading(true);
            const isAdmin = await services.isAdmin().then(data => data);
            if (!isAdmin) {
                setLoading(false);
                navigate("/adminLogin");
            }

            const votingStarted = await services.getVotingStatus().then(data => data);

            if (votingStarted) {
                setVotingStarted(votingStarted);
            }

            getVoters();
        }

        checkAdmin();
    }, [])

    const getVoters = async () => {

        setLoading(true);
        let respone = await fetch(routes.getAllVoters, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + sessionStorage.getItem("token")
            }
        })

        if (respone.status === 401) {
            alert('Session Expired');
            return;
        }


        let result = await respone.json();

        if (result.error === true) {
            alert('Failed to get voters');
            return;
        }
        console.log(result.data);

        setVoters(result.data);

        setLoading(false);

    }

    const deleteVoter = async ({ id, name }) => {
        console.log(routes.deleteVoter + "?cnic=" + id + "&name=" + name);
        let respone = await fetch(routes.deleteVoter+"?cnic="+id+"&name="+name, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + sessionStorage.getItem("token")
            }
        })

        if (respone.status === 401) {
            alert('Session Expired');
            return;
        }

        let data = await respone.json();

        if (data.error === true) {
            alert('Failed to delete voter');
            return;
        }

        alert('Voter Deleted Successfully');
        getVoters();
    }


    return (
        <>
            <h2 className='text-center my-4'>List of Voters</h2>

            <div className='tableWrapper'>
                {loading ? <div class="spinner-border text-primary" role="status">
                    <span class="sr-only"></span>
                </div> : <> <div class="searchBar my-4">
                    <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search" />
                    <button class="btn btn-outline-success " type="submit">Search</button>
                </div>
                    <table class="table table-hover">
                        <thead>
                            <tr>

                                <th scope="col">#</th>
                                <th scope="col">CNIC</th>
                                <th scope="col">Name</th>
                                <th scope="col">Voting Status</th>
                                <th scope="col">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                voters.map((voter, index) => {
                                    return <tr key={index}>
                                        <th scope="row">{index + 1}</th>
                                        <td>{voter.cnic}</td>
                                        <td>{voter.name}</td>
                                        <td>{voter.voted ? "Voted" : "Not Voted"}</td>
                                        <td><button
                                            data-bs-toggle="modal"
                                            data-bs-target="#staticModal"
                                            disabled={votingStarted}
                                            onClick={() => setSelectedVoter({
                                                id: voter.cnic,
                                                name: voter.name
                                            })} className='btn btn-danger'>Delete</button></td>

                                    </tr>
                                }
                                )
                            }
                        </tbody>
                    </table></>}

            </div>
            <Modal
                statement={"Do u want to delete it ?"}
                confirmHandler={deleteVoter}
                onCancelHandler={null}
                selected={selectedVoter}
            />

        </>
    )
}
