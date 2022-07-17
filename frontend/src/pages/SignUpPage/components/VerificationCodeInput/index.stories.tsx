import { useState } from 'react';

import { useInput } from '@/components/@shared/InputBox/useInput';

import VerificationCodeInput from '.';
import { ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/VerificationCodeInput',
  component: VerificationCodeInput,
} as ComponentMeta<typeof VerificationCodeInput>;

const Template = () => {
  const { error, setError, setValue, value } = useInput();
  const [isAnimationActive, setIsAnimationActive] = useState(false);
  const email = 'test1@gmail.com';
  const [isEmailSet, setIsEmailSet] = useState(false);
  const [isSet, setIsSet] = useState(false);

  return (
    <VerificationCodeInput
      email={email}
      value={value}
      setValue={setValue}
      error={error}
      setError={setError}
      isAnimationActive={isAnimationActive}
      setIsAnimationActive={setIsAnimationActive}
      isVerified={isSet}
      isEmailSet={isEmailSet}
      setIsVerified={setIsSet}
    />
  );
};

export const VerificationCodeInputTemplate = Template.bind({});
