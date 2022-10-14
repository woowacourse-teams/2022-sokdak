import styled from '@emotion/styled';

export const Container = styled.div`
  display: grid;
  width: 100%;
  grid-template-columns: repeat(auto-fit, minmax(min(525px, 100%), 1fr));
  align-items: center;
  gap: 2em;
  position: relative;
  justify-items: center;
`;

export const PostItemContainer = styled.div`
  width: 100%;
  box-sizing: border-box;
  max-width: 900px;
  min-width: 320px;
`;
