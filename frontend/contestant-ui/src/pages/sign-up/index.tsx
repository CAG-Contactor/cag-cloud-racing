import * as React from "react"
import { Row, Col, InputGroup, FormControl, Form } from 'react-bootstrap'
import { useState } from 'react'
import { useDispatch } from 'react-redux';
import API from '../../BackendAPI'

const SignUp = () => {
    const [username, setUsername] = useState()
    const [password, setPassword] = useState()
    const [password2, setPassword2] = useState()
    const [errorMsg, setErrorMsg] = useState()

    const dispatch = useDispatch()

    const signUp = (e: any) => {
        e.preventDefault();

        if (password !== password2) {
            setErrorMsg("Password doesn't match")
        } else {
            const data = JSON.stringify({
                name: username,
                password: password,
                type: "blahonga"
            })

            API.signUp(data).then((resp: any) => {
                if (resp.status === 201) {
                    signInUser(data)
                }
            }).catch((e: any) => {
                if (e.response.status === 409) {
                    setErrorMsg("Account with that username already exists")
                } else {
                    setErrorMsg(`Something went wrong, failed with msg: ${e.message}`)
                }
            })
        }
    }

    const signInUser = (data: any) => {
        API.signIn(data).then((resp: any) => {
            dispatch({ type: 'AUTH', payload: { isLoggedIn: true, user: resp.data } })
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
            <h1>Sign up</h1>
            <Form onSubmit={(e: any) => signUp(e)}>
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
                            <InputGroup className="mb-3">
                                <FormControl
                                    placeholder="Password again"
                                    aria-label="Password"
                                    aria-describedby="basic-addon1"
                                    type={"password"}
                                    required={true}
                                    onChange={(evt: React.ChangeEvent<HTMLSelectElement>) => setPassword2(evt.target.value)}
                                />
                            </InputGroup>

                            <button className="btn btn-lg btn-primary btn-block mb-3" type="submit">
                                Sign up
                            </button>
                            {
                                errorMsg && errorMsg ?
                                    <div className="alert alert-danger mt-3" role="alert">
                                        {errorMsg}
                                    </div> :
                                    ''
                            }
                        </Col>
                    </Row>
                </Form.Group>
            </Form>

        </div>
    )

}

export default SignUp