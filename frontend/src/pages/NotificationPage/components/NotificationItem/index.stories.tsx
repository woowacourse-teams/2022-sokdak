import { withRouter } from 'storybook-addon-react-router-v6';

import NotificationItem from '.';
import { ComponentMeta, ComponentStory } from '@storybook/react';

export default {
  title: 'Pages/NotificationPage/NotificationItem',
  component: NotificationItem,
  decorators: [withRouter],
} as ComponentMeta<typeof NotificationItem>;

const Template: ComponentStory<typeof NotificationItem> = args => <NotificationItem {...args} />;

export const NotificationItemTemplate = Template.bind({});
NotificationItemTemplate.args = {
  notification: {
    id: 1,
    content: '새로운 댓글입니다',
    createdAt: '2022-08-01',
    type: 'NEW_COMMENT',
    postId: 1,
  },
};
