import { useState } from 'react';

import { useInput } from '@/components/@shared/InputBox/useInput';

import VerificationCodeInput from '.';
import { ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/VerificationCodeInput',
  component: VerificationCodeInput,
} as ComponentMeta<typeof VerificationCodeInput>;

const Template = (args: ComponentMeta<typeof VerificationCodeInput>) => {
  const { error, setError, setValue, value } = useInput();
  const [isAnimationActive, setIsAnimationActive] = useState(false);
  const email = 'test1@gmail.com';
  const [isEmailSet] = useState(true);
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
      {...args}
    />
  );
};

export const VerificationCodeInputTemplate = Template.bind({
  isEmailSet: true,
  isVerified: false,
});
