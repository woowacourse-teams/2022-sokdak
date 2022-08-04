import { useState } from 'react';

import CheckBox from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/CheckBox',
  component: CheckBox,
} as ComponentMeta<typeof CheckBox>;

const Template: ComponentStory<typeof CheckBox> = args => {
  const [isChecked, setIsChecked] = useState(true);

  return <CheckBox {...args} isChecked={isChecked} setIsChecked={setIsChecked} />;
};

export const CheckBoxTemplate = Template.bind({});
CheckBoxTemplate.args = {
  labelText: '익명',
};
