import PostListItem, { PostListItemProp } from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/PostListItem',
  component: PostListItem,
} as ComponentMeta<typeof PostListItem>;

const Template = (args: PostListItemProp) => <PostListItem {...args} />;

export const PostListItemTemplate: ComponentStory<typeof PostListItem> = Template.bind({});
PostListItemTemplate.args = {
  title: '오늘 날씨 좋네요!',
  localDate: {
    date: '2022-07-05',
    time: '15:00',
  },
  content: '안녕?',
};
