import React, { useState } from "react";
// import { useIntl } from 'react-intl';
import { useMutation } from "@apollo/client/react";
import { gql } from "@apollo/client";
import { Link, useNavigate } from "react-router-dom";

const LOGIN_MUTATION = gql`
  mutation Login($email: String!, $password: String!) {
    login(email: $email, password: $password)
  }
`;

// Define types
type LoginMutationResult = {
  login: string;
};

type LoginVariables = {
  email: string;
  password: string;
};

const Login: React.FC = () => {
  // const intl = useIntl();
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [login, { loading, error }] = useMutation<
    LoginMutationResult,
    LoginVariables
  >(LOGIN_MUTATION);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const { data } = await login({ variables: { email, password } });
      if (data?.login) {
        localStorage.setItem("jwt", data.login); // Store JWT token
        navigate("/dashboard"); // Redirect
      }
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div style={{display: 'flex', flexDirection: 'column', width: '250px', gap: '8px'}}>
          <input
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email"
          />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password"
          />
        </div>
        <div style={{ paddingTop: '8px' }}>
          <button type="submit" disabled={loading}>
            Login
          </button>
        </div>
      </form>
      <p>
        Don't have an account? <Link to="/register">Register</Link>
      </p>
      {error && <p style={{ color: "red" }}>Login failed: {error.message}</p>}
    </div>
  );
};

export default Login;
