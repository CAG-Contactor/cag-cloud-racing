import * as React from "react"
import { Container } from 'react-bootstrap'

const Home = () => {
    return (
        <Container style={{ textAlign: "center" }} className="mt-2">
            <h1>C.A.G RACE</h1>
            <div style={{ color: '#4cff2c', paddingLeft: '.5rem', paddingRight: '.5rem' }} className="text-justify">
                <p>
                    In this game you will try to get through a labyrinth as fast as possible, by steering a ball via an app.
                    The time will start to run as soon as you advance through the first passage. Thereafter you will receive
                    a split time when you pass the 2nd passage.
                    The third passage is the finishing line, after passing it you will see your finishing time.
              </p>
                <p>
                    To participate in the competition you need to register a user, log in and finally register yourself to
                    the racing queue.
              </p>
                This game is built using technologies we have been learning at our techdays through the last year.
                For more information, visit us at the booth.
            </div>
        </Container >
    )
}

export default Home