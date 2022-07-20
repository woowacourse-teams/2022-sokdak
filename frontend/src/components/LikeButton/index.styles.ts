import styled from '@emotion/styled';

export const Button = styled.button<{ isLiked: boolean }>`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 60px;
  height: 30px;
  border-radius: 15px;
  border: 1px solid ${props => (props.isLiked ? props.theme.colors.pink_300 : props.theme.colors.gray_300)};
  gap: 4px;
  color: ${props => (props.isLiked ? props.theme.colors.pink_300 : props.theme.colors.gray_300)};
  background-color: inherit;
`;
