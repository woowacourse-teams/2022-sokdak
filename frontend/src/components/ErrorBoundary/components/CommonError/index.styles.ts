import styled from '@emotion/styled';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100vw;
  height: 100vh;
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  line-height: 30px;
  font-size: 17px;
`;

export const Icon = styled.span`
  font-size: 40px;
  margin: 20px 0;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
`;

export const Information = styled.span`
  color: ${props => props.theme.colors.gray_200};
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  font-size: 15px;
`;
