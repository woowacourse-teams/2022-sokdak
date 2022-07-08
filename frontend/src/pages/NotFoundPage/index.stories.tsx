import NotFound from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';
import { withRouter } from 'storybook-addon-react-router-v6';

export default {
  title: 'Pages/NotFound',
  component: NotFound,
  decorators: [withRouter],
} as ComponentMeta<typeof NotFound>;

const Template = (args: any) => <NotFound {...args} />;

export const NotFoundTemplate: ComponentStory<typeof NotFound> = Template.bind({});
NotFoundTemplate.args = {};
