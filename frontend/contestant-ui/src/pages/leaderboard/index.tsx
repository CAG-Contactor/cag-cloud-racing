import * as React from "react"
import { Container } from 'react-bootstrap'
import API from "../../BackendAPI"
import { useEffect, useState } from 'react'
import Moment from 'react-moment';

const Leaderboard = () => {
    const [leaderboard, setLeaderboard] = useState([])

    useEffect(() => {
        API.getLeaderboard().then((resp: any) => {
            setLeaderboard(resp.data)
        }).catch((e) => {

        })
    }, []);

    const getResultText = (type: string) => {
        switch (type) {
            case 'FINISHED':
                return 'Finished';
            case 'WALKOVER':
                return 'Walkover';
            default:
                return 'Disqualified'
        }
    };

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
                    {leaderboard.map((user: any, index) =>
                        <tr key={index}>
                            <td>{user.userName}</td>
                            <td><Moment format="mm:ss:SSS">{user.finishTime}</Moment></td>
                            <td><Moment format="mm:ss:SSS">{user.splitTime}</Moment></td>
                            <td>{getResultText(user.raceStatus)}</td>
                        </tr>)}
                </tbody>
            </table>)
    }

    return (
        <Container style={{ textAlign: "center" }}>
            <h1>Leaderboard</h1>
            <Leaderboard />
        </Container>
    )
}

export default Leaderboard
