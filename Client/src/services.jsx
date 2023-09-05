import React from 'react'
import "./routes.jsx"
import routes from './routes.jsx'


const services = {
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