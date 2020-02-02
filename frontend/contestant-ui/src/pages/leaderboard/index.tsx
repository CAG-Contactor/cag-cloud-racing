import * as React from "react"
import { Container, Row } from 'react-bootstrap'
import API from "../../BackendAPI"
import { useEffect, useState } from 'react'
import Moment from 'react-moment';

const Leaderboard = () => {
    const [leaderboard, setLeaderboard] = useState([])

    useEffect(() => {
        fetchLeaderboard()

        const oneMin = 60000
        const interval = setInterval(() => fetchLeaderboard(), oneMin)

        return () => clearTimeout(interval)
    }, []);

    const fetchLeaderboard = () => {
        API.getLeaderboard().then((resp: any) => {
            setLeaderboard(resp.data)
        });
    }

    const getResultText = (type: string) => {
        switch (type) {
            case 'FINISHED':
                return 'Finished';
            case 'WALKOVER':
                return 'Walkover';
            default:
                return 'N/A'
        }
    };

    function Leaderboard() {
        return (
            <table style={{ color: '#4cff2c' }} className="center table table-striped">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Time</th>
                        <th>Split</th>
                        <th>Result</th>
                    </tr>
                </thead>
                <tbody>
                    {leaderboard.map((user: any, index) =>
                        <tr key={index}>
                            <td>{user.userName}</td>
                            <td><Moment format="mm:ss:SSS">{user.finishTime - user.startTime}</Moment></td>
                            <td><Moment format="mm:ss:SSS">{user.splitTime - user.startTime}</Moment></td>
                            <td>{getResultText(user.raceStatus)}</td>
                        </tr>)}
                </tbody>
            </table>)
    }

    return (
        <Container style={{ textAlign: "center" }} className="mt-2">
            <h1>Leaderboard</h1>
            <Row>
                <Leaderboard />
            </Row>
        </Container>
    )
}

export default Leaderboard
