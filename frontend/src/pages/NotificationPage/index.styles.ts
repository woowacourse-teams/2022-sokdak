import styled from '@emotion/styled';

export const NotificationPageContainer = styled.div`
  width: 90%;
  display: flex;
  flex-direction: column;
  align-items: center;

  @media (min-width: 875px) {
    width: 50%;
    margin-top: 10px;
  }
`;

export const Title = styled.h1`
  width: 100%;
  font-size: 2rem;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  text-align: left;
  padding: 0.5em 0;
`;

export const NotificationContainer = styled.div`
  width: 100%;
  margin: 0.3em;
`;

export const NotificationTime = styled.h2`
  font-size: 1.2rem;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
  margin: 0.7em 0;
`;

export const NotificationItemContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5em;
`;

export const EmptyContainer = styled.div`
  display: flex;
  width: 100%;
  height: 5rem;
  justify-content: center;
  align-items: center;
  font-family: 'BMHANNAAir', 'Noto Sans KR';
  font-size: 1rem;
  color: ${props => props.theme.colors.gray_300};
`;
