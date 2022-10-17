import styled from '@emotion/styled';

const Layout = styled.section`
  width: calc(100% - 3rem);
  min-width: 375px;
  display: flex;
  justify-content: center;
  padding: 0 3rem;
  box-sizing: border-box;
  margin: auto;
  @media (max-width: 875px) {
    width: 100%;
    padding: 1em;
  }
`;

export default Layout;
