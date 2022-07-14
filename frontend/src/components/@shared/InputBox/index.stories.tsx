import { useState } from 'react';

import Input from './components/Input';

import InputBox from '.';
import { useInput } from './useInput';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/InputBox',
  component: InputBox,
} as ComponentMeta<typeof InputBox>;

const Template: ComponentStory<typeof InputBox> = args => {
  const { value, setValue, error, setError } = useInput();

  return (
    <InputBox value={value} setValue={setValue} error={error} setError={setError}>
      <form style={{ display: 'grid', gridTemplateColumns: '4fr 1fr', alignItems: 'center', gap: '8px' }}>
        <InputBox.Input
          type="email"
          placeholder="이메일"
          onInvalid={() => {
            setError('정확한 이메일 형식을 입력해주세요');
            console.log('hi');
          }}
          required
        />
        <InputBox.SubmitButton
          onClick={() => {
            console.log('click');
          }}
        >
          인증번호 받기
        </InputBox.SubmitButton>
      </form>
      <InputBox.ErrorMessage />
    </InputBox>
  );
};

export const InputBoxTemplate = Template.bind({});
InputBoxTemplate.args = {};
