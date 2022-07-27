import PostForm from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/PostForm',
  component: PostForm,
} as ComponentMeta<typeof PostForm>;

const Template: ComponentStory<typeof PostForm> = args => <PostForm {...args} />;

export const PostFormTemplate = Template.bind({});
PostFormTemplate.args = {
  heading: '글 작성',
  submitType: '글 작성하기',
  handlePost: () => {},
};
