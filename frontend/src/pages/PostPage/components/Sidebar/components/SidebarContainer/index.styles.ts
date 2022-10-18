import styled from '@emotion/styled';

export const Container = styled.div<{ position: number }>`
  display: flex;
  flex-direction: column;
  width: 30%;
  right: 0;
  box-sizing: border-box;
  position: absolute;
  transform: ${({ position }) => `translateY(${position}px);`};
  transition: all 0.2s ease;
  gap: 30px;
`;
