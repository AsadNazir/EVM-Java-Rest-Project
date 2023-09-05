import React from 'react'
import { Outlet } from 'react-router-dom'
import Navbar from './Navbar'

export default function VoterHome() {
    return (
        <div>
            <Navbar />
            <Outlet />

        </div>
    )
}
