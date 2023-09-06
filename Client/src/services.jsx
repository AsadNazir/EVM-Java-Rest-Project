import React from 'react'
import "./routes.jsx"
import routes from './routes.jsx'


const services = {

    getVotingStatus: async () => {
        //get voting status from database

        if(sessionStorage.getItem("token") === null){
            return false;
        }
        
        let result = await fetch(routes.getVotingStatusAdmin, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + sessionStorage.getItem("token")
            }
        })

        if
        (result.status === 401) {
            alert('Session Expired');
            return;
        }

        let data = await result.json();

        if (data.error === false) {
            return false;
        }

        return data.data;

    },




    isVoter: async () => {
        if(sessionStorage.getItem("token") === null){
            return false;
        }

        const response = await fetch(routes.isVoter, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + sessionStorage.getItem("token")
            },

        })

        if (response.status === 401) {
            return false;
        }

        return response.json().then(data => {
            return !data.error
        })
    },

    isAdmin: async () => {
        if(sessionStorage.getItem("token") === null){
            return false;
        }

        const response = await fetch(routes.isAdmin, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": "Bearer " + sessionStorage.getItem("token")
            },

        })

        if (response.status === 401) {
            return false;
        }

        return response.json().then(data => {
            return !data.error
        })
    },

}

export default services;