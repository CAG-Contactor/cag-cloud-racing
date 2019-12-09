import * as React from "react"
import { Container } from 'react-bootstrap'
import API from "../../BackendAPI"
import { useSelector } from 'react-redux';
import { Button } from 'react-bootstrap';
import { useEffect, useState } from 'react'

const styles = {
  heading: {
    textAlign: "center"
  },
  text: {
    fontSize: 13,
    fontWeight: 600
  },
  queue: {
    listStyle: "none"
  }
}

const Queue = () => {
  const user = useSelector((state: any) => state.loginState.user)
  const [queue, setQueue] = useState([])

  useEffect(() => {
    API.getQueue().then((resp: any) => {
      setQueue(resp.data)
    }).catch((e) => {

    })
  }, []);

  const addMeInQueue = () => {
    API.addMeInQueue({ name: user.userName }).then((resp) => {

    }).catch((e) => {
      console.log(e)
    })
  }

  const bailOutFromQueue = () => {
    API.bailOutFromQueue({ name: user.userName }).then((resp) => {

    }).catch((e) => {
      console.log(e)
    })
  }

  function SignUpForRace() {
    return (
      <div className={"mb-5"}>
        <p style={styles.text}>Sign up for queue and wait for your turn to race. You will get an notification when it's time to race!</p>
        <Button size={"lg"} onClick={addMeInQueue} variant="primary">Sign up for race</Button>
      </div>
    )
  }

  function LeaveQueue() {
    return (
      <div className={"mb-5"}>
        <p style={styles.text}>Leave queue</p>
        <Button size={"lg"} onClick={bailOutFromQueue} variant="primary">Bail out</Button>
      </div>
    )
  }

  function Queue() {
    let position = 1

    return (
      <table className="center table table-striped">
        <thead>
          <tr>
            <th>Queue Number</th>
            <th>Name</th>
          </tr>
        </thead>
        <tbody>
          {queue.map((q: any, index) =>
            <tr key={index}>
              <td>{position++}</td>
              <td>{q.userName}</td>
            </tr>)}
        </tbody>
      </table>)
  }

  return (
    <Container style={{ textAlign: "center" }}>
      <h1>Queue</h1>

      <SignUpForRace />
      <LeaveQueue />

      <Queue />
    </Container>
  )
}

export default Queue