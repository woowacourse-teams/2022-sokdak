import styled from '@emotion/styled';
import { Link } from 'react-router-dom';

export const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const TitleContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 100px;
`;

export const Title = styled.p`
  font-size: 24px;
  font-family: 'BMHANNAPro';
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 270px;
  white-space: nowrap;
`;

export const Date = styled.span`
  font-size: 10px;
  color: ${props => props.theme.colors.gray_200};
`;

export const ContentContainer = styled.div`
  min-height: 420px;
  border-bottom: 1px solid ${props => props.theme.colors.sub};
  margin-bottom: 25px;
`;

export const Content = styled.p`
  line-height: 25px;
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
  border-radius: 15px;
  font-size: 17px;
  width: 100px;
  height: 55px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
`;
