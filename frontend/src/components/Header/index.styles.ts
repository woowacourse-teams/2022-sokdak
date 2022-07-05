import styled from '@emotion/styled';
import { Link } from 'react-router-dom';

export const Container = styled.header`
  width: 100%;
  height: 50px;
  display: flex;
  justify-content: space-between;
  padding: 0 20px;
  margin: 15px 0;
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
