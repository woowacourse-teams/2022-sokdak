import { withRouter } from 'storybook-addon-react-router-v6';

import PostItem from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/ProfilePage/PostItem',
  component: PostItem,
  decorators: [withRouter],
} as ComponentMeta<typeof PostItem>;

const Template: ComponentStory<typeof PostItem> = args => (
  <div style={{ width: '335px' }}>
    <PostItem {...args} />
  </div>
);

export const PostItemTemplate: ComponentStory<typeof PostItem> = Template.bind({});
PostItemTemplate.args = {
  title: '똥글',
  content: '똥글 동글 둥글 ~ 둥글레차',
  createdAt: new Date('2022-08-10').toISOString(),
  likeCount: 3,
  commentCount: 5,
};
