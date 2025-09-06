import {
  ApolloClient,
  InMemoryCache,
  HttpLink,
  ApolloLink,
} from '@apollo/client';

// 1. Create the HTTP link to your backend
const httpLink = new HttpLink({
  uri: 'http://localhost:8080/graphql',
});

// 2. Create an auth middleware link (modern way)
const authLink = new ApolloLink((operation, forward) => {
  const token = localStorage.getItem('jwt');
  operation.setContext(({ headers = {} }) => ({
    headers: {
      ...headers,
      Authorization: token ? `Bearer ${token}` : '',
    },
  }));
  return forward(operation);
});

// 3. Combine auth and HTTP links
const link = ApolloLink.from([authLink, httpLink]);

// 4. Create the Apollo client
export const client = new ApolloClient({
  link,
  cache: new InMemoryCache(),
});
