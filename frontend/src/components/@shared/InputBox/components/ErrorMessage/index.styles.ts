import styled from '@emotion/styled';

export const MessageContainer = styled.div`
  width: 100%;
  padding: 10px;
  height: 10px;
`;

export const Message = styled.p`
  font-size: 12px;
  color: ${props => props.theme.colors.red_100};
`;
