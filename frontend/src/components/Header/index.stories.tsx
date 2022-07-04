import Header from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Header',
  component: Header,
} as ComponentMeta<typeof Header>;

const Template = (args: any) => <Header {...args} />;

export const HeaderTemplate: ComponentStory<typeof Header> = Template.bind({});
HeaderTemplate.args = {};
