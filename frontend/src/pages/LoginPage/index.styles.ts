import { Link } from 'react-router-dom';

import styled from '@emotion/styled';

export const LoginForm = styled.form`
  display: flex;
  width: 90%;
  height: 90vh;
  max-width: 450px;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

export const Heading = styled.h1`
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  font-size: 27px;
  margin: 40px 0 70px;
`;

export const SubmitButton = styled.button`
  width: 100%;
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  background-color: ${props => props.theme.colors.sub};
  color: white;
  border-radius: 15px;
  font-size: 17px;
  width: 100%;
  height: 55px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const SignUpLink = styled(Link)`
  display: inline;
  color: ${props => props.theme.colors.sub};
  cursor: pointer;
`;

export const SignUpText = styled.p`
  width: 100%;
  padding: 40px 0px 30px;
  text-align: center;
  cursor: default;
  border-bottom: 1px solid ${props => props.theme.colors.gray_50};
`;

export const HomeLink = styled(Link)`
  height: fit-content;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  font-size: 40px;
  color: ${props => props.theme.colors.sub};
`;
