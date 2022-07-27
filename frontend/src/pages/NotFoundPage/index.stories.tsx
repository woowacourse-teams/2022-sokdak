import { withRouter } from 'storybook-addon-react-router-v6';

import NotFound from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/NotFound',
  component: NotFound,
  decorators: [withRouter],
} as ComponentMeta<typeof NotFound>;

const Template = () => <NotFound />;

export const NotFoundTemplate: ComponentStory<typeof NotFound> = Template.bind({});
NotFoundTemplate.args = {};
