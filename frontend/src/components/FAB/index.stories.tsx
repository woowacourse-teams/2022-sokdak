import FAB from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/FAB',
  component: FAB,
} as ComponentMeta<typeof FAB>;

const Template = (args: any) => <FAB {...args} />;

export const FABTemplate: ComponentStory<typeof FAB> = Template.bind({});
FABTemplate.args = {};
