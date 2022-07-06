import Header from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';
import { withRouter } from 'storybook-addon-react-router-v6';

export default {
  title: 'Components/Header',
  component: Header,
  decorators: [withRouter],
} as ComponentMeta<typeof Header>;

const Template = (args: any) => <Header {...args} />;

export const HeaderTemplate: ComponentStory<typeof Header> = Template.bind({});
HeaderTemplate.args = {};
