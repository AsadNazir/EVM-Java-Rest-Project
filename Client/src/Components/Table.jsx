import React, { useEffect } from 'react'
import "../CSS/table.css"

export default function Table() {

    const getParties = async () => {
        //get parties from database

    }

    const deleteParty = async () => {
        //delete party from database
    }

    useEffect(() => {
        getParties();
    }, [])


    return (
        <div className='tableWrapper'>
            <div class="searchBar my-4">
                <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search" />
                <button class="btn btn-outline-success " type="submit">Search</button>
            </div>
            <table class="table table-hover">
                <thead>
                    <tr>
                       
                        <th scope="col">Reg No</th>
                        <th scope="col">Name</th>
                        <th scope="col">Election Sign</th>
                        <th scope="col">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Mark</td>
                        <td>Otto</td>
                        <td>@mdo</td>
                        <td><button className='btn btn-danger' data-bs-toggle="modal" data-bs-target="#staticModal">Delete</button></td>
                    </tr>

                </tbody>
            </table>
        </div>
    )
}
