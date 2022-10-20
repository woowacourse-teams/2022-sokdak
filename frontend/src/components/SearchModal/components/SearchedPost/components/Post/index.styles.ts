import styled from '@emotion/styled';

export const Container = styled.button`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
  height: 100px;
  background-color: transparent;
  padding: 0;
  box-sizing: border-box;
  font-size: 0.8rem;

  @media (min-width: 875px) {
    max-width: 1200px;
    font-size: 1rem;
  }
`;

export const Title = styled.p`
  font-weight: 700;

  .highlight {
    color: ${props => props.theme.colors.sub};
    font-weight: 700;
  }
`;

export const Content = styled.p`
  .highlight {
    color: ${props => props.theme.colors.sub};
    font-weight: 700;
  }

  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 100%;
  text-align: start;
`;

export const URL = styled.p`
  color: ${props => props.theme.colors.gray_200};
  font-size: 0.8rem;

  @media (min-width: 875px) {
    font-size: 0.7rem;
  }
`;
