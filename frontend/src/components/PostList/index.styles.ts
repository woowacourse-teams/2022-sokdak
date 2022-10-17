import styled from '@emotion/styled';

export const Container = styled.div`
  display: grid;
  width: 100%;
  grid-template-columns: repeat(auto-fit, minmax(min(525px, 50%), 1fr));
  align-items: center;
  gap: 2em;
  position: relative;
  justify-items: center;
  @media (max-width: 1250px) and (min-width: 875px) {
    grid-template-columns: repeat(2, 1fr);
  }
`;

export const PostItemContainer = styled.div`
  width: 100%;
  box-sizing: border-box;
  min-width: 320px;
`;
