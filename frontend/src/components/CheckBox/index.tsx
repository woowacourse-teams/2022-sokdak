import * as Styled from './index.styles';

interface CheckBoxProps {
  className?: string;
  isChecked: boolean;
  setIsChecked: React.Dispatch<React.SetStateAction<boolean>>;
  labelText: string;
}

const CheckBox = ({ className, isChecked, setIsChecked, labelText }: CheckBoxProps) => {
  return (
    <Styled.Container className={className}>
      <Styled.HiddenCheckBox
        type="checkbox"
        checked={isChecked}
        onChange={e => setIsChecked(e.currentTarget.checked)}
      />
      <Styled.CheckBox />
      <Styled.Label>{labelText}</Styled.Label>
    </Styled.Container>
  );
};

export default CheckBox;
