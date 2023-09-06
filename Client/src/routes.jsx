import React from 'react'

const routes = {
    getAllVoters: "http://localhost:8888/EVM_2_war_exploded/admin/getAllVoters",
    getAllParties: "http://localhost:8888/EVM_2_war_exploded/admin/getAllParties",
    deleteParty: "http://localhost:8888/EVM_2_war_exploded/admin/deleteParty",
    deleteVoter: "http://localhost:8888/EVM_2_war_exploded/admin/deleteVoter",
    getVotingTime: "http://localhost:8888/EVM_2_war_exploded/admin/getVotingTime",
    isVoter: "http://localhost:8888/EVM_2_war_exploded/voter/isVoter",
    getVotingStatusVoter: "http://localhost:8888/EVM_2_war_exploded/voter/isVotingReady",
    getVotingStatusAdmin: "http://localhost:8888/EVM_2_war_exploded/admin/isVotingReady",
    isAdmin: "http://localhost:8888/EVM_2_war_exploded/admin/isAdmin",
    getResultsAdmin: "",
    getResultsVoter: "",
    votingStarted: "http://localhost:8888/EVM_2_war_exploded/voter/isVotingReady",
    getVoteCount: "http://localhost:8888/EVM_2_war_exploded/admin/getVoteCount",
    startVoting: "http://localhost:8888/EVM_2_war_exploded/admin/startVoting",
    adminLogin: "http://localhost:8888/EVM_2_war_exploded/auth/adminLogin",
    voterLogin: "http://localhost:8888/EVM_2_war_exploded/auth/login",
    castVote: "http://localhost:8888/EVM_2_war_exploded/voter/vote",
    regsiterVoter: "http://localhost:8888/EVM_2_war_exploded/auth/register",
    addParty: "http://localhost:8888/EVM_2_war_exploded/admin/addParty",
    
}
//define api routes only
export default routes;
