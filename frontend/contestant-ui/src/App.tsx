import React from 'react'
import { useState, useEffect } from 'react'
import { Container, Row } from 'react-bootstrap'
import './App.css'
import { HashRouter, Route, Link } from "react-router-dom"
import { Navbar, Nav } from 'react-bootstrap'
import { useSelector } from 'react-redux';
import Queue from './pages/queue'
import SignIn from './pages/sign-in'
import SignUp from './pages/sign-up'
import Leaderboard from './pages/leaderboard'
import Races from './pages/races'
import Home from './pages/home'
import { useDispatch } from 'react-redux'

const App = () => {
  const dispatch = useDispatch()

  useEffect(() => {
    const username = localStorage.getItem("username")
    dispatch({ type: 'SET_USERNAME', payload: { user: { userName: username } } })
  }, []);

  const isLoggedIn = useSelector((state: any) => state.loginState.isLoggedIn)

  const hasToken = localStorage.getItem("token");

  return (isLoggedIn || !!hasToken) ? <AppAuthenticated /> : <AppUnAuthenticated />
}

function AppUnAuthenticated() {
  const [showSignUp, setShowSignUp] = useState(false)

  return (
    <Container style={{ textAlign: 'center' }}>
      {showSignUp ?
        <SignUp /> :
        <SignIn />}

      <Row className="justify-content-center">
        <a href="#" onClick={() => setShowSignUp(!showSignUp)}>
          {showSignUp ? 'Sign in' : 'Sign up'}
        </a>
      </Row>

    </Container>
  )
}

function AppAuthenticated() {
  function logout() {
    localStorage.removeItem("token")
    localStorage.removeItem("username")
    window.location.reload()
  }

  return (
    <HashRouter>
      <div>
        <Navbar style={{ backgroundColor: 'black' }} collapseOnSelect expand="lg"  variant="dark">
          <Navbar.Toggle aria-controls="responsive-navbar-nav" />
          <Navbar.Collapse id="responsive-navbar-nav">
            <Nav className="mr-auto">
              <Nav.Item>
                <Nav.Link eventKey="1" as={Link} to="/">
                  Home
                </Nav.Link>
              </Nav.Item>
              <Nav.Item>
                <Nav.Link eventKey="2" as={Link} to="/queue">
                  Queue
                </Nav.Link>
              </Nav.Item>
              <Nav.Item>
                <Nav.Link eventKey="3" as={Link} to="/leaderboard">
                  Leaderboard
                </Nav.Link>
              </Nav.Item>
              <Nav.Item>
                <Nav.Link eventKey="4" as={Link} to="/my-races">
                  My races
                </Nav.Link>
              </Nav.Item>
            </Nav>
          </Navbar.Collapse>
          <Link to='/my-races' className="nav-link float-right" onClick={logout}>Logout</Link>
        </Navbar>


        <Route path="/queue">
          <Queue />
        </Route>
        <Route path="/leaderboard">
          <Leaderboard />
        </Route>
        <Route path="/my-races">
          <Races />
        </Route>
        <Route exact path="/">
          <Home />
        </Route>
      </div>
    </HashRouter>
  )
}


export default App;
