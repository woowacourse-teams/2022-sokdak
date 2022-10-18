import styled from '@emotion/styled';

export const Container = styled.div`
  width: 80%;
  margin: 3rem auto;

  display: flex;
  flex-direction: column;
  align-items: center;

  gap: 5rem;

  @media (max-width: 875px) {
    gap: 3rem;
  }
`;

export const BoardItemContainer = styled.div`
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  gap: 70px 0;
  justify-content: space-between;

  @media (max-width: 875px) {
    gap: 50px;
    flex-direction: column;
    align-items: center;
  }
`;
