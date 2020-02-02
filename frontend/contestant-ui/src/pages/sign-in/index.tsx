import * as React from "react"
import { Row, Col, InputGroup, FormControl, Form } from 'react-bootstrap'
import { useState } from 'react'
import { useDispatch } from 'react-redux'
import API from '../../BackendAPI'

const SignIn = () => {
    const [username, setUsername] = useState()
    const [password, setPassword] = useState()
    const [errorMsg, setErrorMsg] = useState()
    const [loading, setLoading] = useState(false)
    const dispatch = useDispatch()

    const signIn = (e: any) => {
        e.preventDefault();
        setLoading(true)
        const data = JSON.stringify({
            name: username,
            password: password,
            type: "CONTESTANT"
        })

        API.signIn(data).then((resp: any) => {
            dispatch({ type: 'AUTH', payload: { isLoggedIn: true, user: resp.data } })
            localStorage.setItem("token", resp.data.token)
            localStorage.setItem("username", resp.data.userName)

        }).catch((e: any) => {
            if (e.response.status === 401) {
                setErrorMsg("Username or password is wrong. Please try again")
            } else {
                setErrorMsg(`Something went wrong, failed with msg: ${e.message}`)
            }
        });
    }

    return (
        <div>
            <h1>Sign in</h1>
            <Form onSubmit={(e: any) => signIn(e)}>
                <Form.Group>
                    <Row className="justify-content-center">
                        <Col md={3} xs={12}>
                            <InputGroup className={"mb-3"}>
                                <FormControl
                                    placeholder="Username"
                                    aria-label="Username"
                                    aria-describedby="basic-addon1"
                                    required={true}
                                    onChange={(evt: React.ChangeEvent<HTMLSelectElement>) => setUsername(evt.target.value)}
                                />
                            </InputGroup>
                            <InputGroup className="mb-3">
                                <FormControl
                                    placeholder="Password"
                                    aria-label="Password"
                                    aria-describedby="basic-addon1"
                                    type={"password"}
                                    required={true}
                                    onChange={(evt: React.ChangeEvent<HTMLSelectElement>) => setPassword(evt.target.value)}
                                />
                            </InputGroup>

                            {loading && <p className="loading-indicator">Signing in...</p>}
                            {!loading &&
                                <button className="btn btn-lg btn-primary btn-block mb-3" type="submit">
                                    Sign in
                            </button>
                            }
                            {
                                errorMsg ?
                                    <div className="alert alert-danger mt-3" role="alert">
                                        {errorMsg}
                                    </div> :
                                    ''
                            }
                        </Col>
                    </Row>

                </Form.Group>
            </Form>
        </div >
    )
}


export default SignIn