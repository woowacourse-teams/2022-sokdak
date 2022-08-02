import { css } from '@emotion/react';
import styled from '@emotion/styled';

export const ReportModalContainer = styled.form`
  background-color: white;
  padding: 1em;
  box-shadow: 0px 1px 7px ${props => props.theme.colors.gray_150};
`;

export const Title = styled.p`
  font-weight: 700;
  font-size: 1.5rem;
  margin-bottom: 1rem;
`;

export const Notice = styled.p`
  font-weight: 400;
  font-size: 0.8rem;
  margin-bottom: 1.8rem;
  color: ${props => props.theme.colors.gray_200};
`;

export const Message = styled.textarea`
  resize: none;
  width: 15rem;
  height: 10rem;
  border: 1px solid ${props => props.theme.colors.gray_50};
  margin-bottom: 1.5rem;
`;

export const ButtonContainer = styled.div`
  display: flex;
  justify-content: flex-end;
`;

const button = () => css`
  width: 3.8rem;
  height: 28px;
  border-radius: 4px;
  font-size: 10px;
  margin-left: 10px;
`;

export const ConfirmButton = styled.button`
  ${button}

  background: ${props => props.theme.colors.sub};
  color: white;
`;

export const CancelButton = styled.button`
  ${button}

  background: white;
  color: ${props => props.theme.colors.sub};
`;
