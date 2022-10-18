import styled from '@emotion/styled';

export const Container = styled.div<{ onClick: React.MouseEventHandler<HTMLDivElement> | undefined }>`
  min-width: fit-content;
  height: fit-content;
  border: 1px solid ${props => props.theme.colors.sub};
  border-radius: 15px;
  background-color: white;
  font-size: 13px;
  color: ${props => props.theme.colors.sub};
  padding: 6px 8px 5px 8px;
  box-sizing: border-box;
  user-select: none;
  cursor: ${props => (props.onClick ? 'pointer' : null)};

  display: flex;
  justify-content: center;
  align-items: center;

  @media (min-width: 875px) {
    font-size: 0.875rem;
  }
`;

export const Count = styled.span`
  color: ${props => props.theme.colors.gray_200};
  font-size: 11px;
  margin-left: 3px;

  @media (min-width: 875px) {
    font-size: 0.75rem;
  }
`;
