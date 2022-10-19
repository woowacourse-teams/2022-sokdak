import { Link } from 'react-router-dom';

import { css, Theme } from '@emotion/react';
import styled from '@emotion/styled';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  width: 100%;
  height: fit-content;
  border: 1px solid ${props => props.theme.colors.gray_400};
  padding: 30px;
  box-sizing: border-box;
  background-color: white;
  border-radius: 10px;
`;

export const Title = styled.h1`
  font-size: 24px;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  margin-bottom: 20px;
`;

export const Items = styled.ul`
  list-style-type: disc;
  list-style-position: inside;
  margin-top: 10px;
`;

export const Item = styled.li`
  margin: 15px 0;
`;

const linkStyle = (props: { theme: Theme }) => css`
  cursor: pointer;
  color: ${props.theme.colors.gray_200};
  margin-top: 2px;

  :hover {
    color: ${props.theme.colors.sub};
    border-bottom: 1px solid ${props.theme.colors.sub};
  }
`;

export const InternalLink = styled(Link)`
  ${linkStyle}
`;

export const ExternalLink = styled.a`
  ${linkStyle}
`;
