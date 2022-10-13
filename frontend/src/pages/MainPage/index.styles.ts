import styled from '@emotion/styled';

export const MainPageContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 5rem;
  padding: 1rem 0 5em 0;
  margin-top: 3rem;
  @media (max-width: 875px) {
    gap: 3rem;
  }
`;

export const BoardItemContainer = styled.div`
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 50px;
  @media (max-width: 875px) {
    flex-direction: column;
    align-items: center;
  }
`;
