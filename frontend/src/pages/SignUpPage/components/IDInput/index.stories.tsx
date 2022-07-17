import { useState } from 'react';

import { useInput } from '@/components/@shared/InputBox/useInput';

import IDInput from '.';
import { ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/ IDInput',
  component: IDInput,
} as ComponentMeta<typeof IDInput>;

const Template = () => {
  const { error, setError, setValue, value } = useInput();
  const [isAnimationActive, setIsAnimationActive] = useState(false);
  const [isSet, setIsSet] = useState(false);

  return (
    <IDInput
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

export const IDInputTemplate = Template.bind({});
