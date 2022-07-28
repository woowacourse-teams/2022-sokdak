import { Link } from 'react-router-dom';

import styled from '@emotion/styled';

export const Container = styled.header`
  width: 350px;
  height: 50px;
  display: flex;
  justify-content: space-between;
  margin: 15px auto;
  box-sizing: border-box;
`;

export const LeftSide = styled(Link)`
  display: flex;
  align-items: center;
  gap: 8px;
`;

export const RightSide = styled.div`
  display: flex;
  align-items: center;
  gap: 6px;
`;

export const Title = styled.p`
  font-size: 1.5rem;
  font-family: 'BMYEONSUNG';
`;

export const Avartar = styled.button`
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 100%;
  background-color: inherit;
  border: 1px solid ${props => props.theme.colors.main};
  cursor: pointer;
  font-family: 'BMHANNAPro';
`;

export const LoginLink = styled(Link)`
  display: flex;
  justify-content: center;
  align-items: center;

  width: 50px;
  height: 20px;
  font-family: 'BMHANNAPro';
  font-size: 12px;
  color: ${props => props.theme.colors.gray_900};
  border: 1px solid black;
  border-radius: 8px;
`;
