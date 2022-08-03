import styled from '@emotion/styled';

export const DropdownContainer = styled.div`
  display: flex;
  position: relative;
  width: 100%;
  height: fit-content;
  justify-content: center;
  text-align: center;
`;

export const DropdownTrigger = styled.button`
  background-color: transparent;
  margin: auto;
  overflow: hidden;
`;

export const DropdownList = styled.ul`
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  top: 120%;
  z-index: 10;
`;
