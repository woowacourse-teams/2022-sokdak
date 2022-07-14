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

export const Avartar = styled.div`
  width: 40px;
  height: 40px;
  border-radius: 100%;
  border: 1px solid ${props => props.theme.colors.main};
`;
