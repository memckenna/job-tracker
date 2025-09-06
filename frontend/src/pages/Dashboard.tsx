import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@apollo/client/react';
// import { GET_APPLICATIONS } from '../graphql/queries';
import { gql } from '@apollo/client';

const LOGIN_MUTATION = gql`
  mutation Login($email: String!, $password: String!) {
    login(email: $email, password: $password)
  }
`;

// ✅ Define TypeScript types for mutation result and variables
type LoginResult = {
  login: string; // JWT token string
};

type LoginVariables = {
  email: string;
  password: string;
};

const Login: React.FC = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  // ✅ Provide generics here
  const [login, { loading, error }] = useMutation<LoginResult, LoginVariables>(LOGIN_MUTATION);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const { data } = await login({ variables: { email, password } });

      // ✅ Now TypeScript knows data.login is a string
      if (data?.login) {
        localStorage.setItem('jwt', data.login);
        navigate('/dashboard');
      } else {
        alert('No token returned');
      }
    } catch (err) {
      console.error('Login failed:', err);
    }
  };

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <input value={email} onChange={(e) => setEmail(e.target.value)} placeholder="Email" />
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" />
        <button type="submit" disabled={loading}>Login</button>
      </form>
      {error && <p style={{ color: 'red' }}>Login failed: {error.message}</p>}
    </div>
  );
};

export default Login;


// const Dashboard: React.FC = () => {
//   const navigate = useNavigate();

//   // 🔐 Protect the route
//   useEffect(() => {
//     const token = localStorage.getItem('jwt');
//     if (!token) {
//       navigate('/');
//     }
//   }, [navigate]);

//   const { loading, error, data } = useQuery(GET_APPLICATIONS);

//   if (loading) return <p>Loading...</p>;
//   if (error) return <p style={{ color: 'red' }}>Error: {error.message}</p>;

//   return (
//     <div>
//       <h2>Dashboard</h2>
//       {data.getApplications.length === 0 ? (
//         <p>No job applications yet.</p>
//       ) : (
//         <ul>
//           {data.getApplications.map((app: any) => (
//             <li key={app.id}>
//               <strong>{app.position}</strong> at {app.company} — {app.status}
//               {app.deadline && <p>Deadline: {app.deadline}</p>}
//               {app.notes && <p>Notes: {app.notes}</p>}
//             </li>
//           ))}
//         </ul>
//       )}
//     </div>
//   );
// };

// export default Dashboard;
