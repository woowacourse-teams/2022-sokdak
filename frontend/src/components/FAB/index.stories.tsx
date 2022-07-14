import FAB from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/FAB',
  component: FAB,
} as ComponentMeta<typeof FAB>;

const Template: ComponentStory<typeof FAB> = args => <FAB {...args} />;

export const FABTemplate = Template.bind({});
FABTemplate.args = {};
