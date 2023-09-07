import React, { useEffect } from 'react'
import "../CSS/adminPage.css"
import { useState } from 'react';
import { BsFill1CircleFill, BsFill2CircleFill } from 'react-icons/bs';
import services from '../services';
import routes from '../routes';
import { useNavigate } from 'react-router-dom';


export default function ViewResults() {

    const [votingStarted, setVotingStarted] = useState(false);
    const [loading, setLoading] = useState(false);
    const [results, setResults] = useState([]);
    const [parties, setParties] = useState([]);
    const navigate = useNavigate();



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

        setParties(result.data);
        setLoading(false);
    }


    async function getResults() {
        setLoading(true);
        let respone = await fetch(routes.getResultsAdmin, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + sessionStorage.getItem("token")
            }
        }
        )

        if (respone.status === 401) {
            alert('Session Expired');
            return;
        }

        let data = await respone.json();

        if (data.error === true) {
            alert('Failed to get results');
            return;
        }

        setResults(data.data);
        sortParties();
        setLoading(false);
        console.log(results);

    }

    // Sort results in descending order
    //parallel sort parties

    function sortParties() {
        let tempParties = [...parties];
        let tempResults = [...results];

        for (let i = 0; i < tempResults.length; i++) {
            for (let j = i + 1; j < tempResults.length; j++) {
                if (tempResults[i].votes < tempResults[j].votes) {

                    //shift parties
                    let temp = tempParties[i];
                    tempParties[i] = tempParties[j];
                    tempParties[j] = temp;

                    //shift results
                    let temp2 = tempResults[i];
                    tempResults[i] = tempResults[j];
                    tempResults[j] = temp2;
                }
            }
        }

        setResults(tempResults);
        setParties(tempParties);

        console.log(tempResults);
        console.log(tempParties);


    }



    useEffect(() => {

        services.getVotingStatus().then(data => {
            setVotingStarted(data);
        })

        async function checkAdmin() {
            const isAdmin = await services.isAdmin().then(data => data);
            if (!isAdmin) {
                navigate("/adminLogin");
            }

            getResults();
            getParties();


        }

        checkAdmin();

    }, [])



    return (
        <div className='viewResults'>
            <h1 className='text-center my-4'>{votingStarted ? "Voting in Progress !" : "Voting has stopped"}</h1>
            <div className='positions'>
                <div to={"addParty"} type="button" class="btn btn-success btn-lg btn-block"><div className='icons'><BsFill1CircleFill /></div>
                    <div className='stats'>
                        <h3>{parties[0]? parties[0].partyName: "null"}</h3>
                        <h2>votes {results[0]? results[0].votes: "null"}</h2>
                    </div>

                </div>
                <div to={"addParty"} type="button" class="btn btn-danger btn-lg btn-block"><div className='icons'><BsFill2CircleFill /></div>
                    <div className='stats'>
                        <h3></h3>
                        <h2>votes </h2>
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
                            {
                                results.map((result, index) => {
                                    return (
                                        <tr key={index}>
                                            <td>{index + 1}</td>
                                            <td>{result.regNo}</td>
                                            <td>{parties[index].partyName}</td>
                                            <td>{parties[index].electionSign}</td>
                                            <td>{result.votes}</td>
                                        </tr>
                                    )
                                })
                            }
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

