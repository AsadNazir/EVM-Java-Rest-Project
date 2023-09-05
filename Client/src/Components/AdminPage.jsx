import React, { useEffect } from 'react'
import "../CSS/adminPage.css"
import { useState } from 'react';
import { BsFillPersonPlusFill, BsHourglassTop, BsPeopleFill } from "react-icons/bs";
import { GiMedallist } from "react-icons/gi"
import Table from './Table';
import Navbar from './Navbar';
import { Outlet } from 'react-router-dom';

export default function AdminPage() {
   
    return (
        <>
            <Navbar />
           <Outlet />

        </>
    )
}
