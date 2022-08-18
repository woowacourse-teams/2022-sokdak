import PostCountInfo from '.';
import { ComponentStory, ComponentMeta } from '@storybook/react';

export default {
  title: 'Components/PostCountInfo',
  component: PostCountInfo,
} as ComponentMeta<typeof PostCountInfo>;

const Template: ComponentStory<typeof PostCountInfo> = args => <PostCountInfo {...args} />;

export const PostCountInfoTemplate = Template.bind({});
PostCountInfoTemplate.args = {
  likeCount: 15,
  commentCount: 3,
};
