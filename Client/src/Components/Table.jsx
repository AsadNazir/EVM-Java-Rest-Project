import React, { useEffect } from 'react'
import "../CSS/table.css"
import { useState } from 'react';
import routes from '../routes';
import Modal from './Modal';
import services from '../services';
import { useNavigate } from 'react-router-dom';

export default function Table() {

    const navigate = useNavigate();
    const [allParties, setAllParties] = useState([]);
    const [loading, setLoading] = useState(false);
    const [selectedParty, setSelectedParty] = useState(null);
    const [votingStarted, setVotingStarted] = useState(false);

    async function getVotingStatus() {
        //get voting status from database

        let result = await fetch(routes.getVotingStatusAdmin, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + sessionStorage.getItem("token")
            }
        })


        if (result.status === 401) {
            alert('Session Expired');
            navigate("/adminLogin");
            return;
        }

        let data = await result.json();

        if (data.error === false) {
            setVotingStarted(data.data);
        }

    }


    async function getParties() {

        setLoading(true);
        const response = await fetch(routes.getAllParties, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + sessionStorage.getItem("token")
            }
        })

        if (response.status === 401) {
            alert('Session Expired');
            navigate("/adminLogin");
            return;
        }

        let result = await response.json();

        setAllParties(result.data);
        setLoading(false);
    }

    const deleteParty = async (id) => {
        setSelectedParty(id);
    }

    useEffect(() => {

        async function checkAdmin() {
            const isAdmin = await services.isAdmin().then(data => data);
            if (!isAdmin) {
                navigate("/adminLogin");
            }

            getParties();
            getVotingStatus();
        }

        checkAdmin();


    }, [])

    const confirmHandler = async (id) => {
        // delete party from database
        let response = await fetch(routes.deleteParty + "?regNo=" + id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + sessionStorage.getItem("token")
            },
        })

        if (response.status === 401) {
            alert('Session Expired');
            navigate("/adminLogin");
            return;
        }
        else if (response.status === 200) {
            alert('Party Deleted Successfully');
            getParties();
        }

        let result = await response.json();

        if (result.error === false) {
            alert('Party Deleted Successfully');
        }

        else {
            alert('Party Deletion Failed');
        }
        setSelectedParty(null);
        //


        console.log(routes.deleteParty + "?regNo=" + id)
    }

    const cancelHandler = () => {
        setSelectedParty(null);
    }


    return (
        <div className='tableWrapper'>
            <Modal statement={"Do u want to delete it ?"} selected={selectedParty} cancelHandler={cancelHandler} confirmHandler={confirmHandler} />
            {loading ? <div class="spinner-border text-primary" role="status">
                <span class="sr-only"></span>

            </div> : <><div class="searchBar my-4">
                <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search" />
                <button class="btn btn-outline-success " type="submit">Search</button>
            </div>
                <table class="table table-hover">
                    <thead>
                        <tr>

                            <th scope="col">Reg No</th>
                            <th scope="col">Party Name</th>
                            <th scope="col">Party Leader</th>
                            <th scope="col">Election Sign</th>
                            <th scope="col">Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            allParties.map((party, index) => {
                                return (
                                    <tr key={index}>
                                        <td>{party.regNo}</td>
                                        <td>{party.partyName}</td>
                                        <td>{party.partyLeader}</td>
                                        <td>{party.electionSign}</td>
                                        <td><button id={party.regNo} disabled={votingStarted} className='btn btn-danger' data-bs-toggle="modal" data-bs-target="#staticModal"
                                            onClick={() => deleteParty(party.regNo)}>Delete</button></td>
                                    </tr>
                                )
                            })
                        }
                    </tbody>
                </table>
            </>
            }



        </div>
    )
}
