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
`;

export const Input = styled.textarea`
  width: 288px;
  height: 80px;
  border: 0.5px solid ${props => props.theme.colors.gray_400};
  padding: 10px;
  box-sizing: border-box;

  ::placeholder {
    font-size: 12px;
  }
`;

export const Controller = styled.div`
  width: 288px;
  height: 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 10px 0;
`;

export const SubmitButton = styled.button`
  width: 60px;
  height: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 11px;
  background-color: ${props => props.theme.colors.sub};
  color: white;
  border-radius: 3px;
`;
