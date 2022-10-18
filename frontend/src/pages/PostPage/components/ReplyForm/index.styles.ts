import { css } from '@emotion/react';
import styled from '@emotion/styled';

export const Form = styled.form`
  width: 100%;
  padding-top: 24px;
  background-color: ${props => props.theme.colors.gray_5};
  display: flex;
  flex-direction: column;
  align-items: center;
  box-sizing: border-box;
  margin-bottom: -0.5px;
  z-index: 5;
  font-size: 0.75rem;
`;

export const Input = styled.textarea`
  width: 90%;
  height: 80px;
  border: 0.5px solid ${props => props.theme.colors.gray_400};
  padding: 10px;
  box-sizing: border-box;

  ::placeholder {
    font-size: 0.9em;
  }
`;

export const Controller = styled.div`
  width: 90%;
  height: 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 10px 0;
`;

export const ButtonContainer = styled.div`
  display: flex;
`;

const buttonStyle = css`
  width: 3.75rem;
  height: 1.5rem;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 0.68rem;
  border-radius: 3px;

  @media (min-width: 875px) {
    font-size: 0.8rem;
    width: 4.75rem;
    height: 1.8rem;
  }
`;

export const CancelButton = styled.button`
  ${buttonStyle};
  background-color: transparent;
  color: ${props => props.theme.colors.sub};
`;

export const SubmitButton = styled.button`
  ${buttonStyle};
  background-color: ${props => props.theme.colors.sub};
  color: white;
`;
