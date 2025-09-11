import React, { useState } from 'react';
import { useMutation } from '@apollo/client/react';
import { gql } from '@apollo/client';
import { useNavigate } from 'react-router-dom';

const REGISTER_MUTATION = gql`
  mutation Register($email: String!, $password: String!) {
    register(email: $email, password: $password)
  }
`;

type RegisterMutationData = {
  register: string; // the returned JWT
};

type RegisterMutationVars = {
  email: string;
  password: string;
};

const Register: React.FC = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [registerUser, { loading, error }] = useMutation<RegisterMutationData, RegisterMutationVars>(REGISTER_MUTATION);
  console.log(registerUser)
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const res = await registerUser({ variables: { email, password } });
      localStorage.setItem('jwt', res.data?.register || '');
      navigate('/dashboard');
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div>
      {loading && <p>Registering...</p>}
      <form onSubmit={handleSubmit}>
        <div style={{display: 'flex', flexDirection: 'column', width: '250px', gap: '8px'}}>
          <input required value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" />
          <input required type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" />
        </div>
        <div style={{ paddingTop: '8px'}}>
          <button type="submit" disabled={loading}>Register</button>
        </div>
      </form>
      {error && <p style={{ color: 'red' }}>Error: {error.message}</p>}
    </div>
  );
};

export default Register;
