import { withRouter } from 'storybook-addon-react-router-v6';

import NotificationPage from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/NotificationPage',
  component: NotificationPage,
  decorators: [withRouter],
} as ComponentMeta<typeof NotificationPage>;

const Template = () => <NotificationPage />;

export const NotificationPageTemplate: ComponentStory<typeof NotificationPage> = Template.bind({});
NotificationPageTemplate.args = {};
