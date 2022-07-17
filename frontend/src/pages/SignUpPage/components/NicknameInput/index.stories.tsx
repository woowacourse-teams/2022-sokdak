import { useState } from 'react';

import { useInput } from '@/components/@shared/InputBox/useInput';

import NicknameInput from '.';
import { ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/ NicknameInput',
  component: NicknameInput,
} as ComponentMeta<typeof NicknameInput>;

const Template = () => {
  const { error, setError, setValue, value } = useInput();
  const [isAnimationActive, setIsAnimationActive] = useState(false);
  const [isSet, setIsSet] = useState(false);

  return (
    <NicknameInput
      value={value}
      setValue={setValue}
      error={error}
      setError={setError}
      isAnimationActive={isAnimationActive}
      setIsAnimationActive={setIsAnimationActive}
      isSet={isSet}
      setIsSet={setIsSet}
    />
  );
};

export const NicknameInputTemplate = Template.bind({});
