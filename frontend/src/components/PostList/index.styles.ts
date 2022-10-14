import styled from '@emotion/styled';

export const Container = styled.div`
  display: grid;
  width: 100%;
  grid-template-columns: 1fr 1fr;
  align-items: center;
  gap: 2em;
  position: relative;

  @media (max-width: 875px) {
    display: flex;
    flex-direction: column;
  }
`;

export const PostItemContainer = styled.div`
  width: 95%;
  box-sizing: border-box;
  @media (max-width: 875px) {
    width: 100%;
  }
`;
