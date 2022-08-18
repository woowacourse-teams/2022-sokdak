import styled from '@emotion/styled';

export const Container = styled.label`
  display: flex;
  align-items: center;
  width: 50px;
  height: 35px;
  user-select: none;
  gap: 7px;
`;

export const CheckBox = styled.div`
  width: 13px;
  height: 13px;
  border: 2px solid ${props => props.theme.colors.gray_400};
  position: relative;
`;

export const HiddenCheckBox = styled.input`
  display: none;

  :checked + ${CheckBox}::after {
    content: '✔';
    font-size: 11px;
    color: ${props => props.theme.colors.sub};
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
`;

export const Label = styled.span`
  display: block;
  font-size: 12px;
  color: ${props => props.theme.colors.gray_200};
`;
