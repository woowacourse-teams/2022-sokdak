import styled from '@emotion/styled';

export const Button = styled.button<{ isLiked: boolean }>`
  display: flex;
  justify-content: center;
  align-items: center;
  min-width: 50px;
  height: 30px;
  border-radius: 15px;
  border: 1px solid ${props => (props.isLiked ? props.theme.colors.pink_300 : props.theme.colors.gray_300)};
  gap: 4px;
  background-color: inherit;
  font-size: 0.7rem;
  color: ${props => (props.isLiked ? props.theme.colors.pink_300 : props.theme.colors.gray_300)};

  @media (min-width: 875px) {
    font-size: 0.8rem;
  }
`;
