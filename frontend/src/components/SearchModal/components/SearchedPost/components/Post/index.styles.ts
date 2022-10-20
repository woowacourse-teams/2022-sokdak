import styled from '@emotion/styled';

export const Container = styled.button`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 100px;
  background-color: transparent;
  padding: 0;
`;

export const Title = styled.p`
  font-size: 1rem;
  font-weight: 700;

  .highlight {
    color: ${props => props.theme.colors.sub};
    font-weight: 700;
  }
`;

export const Content = styled.p`
  font-size: 1rem;

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
  color: ${props => props.theme.colors.sub};
`;
