import { Link } from 'react-router-dom';

import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const ListButtonContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: flex-end;
`;

export const ListButton = styled(Link)`
  font-family: 'BMHANNAAir';
  background-color: ${props => props.theme.colors.sub};
  color: white;
  border-radius: 8px;
  font-size: 13px;
  padding: 4% 6%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const SpinnerContainer = styled.div`
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translateY(-50%);
  transform: translateX(-50%);
`;

export const ErrorContainer = styled.div`
  width: 100%;
  height: 500px;
  font-family: 'BMHANNAPro';
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 3em;
`;
