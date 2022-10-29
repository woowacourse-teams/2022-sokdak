import Comment from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Pages/PostPage/Comment',
  component: Comment,
} as ComponentMeta<typeof Comment>;

const Template: ComponentStory<typeof Comment> = args => (
  <div style={{ width: '300px' }}>
    <Comment {...args} />
    <Comment {...args} />
    <Comment {...args} />
    <Comment {...args} />
  </div>
);

export const CommentTemplate = Template.bind({});
CommentTemplate.args = {
  nickname: '익명',
  content:
    '속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥 속닥속닥',
  createdAt: new Date().toISOString(),
  authorized: false,
  likeCount: 10,
};
