import { withRouter } from 'storybook-addon-react-router-v6';

import Notification from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/Header/Notification',
  component: Notification,
  decorators: [withRouter],
} as ComponentMeta<typeof Notification>;

const Template: ComponentStory<typeof Notification> = () => <Notification />;

export const NotificationTemplate = Template.bind({});
