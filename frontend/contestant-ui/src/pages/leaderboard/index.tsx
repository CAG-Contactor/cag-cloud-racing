import * as React from "react"
import { Container } from 'react-bootstrap'
import API from "../../BackendAPI"
import { useSelector } from 'react-redux';
import { Button } from 'react-bootstrap';
import { useEffect, useState } from 'react'

const styles = {
  heading: {
    textAlign: "center"
  }
}

const Leaderboard = () => {
  const [leaderboard, setLeaderboard] = useState([])

  const getLeaderboard = () => {
    API.getLeaderboard().then((resp: any) => {
      setLeaderboard(resp.data)
    })
  }

  useEffect(() => {
    getLeaderboard()
  }, []);

  function Leaderboard() {
    return (
      <table className="center table table-striped">
        <thead>
          <tr>
            <th>Name</th>
            <th>Time</th>
            <th>Split</th>
            <th>Result</th>
          </tr>
        </thead>
        <tbody>
          {leaderboard.map((race: any, index) =>
            <tr key={index}>
              <td>name...</td>
              <td>time...</td>
              <td>split time...</td>
              <td>result text...</td>
            </tr>)}
        </tbody>
      </table>)
  }

  return (
    <Container style={{ textAlign: "center" }}>
      <h1 className="mb-4">Leaderboard</h1>

      <Leaderboard />

    </Container>
  )
}

export default Leaderboard