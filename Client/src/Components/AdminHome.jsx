import React from 'react'
import "../CSS/adminPage.css"
import { useState } from 'react';
import { BsFillPersonPlusFill, BsHourglassTop, BsPeopleFill } from "react-icons/bs";
import { GiMedallist } from "react-icons/gi"
import Table from './Table';
import { useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Modal from './Modal';
import routes from '../routes';
import services from '../services';


export default function AdminHome() {
    const [votingStarted, setVotingStarted] = useState(false);
    const [votingloading, setVotingLoading] = useState(false);
    const [loading, setLoading] = useState(false);
    const [allParties, setAllParties] = useState([]);


    const navigate = useNavigate();


    const getVotingStatus = async () => {
        //get voting status from database
        setVotingStarted(true);
    }

    useEffect(() => {

        async function checkAdmin() {
            const isAdmin = await services.isAdmin().then(data => data);
            if (!isAdmin) {
                navigate("/adminLogin");
            }
        }

        checkAdmin();
    }, [])


    const changeVotingStatus = async () => {

        //setLoading(!loading);
        //change voting status in database
        setVotingLoading(!votingStarted);
    }
    const confirmHandler = async (id) => {

        
    }

    return (
        <>{
            votingloading ? <div class="spinner-border text-light" role="status">
                <span class="visually-hidden">Loading...</span>
            </div> :
                votingStarted ? <h1 className='votingStatus'>Voting Started</h1> : <h1 className='votingStatus'>Voting has not Started</h1>}
            <div className='AdminPageWrapper'>
                <Link to={"addParty"} type="button" class="btn btn-primary btn-lg btn-block"><span className='icons'><BsFillPersonPlusFill /></span><h4>Add Party</h4></Link>
                <Link to={"viewResults"} type="button" class="btn btn-secondary btn-lg btn-block"><span className='icons'><GiMedallist /></span><h4>View Results</h4></Link>
                <Link to={"viewVoters"} type="button" class="btn btn-success btn-lg btn-block"><span className='icons'><BsPeopleFill /></span><h4>View Voters</h4></Link>
                <button type="button" class="btn btn-warning btn-lg btn-block" data-bs-toggle="modal" data-bs-target="#staticBackdrop">
                    {/* Add a loader */}
                    {loading ? <div class="spinner-border text-light" role="status">
                        <span class="visually-hidden">Loading...</span>
                    </div> : <span className='icons'><BsHourglassTop /><h4>{votingStarted ? "Stop Voting" : "Start Voting"}</h4></span>}
                </button>
            </div>
            <Table />
            {/* <!-- Modal --> */}
            <div class="modal fade" id="staticBackdrop" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="staticBackdropLabel">Voting Time</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        {!votingStarted ? <div class="modal-body">
                            <label class="form-label" htmlFor="time">Enter Time in Minutes for the Voting</label>
                            <input className='form-control' type="number" id="time" name='time' />
                        </div> : <div class="modal-body">Do You want to stop the voting ?</div>}
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button onClick={changeVotingStatus} type="button" class="btn btn-primary" data-bs-dismiss="modal">{!votingStarted ? "Start the Voting" : "Stop the voting"}</button>
                        </div>
                    </div>
                </div>
            </div>
           

        </>
    )
}
