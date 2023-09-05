import React from 'react'

export default function ViewVoters() {
    return (
        <>
        <h2 className='text-center my-4'>List of Voters</h2>
            <div className='tableWrapper'>
                <div class="searchBar my-4">
                    <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search" />
                    <button class="btn btn-outline-success " type="submit">Search</button>
                </div>
                <table class="table table-hover">
                    <thead>
                        <tr>

                            <th scope="col">#</th>
                            <th scope="col">CNIC</th>
                            <th scope="col">Name</th>
                            <th scope="col">Voting Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Mark</td>
                            <td>Otto</td>
                            <td>@mdo</td>
                            <td>N/A</td>
                        </tr>

                    </tbody>
                </table>
            </div>
        </>
    )
}
