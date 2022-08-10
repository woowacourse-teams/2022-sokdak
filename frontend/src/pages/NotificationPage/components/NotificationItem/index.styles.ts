import { Link } from 'react-router-dom';

import styled from '@emotion/styled';

export const ItemContainer = styled(Link)`
  border: 1px solid rgba(0, 0, 0, 0.05);
  box-shadow: 0px 0px 4px rgba(0, 0, 0, 0.15);
  padding: 1.3em 2em;
  padding-right: 1em;
  border-radius: 1rem;
  font-size: 0.8rem;
  display: flex;
  width: calc(100% - 3em);
  justify-content: space-between;
  margin-bottom: 0.5em;
`;

export const NotificationMessage = styled.p`
  line-height: 1.3em;
  margin: auto 0;
  word-break: break-all;
`;

export const Highlight = styled.span`
  color: ${props => props.theme.colors.sub};
  text-decoration: underline;
  margin: 0 0.3em;
`;

export const RedLight = styled.span`
  color: ${props => props.theme.colors.red_200};
  margin: 0 0.3em;
`;

export const DeleteButton = styled.button`
  color: ${props => props.theme.colors.gray_300};
  background-color: transparent;
  font-size: 1.2rem;
`;
