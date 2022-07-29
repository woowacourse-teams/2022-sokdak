import styled from '@emotion/styled';

export const Container = styled.div<{ onClick: React.MouseEventHandler<HTMLDivElement> | undefined }>`
  min-width: fit-content;
  height: 23px;
  border: 1px solid ${props => props.theme.colors.sub};
  border-radius: 15px;
  background-color: white;
  font-size: 13px;
  color: ${props => props.theme.colors.sub};
  padding: 6px;
  box-sizing: border-box;
  user-select: none;
  cursor: ${props => (props.onClick ? 'pointer' : null)};
`;
