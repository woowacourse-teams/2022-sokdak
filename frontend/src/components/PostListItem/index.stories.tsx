import PostListItem from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/PostListItem',
  component: PostListItem,
} as ComponentMeta<typeof PostListItem>;

const Template: ComponentStory<typeof PostListItem> = args => <PostListItem {...args} />;

export const PostListItemTemplate = Template.bind({});
PostListItemTemplate.args = {
  title: '오늘 날씨 좋네요!',
  content: '안녕?',
  createdAt: new Date('2021-07-28').toISOString(),
  likeCount: 1200,
  viewCount: 130,
  commentCount: 0,
  modified: true,
};
