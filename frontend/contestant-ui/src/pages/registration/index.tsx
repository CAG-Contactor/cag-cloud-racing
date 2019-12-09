import * as React from "react"
import { useEffect } from 'react'
import { Container } from 'react-bootstrap'

const styles = {
  container: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center'
  }
}

interface RegistrationProps {

}

const Registration: React.FC<RegistrationProps> = (props) => {
  useEffect(() => {
    fetch('https://swapi.co/api/starships')
      .then(response => response.json())
      .then(response => {
        console.log(response)
      })
      .catch(error => {
        console.log(error)
      });
  }, []);

  return (
    <Container style={styles.container}>
      <h1>Registration</h1>

    </Container>
  )
}

export default Registration