import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css'
import { Navbar, Nav, NavItem } from 'react-bootstrap';


const App: React.FC = () => {
  return (
    <Router>
      <div>
        <Navbar bg="dark" variant="dark">
          <Navbar.Brand href="#home">Navbar</Navbar.Brand>
          <Nav className="mr-auto">
            <Nav.Link href="registration">Registration</Nav.Link>
            <Nav.Link href="sign-in">Sign-in</Nav.Link>
            <Nav.Link href="race">Race</Nav.Link>
            <Nav.Link href="leaderboard">Leaderboard</Nav.Link>
            <Nav.Link href="my-races">My Races</Nav.Link>
          </Nav>
        </Navbar>

        {/* A <Switch> looks through its children <Route>s and
            renders the first one that matches the current URL. */}
        <Switch>
          <Route path="/registration">
            <About />
          </Route>
          <Route path="/sign-in">
            <Users />
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
          <Route path="/">
            <Home />
          </Route>
        </Switch>
      </div>
    </Router>
  );

  function Home() {
    return <h2>Home</h2>;
  }

  function About() {
    return <h2>About</h2>;
  }

  function Users() {
    return <h2>Users</h2>;
  }
}

export default App;
