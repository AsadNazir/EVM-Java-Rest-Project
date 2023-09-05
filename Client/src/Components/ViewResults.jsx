import React from 'react'
import "../CSS/adminPage.css"
import { useState } from 'react';
import { BsFill1CircleFill, BsFill2CircleFill } from 'react-icons/bs';
import Table from './Table';
export default function ViewResults() {

    const [votingStarted, setVotingStarted] = useState(false);
    const [loading, setLoading] = useState(false);

    const getVotingStatus = async () => {
        //get voting status from database
        setVotingStarted(true);
    }

    return (
        <div className='viewResults'>
            <h1 className='text-center my-4'>{votingStarted ? "Voting in Progress !" : "Voting has stopped"}</h1>
            <div className='positions'>
                <div to={"addParty"} type="button" class="btn btn-success btn-lg btn-block"><div className='icons'><BsFill1CircleFill /></div>
                    <div className='stats'>
                        <h3>Party 1</h3>
                        <h2>1000 Votes</h2>
                    </div>

                </div>
                <div to={"addParty"} type="button" class="btn btn-danger btn-lg btn-block"><div className='icons'><BsFill2CircleFill /></div>
                    <div className='stats'>
                        <h3>Party 2</h3>
                        <h2>100 Votes</h2>
                    </div>

                </div>

            </div>
            <div className='tableWrapper'>
                <div class="searchBar my-4">
                    <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search" />
                    <button class="btn btn-outline-success " type="submit">Search</button>
                </div>
                <table class="table table-hover">
                    <thead>
                        <tr>

                            <th scope="col">Rank</th>
                            <th scope="col">Reg No</th>
                            <th scope="col">Name</th>
                            <th scope="col">Election Sign</th>
                            <th scope="col">Votes</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td>Mark</td>
                            <td>Otto</td>
                            <td>@mdo</td>
                            <td><b>1000</b></td>
                        </tr>

                    </tbody>
                </table>
            </div>
        </div>
    )
}
