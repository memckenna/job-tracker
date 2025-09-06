import { gql } from '@apollo/client';

export const GET_APPLICATIONS = gql`
  query {
    getApplications {
      id
      position
      company
      status
      notes
      deadline
    }
  }
`;
