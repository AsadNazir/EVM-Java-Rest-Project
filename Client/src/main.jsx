import React, { useEffect } from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import Login from './Components/Login.jsx'
import { RouterProvider } from 'react-router-dom'
import { createBrowserRouter } from 'react-router-dom'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import Register from './Components/Register.jsx'
import AdminPage from './Components/AdminPage.jsx'
import VoterPage from './Components/VoterPage.jsx'
import AdminHome from './Components/AdminHome.jsx'
import AddParty from './Components/AddParty.jsx'
import ViewResults from './Components/ViewResults.jsx'
import ViewVoters from './Components/ViewVoters.jsx'
import AdminLogin from './Components/AdminLogin.jsx'
import VoterHome from './Components/VoterHome.jsx'

//Main Router Browser Router
const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    children: [
      {
        path: "/",
        element: <Login />
      },
      {
        path: "/adminLogin",
        element: <AdminLogin />
      },
      {
        path: "/register",
        element: <Register />
      },
      {
        path: "/admin",
        element: <AdminPage />,
        children: [
          {
            path: "",
            element: <AdminHome />,
          },
          {
            path: "addParty",
            element: <AddParty />,
          },
          {
            path: "viewResults",
            element: <ViewResults/>,
          },
          {
            path: "viewVoters",
            element: <ViewVoters/>,
          },
        ]
        
      },
      {
        path: "/voter",
        element: <VoterHome />,
        children: [
          {
            path: "",
            element: <VoterPage />,
          },
        ]
        

      }
      
    ]
  }
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router}/>
  </React.StrictMode>,
)
