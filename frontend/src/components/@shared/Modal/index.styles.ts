import styled from '@emotion/styled';

export const Modal = styled.div`
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 20px;
  z-index: 20;
`;

export const Dimmer = styled.div`
  width: 100%;
  height: 100vh;
  background-color: black;
  opacity: 0.2;
  position: fixed;
  top: 0px;
  left: 0px;
  z-index: 10;
`;
