import styled from '@emotion/styled';

export const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const Error = styled.div`
  width: 100%;
  height: 500px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  gap: 2em;
`;

export const ErrorCode = styled.p`
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  font-size: 80px;
`;

export const HashTag = styled.p`
  width: 100%;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  font-size: 20px;
  margin: -8px 0 25px -35px;
`;
