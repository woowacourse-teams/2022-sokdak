import styled from '@emotion/styled';

export const Container = styled.label<{ visible: boolean }>`
  display: flex;
  align-items: center;
  width: fit-content;
  height: 35px;
  user-select: none;
  gap: 7px;
  visibility: ${props => (props.visible ? 'visible' : 'hidden')};
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
    font-size: 0.68rem;
    color: ${props => props.theme.colors.sub};
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
`;

export const Label = styled.span`
  display: block;
  font-size: 0.75rem;
  color: ${props => props.theme.colors.gray_200};

  @media (min-width: 875px) {
    font-size: 0.8rem;
  }
`;
