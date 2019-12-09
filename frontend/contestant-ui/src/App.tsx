import React from 'react'
import { useState } from 'react'
import { Container, Row, Col } from 'react-bootstrap'

import './App.css'
import { HashRouter, Route, Link } from "react-router-dom"
import { Navbar, Nav } from 'react-bootstrap'
import Registration from './pages/registration'
import SignIn from './pages/sign-in'
import SignUp from './pages/sign-up'
import { useSelector } from 'react-redux';


const App: React.FC = () => {
  const isLoggedIn = useSelector((state: any) => state.loginState.isLoggedIn)
  console.log(useSelector((state: any) => state.loginState))

  return !isLoggedIn ? <AppAuthenticated /> : <AppUnAuthenticated />
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
          Sign Up
        </a>
      </Row>

    </Container>
  )
}

function AppAuthenticated() {
  return (
    <HashRouter>
      <div>
        <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
          <Navbar.Toggle aria-controls="responsive-navbar-nav" />
          <Navbar.Collapse id="responsive-navbar-nav">
            <Nav className="mr-auto">
              <Link to='/' className="nav-link">Home</Link>
              <Link to='/registration' className="nav-link">Registration</Link>
              <Link to='/race' className="nav-link">Race</Link>
              <Link to='/leaderboard' className="nav-link">Leaderboard</Link>
              <Link to='/my-races' className="nav-link">My races</Link>
            </Nav>
          </Navbar.Collapse>
        </Navbar>


        <Route path="/registration">
          <Registration />
        </Route>
        <Route path="/race">
          <Users />
        </Route>
        <Route path="/leaderboard">
          <Users />
        </Route>
        <Route path="/my-races">
          <Users />
        </Route>
        <Route exact path="/">
          <Home />
        </Route>

      </div>
    </HashRouter>
  )
}

function Home() {
  return <h2>Home</h2>;
}

function About() {
  return <h2>About</h2>;
}

function Users() {
  return <h2>Users</h2>;
}


export default App;
