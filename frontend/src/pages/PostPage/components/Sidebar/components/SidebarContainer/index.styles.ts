import styled from '@emotion/styled';

export const SidebarContainer = styled.div`
  width: 30%;
  height: calc(100% - 8rem);
  position: absolute;
  right: 0;
`;
export const Container = styled.div<{ position: number }>`
  display: flex;
  flex-direction: column;
  width: 100%;
  left: 0;
  box-sizing: border-box;
  transform: ${({ position }) => `translateY(${position}px)`};
  transition: all 0.2s ease;
  gap: 30px;
`;
