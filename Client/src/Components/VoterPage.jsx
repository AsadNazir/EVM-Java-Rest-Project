import React, { useEffect } from 'react'
import { Outlet, useNavigate } from 'react-router-dom'
import { BsFillPersonPlusFill} from "react-icons/bs";
import {LuVote} from "react-icons/lu"
import { GiMedallist } from "react-icons/gi"
import { Link } from 'react-router-dom';
import "../CSS/adminPage.css"
import { useState } from 'react';
import "../CSS/voterPage.css"
import services from '../services';

export default function VoterPage() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [votingStarted, setVotingStarted] = useState(false);

  useEffect(() => {
    
    async function checkVoter() {
      const isVoter = await services.isVoter().then(data => data);
      if (!isVoter) {
        navigate("/");
      }
    }

    checkVoter();
  }, [])

  return (
    <>
      {votingStarted ? <h1 className='votingStatus'>Voting Started</h1> : <h1 className='votingStatus'>Voting has not Started</h1>}
      <div className='VoterPageWrapper'>
        <Link to={"castVoter"} type="button" class="btn btn-primary btn-lg btn-block"><span className='icons'><LuVote /></span><h4>Cast Vote</h4></Link>
        <Link to={"viewResults"} type="button" class="btn btn-secondary btn-lg btn-block"><span className='icons'><GiMedallist /></span><h4>View Results</h4></Link>
      </div>
    </>
  )
}
