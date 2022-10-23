import styled from '@emotion/styled';

export const SidebarContainer = styled.div`
  width: 30%;
  height: calc(100% - 8rem);
  position: absolute;
  right: 0;
`;
export const Container = styled.div<{ position: number; height: number }>`
  display: flex;
  flex-direction: column;
  width: max-content;
  left: 0;
  box-sizing: border-box;
  transform: ${props =>
    props.height - props.position >= 0 ? `translateY(${props.position}px)` : `translateY(${props.height}px)`};
  transition: all 0.2s ease;
  gap: 30px;
`;
