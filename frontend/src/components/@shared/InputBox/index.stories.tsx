import { useState } from 'react';

import Layout from '@/components/@styled/Layout';

import InputBox from '.';
import { useInput } from './useInput';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/InputBox',
  component: InputBox,
} as ComponentMeta<typeof InputBox>;

const Template: ComponentStory<typeof InputBox> = () => {
  const { value, setValue, error, setError } = useInput();
  const [isAnimationActive, setIsAnimationActive] = useState(false);
  const handleInvalid = () => {
    setError('정확한 이메일 형식을 입력해주세요');
  };
  return (
    <Layout>
      <InputBox
        value={value}
        setValue={setValue}
        error={error}
        setError={setError}
        isAnimationActive={isAnimationActive}
        setIsAnimationActive={setIsAnimationActive}
      >
        <form style={{ display: 'grid', gridTemplateColumns: '4fr 1fr', alignItems: 'center', gap: '8px' }}>
          <InputBox.Input type="email" placeholder="이메일" handleInvalid={handleInvalid} required />
          <InputBox.SubmitButton onClick={() => {}}>인증번호 받기</InputBox.SubmitButton>
        </form>
        <InputBox.ErrorMessage />
      </InputBox>
    </Layout>
  );
};

export const InputBoxTemplate = Template.bind({});
InputBoxTemplate.args = {};
