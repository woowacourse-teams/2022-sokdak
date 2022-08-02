import { useState } from 'react';

import { useInput } from '@/components/@shared/InputBox/useInput';

import EmailInput from '.';
import { ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/EmailInput',
  component: EmailInput,
} as ComponentMeta<typeof EmailInput>;

const Template = () => {
  const { error, setError, setValue, value } = useInput();
  const [isAnimationActive, setIsAnimationActive] = useState(false);
  const [isSet, setIsSet] = useState(false);
  const [isVerified] = useState(false);

  return (
    <EmailInput
      value={value}
      setValue={setValue}
      error={error}
      setError={setError}
      isAnimationActive={isAnimationActive}
      setIsAnimationActive={setIsAnimationActive}
      isSet={isSet}
      setIsSet={setIsSet}
      isVerified={isVerified}
    />
  );
};

export const EmailInputTemplate = Template.bind({});
