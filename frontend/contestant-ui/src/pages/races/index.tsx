import * as React from "react"
import { Container } from 'react-bootstrap'
import API from "../../BackendAPI"
import { useEffect, useState } from 'react'
import Moment from 'react-moment';
import { useSelector } from 'react-redux';

const Races = () => {
    const [races, setRaces] = useState([])
    const user = useSelector((state: any) => state.loginState.user)

    useEffect(() => {
        API.getRaces(user.userName).then((resp: any) => {
            setRaces(resp.data)
        }).catch((e) => {
        })
    }, [user.userName]);

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

    function Races() {
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
                    {races.map((race: any, index) =>
                        <tr key={index}>
                            <td>{race.userName}</td>
                            <td><Moment format="mm:ss:SSS">{race.finishTime}</Moment></td>
                            <td><Moment format="mm:ss:SSS">{race.splitTime}</Moment></td>
                            <td>{getResultText(race.raceStatus)}</td>
                        </tr>)}
                </tbody>
            </table>)
    }

    return (
        <Container style={{ textAlign: "center" }}>
            <h1>My Races</h1>
            <Races />
        </Container>
    )
}

export default Races
