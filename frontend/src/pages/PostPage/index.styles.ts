import styled from '@emotion/styled';
import { Link } from 'react-router-dom';

export const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const SpinnerContainer = styled.div`
  width: 100%;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export const HeadContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 60px;
`;

export const TitleContainer = styled.div`
  width: 240px;
  word-wrap: break-word;
`;

export const Title = styled.p`
  font-size: 24px;
  font-family: 'BMHANNAPro';
  word-break: keep-all;
`;

export const Date = styled.span`
  min-width: 80px;
  text-align: right;
  font-size: 10px;
  color: ${props => props.theme.colors.gray_200};
`;

export const ContentContainer = styled.div`
  width: 100%;
  min-height: 420px;
  border-bottom: 1px solid ${props => props.theme.colors.sub};
  margin-bottom: 25px;
  padding-bottom: 20px;
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
