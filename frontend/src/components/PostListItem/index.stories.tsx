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
  localDate: {
    date: '2022-07-05',
    time: '15:00',
  },
  content: '안녕?',
};
