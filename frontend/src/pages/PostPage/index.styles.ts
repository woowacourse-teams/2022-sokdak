import { Link } from 'react-router-dom';

import { css, Theme } from '@emotion/react';
import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const ContentContainer = styled.div`
  width: 100%;
  min-height: 420px;
  border-bottom: 1px solid ${props => props.theme.colors.sub};
  margin-bottom: 25px;
`;

export const Content = styled.p`
  min-height: 370px;
  line-height: 25px;
  white-space: pre-wrap;
`;

export const TagContainer = styled.div`
  min-height: 50px;
  float: left;
  display: flex;
  box-sizing: border-box;
  flex-wrap: wrap;
  gap: 5px;
  row-gap: 10px;
  padding: 15px 0;
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
