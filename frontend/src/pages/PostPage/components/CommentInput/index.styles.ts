import styled from '@emotion/styled';

export const Form = styled.form``;

export const CommentCount = styled.p`
  font-size: 20px;
  font-family: 'BMHANNAPro', 'Noto Sans KR';
`;

export const ContentInput = styled.textarea`
  width: calc(100% - 20px);
  height: 100px;
  padding: 10px;
  font-size: 14px;
  margin: 20px 0 0 0;
  border: 1px solid ${props => props.theme.colors.gray_400};
`;

export const CheckBoxContainer = styled.div`
  display: flex;
  align-items: center;
`;

export const CheckBox = styled.input``;

export const Label = styled.label`
  font-size: 12px;
  color: ${props => props.theme.colors.gray_200};
`;

export const SubmitButton = styled.button`
  height: 2.5rem;
  padding: 0.8em 1.2rem;
  float: right;
  font-size: 0.9rem;
  background-color: ${props => props.theme.colors.sub};
  color: white;
  border-radius: 3px;
`;
