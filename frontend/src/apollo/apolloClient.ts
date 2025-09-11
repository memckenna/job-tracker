import { ApolloClient, InMemoryCache, HttpLink, ApolloLink } from '@apollo/client';

const httpLink = new HttpLink({
  uri: 'http://localhost:8080/graphql',
});

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

const link = ApolloLink.from([authLink, httpLink]);

export const client = new ApolloClient({
  link,
  cache: new InMemoryCache(),
});
