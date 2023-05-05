import React, { Component } from 'react'
import { Navigate } from 'react-router-dom'
import { Container } from 'semantic-ui-react'
import AuthContext from '../context/AuthContext'

class UserPage extends Component {
  static contextType = AuthContext

  state = {
    isUser: true
  }

  componentDidMount() {
    const Auth = this.context
    const user = Auth.getUser()
    const isUser = user.data.rol[0] === 'USER'
    this.setState({ isUser })
  }

  handleInputChange = (e, { name, value }) => {
    this.setState({ [name]: value })
  }

  render() {
    if (!this.state.isUser) {
      return <Navigate to='/' />
    } else {
      return (
        <Container>
          Page
        </Container>
      )
    }
  }
}

export default UserPage